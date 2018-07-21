package de.jmda.fx.control.combobox;

import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent>
{
//	private final static Logger LOGGER = LogManager.getLogger(AutoCompleteComboBoxListener.class);

	// constructor injection
	private ComboBox<T> cmbBx;
	private Predicate<T> predicate;

	/** populated in constructor, holds a copy of the initial items */
	private ObservableList<T> initialItems = FXCollections.observableArrayList();

	public AutoCompleteComboBoxListener(final ComboBox<T> cmbBx, final Predicate<T> predicate)
	{
		this.cmbBx = cmbBx;
		this.predicate = predicate;

		initialItems.addAll(cmbBx.getItems());

		// if combobox was not editable all effort in this class would be superfluous
		cmbBx.setEditable(true);
		cmbBx.setOnAction
		(
				e ->
				{
					cmbBx.getEditor().end();
//					LOGGER.debug("editor: " + cmbBx.getEditor().getText() + ", value: " + cmbBx.getValue());
				}
		);
		cmbBx.setOnKeyReleased(this);
	}

	@Override public void handle(KeyEvent keyEvent)
	{
		// handle events that allow for early return
		if (keyEvent.getCode() == KeyCode.ENTER)
		{
			cmbBx.setValue(cmbBx.getSelectionModel().getSelectedItem());
//			LOGGER.debug("editor: " + cmbBx.getEditor().getText() + ", value: " + cmbBx.getValue());
			return;
		}
		if (keyEvent.getCode() == KeyCode.DOWN)
		{
			if (cmbBx.isShowing() == false)
			{
				cmbBx.show();
			}
			return;
		}
		if (   keyEvent.getCode() == KeyCode.LEFT
		    || keyEvent.getCode() == KeyCode.RIGHT
		    || keyEvent.getCode() == KeyCode.HOME
		    || keyEvent.getCode() == KeyCode.END
		    || keyEvent.getCode() == KeyCode.TAB
		    || keyEvent.getCode() == KeyCode.BACK_SPACE
		    || keyEvent.getCode() == KeyCode.DELETE
		    || keyEvent.isControlDown()
		    || keyEvent.isAltDown()
		    || keyEvent.isShiftDown()
		   )
		{
			return;
		}

		repopulate(initialItems);

		if (!cmbBx.getItems().isEmpty())
		{
			cmbBx.show();
		}
	}

	public void repopulate(ObservableList<T> newPopulation)
	{
		initialItems = newPopulation;

		// store cmbBx ui for restore after repopulation
		boolean showing = cmbBx.isShowing();
		T selectedItem = cmbBx.getSelectionModel().getSelectedItem();

		// combobox items will be repopulated, so make sure the "old" items do not show any longer on the
		// screen
		cmbBx.hide();

		// repopulate combobox.items with those initial items that match the predicate
		ObservableList<T> list = FXCollections.observableArrayList();
		cmbBx.setItems(list);

		initialItems
				.forEach
				(
						item ->
						{
							if (predicate.test(item))
							{
								list.add(item);
							}
						}
				);

		// restore cmbBx ui
		if (showing) cmbBx.show();
		if (cmbBx.getItems().contains(selectedItem)) cmbBx.getSelectionModel().select(selectedItem);
	}
}