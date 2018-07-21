package de.jmda.fx.control.table;

import java.util.function.Function;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class ActionButtonTableCell<T> extends TableCell<T, Boolean>
{
	private final String label;                  // constructor injection
	private final String tooltipText;            // constructor injection
	private final Function<T, Boolean> function; // constructor injection
	private final Button button;                 // constructor initialisation

	private double buttonFontSize;

	public ActionButtonTableCell(String label, String tooltipText, Function<T, Boolean> function)
	{
		this.label = label;
		this.function = function;
		this.tooltipText = tooltipText;

//		getStyleClass().add("action-button-table-cell");

		button = new Button(label);
		button.setTooltip(new Tooltip(tooltipText));
		button.setOnAction(e -> function.apply(getCurrentItem()));
///		HBox.setHgrow(button, Priority.NEVER);
//		button.setMaxWidth(Double.MAX_VALUE);
	}

	public T getCurrentItem() { return (T) getTableView().getItems().get(getIndex()); }

	@Override public void updateItem(Boolean item, boolean empty)
	{
		super.updateItem(item, empty);

		if (empty)
		{
			setGraphic(null);
		}
		else
		{
			setGraphic(button);
		}
	}

	public Callback<CellDataFeatures<T, Boolean>, ObservableValue<Boolean>> cellValueFactory()
	{
		return p -> new SimpleBooleanProperty(p.getValue() != null);
	}

	/** @return a "copy" of this instance */
	public Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> cellFactory()
	{
//		return p -> ActionButtonTableCell.this;
		return p ->
		{
			ActionButtonTableCell<T> result = new ActionButtonTableCell<>(label, tooltipText, function);
//			result.actionButton.getStyleClass().add("-fx-font-size: " + buttonFontSize + ";");
			result.button.setStyle("-fx-font-size: " + buttonFontSize + ";");
			return result;
		};
	}

	public void setButtonFontSize(double size) { buttonFontSize = size; }
}