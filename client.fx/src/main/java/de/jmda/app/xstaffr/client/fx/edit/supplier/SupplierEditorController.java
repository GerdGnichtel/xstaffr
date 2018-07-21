package de.jmda.app.xstaffr.client.fx.edit.supplier;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.core.cdi.event.ActionOnEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class SupplierEditorController implements SupplierEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(SupplierEditorController.class);

	@FXML private TableView<SupplierTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
	@FXML private TextField txtFldFilter;
	@FXML private TitledPane ttldPnDetails;
	@FXML private TextField txtFldName;
	@FXML private TextArea txtAreaText;
	@FXML private Button btnEdit;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;

	private XStaffrService xStaffrService;
	private ObservableList<SupplierTableWrapper> tableWrappers;
	private FilteredList<SupplierTableWrapper> tableWrappersFiltered;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<Supplier> selected = new SimpleObjectProperty<>();

	@FXML private void initialize()
	{
		LOGGER.debug("initialising");

		xStaffrService = XStaffrServiceProviderJPASE.instance().provide();

		selected.addListener((obs, old, newValue) -> onSelectedChanged(newValue));
		mode.addListener((obs, old, newValue) -> onModeChanged(newValue));

		mode.set(Mode.SELECT);

		ttldPnDetails.setExpanded(true);

		initTable();
		initTableControlBar();
		initDetails();

		reload();
	}

	private void initTable()
	{
		TableColumn<SupplierTableWrapper, String> tcName = new TableColumn<>("name");
		tcName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));

		tblVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblVw
				.getSelectionModel()
				.selectedItemProperty().addListener((obs, old, newValue) -> onTableSelectionChanged(newValue));

		tblVw.getColumns().add(tcName);
	}

	private void initTableControlBar()
	{
		btnRemove.setOnAction(e -> onBtnRemove());
		txtFldFilter.textProperty().addListener((obs, old, newValue) -> onFilterTextChanged(newValue));
	}

	private void initDetails()
	{
		ttldPnDetails.setExpanded(true);

		btnEdit.setOnAction(e -> onBtnEdit());
		btnUpdate.setOnAction(e -> onBtnUpdate());
		btnNew.setOnAction(e -> onBtnNew());
		btnCreate.setOnAction(e -> onBtnCreate());
		btnCancel.setOnAction(e -> onBtnCancel());
	}

	private Set<SupplierTableWrapper> toTableWrappers(Set<Supplier> suppliers)
	{
		Set<SupplierTableWrapper> result = new HashSet<>();
		suppliers.forEach(customer -> result.add(new SupplierTableWrapper(customer)));
		return result;
	}

	private Set<Supplier> tableWrappersToSet()
	{
		Set<Supplier> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getSupplier()));
		return result;
	}

	@Override public void reload()
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(xStaffrService.suppliers()));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		SortedList<SupplierTableWrapper> sortedList = new SortedList<>(tableWrappersFiltered);
		// sort the list in the same way the table is sorted (click on headers)
		sortedList.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(sortedList);
	}

	private void onTableSelectionChanged(SupplierTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getSupplier());
		}
		btnEdit.setDisable(selected.get() == null);
	}

	private void onSelectedChanged(Supplier newValue)
	{
		if (newValue == null)
		{
			clearDetails();
		}
		else
		{
			updateDetails(newValue);
		}
	}

	private void onModeChanged(Mode newValue)
	{
		if (newValue == Mode.SELECT)
		{
			setDetailsReadOnly(true);

			tblVw.setDisable(false);
			hbx.setDisable(false); // enable remove button and filter

			btnEdit.setDisable(selected.get() == null);
			btnUpdate.setDisable(true);
			btnNew.setDisable(false);
			btnCreate.setDisable(true);
			btnCancel.setDisable(true);
		}
		else if (newValue == Mode.EDIT)
		{
			setDetailsReadOnly(false);

			tblVw.setDisable(true);
			hbx.setDisable(true); // disable remove button and filter
			txtFldFilter.setText("");

			txtFldName.requestFocus();

			btnEdit.setDisable(true);
			btnUpdate.setDisable(false);
			btnNew.setDisable(true);
			btnCreate.setDisable(true);
			btnCancel.setDisable(false);
		}
		else if (newValue == Mode.NEW)
		{
			clearDetails();
			setDetailsReadOnly(false);

			tblVw.setDisable(true);
			hbx.setDisable(true); // disable remove button and filter
			txtFldFilter.setText("");

			txtFldName.requestFocus();

			btnEdit.setDisable(true);
			btnUpdate.setDisable(true);
			btnNew.setDisable(true);
			btnCreate.setDisable(false);
			btnCancel.setDisable(false);
		}
	}

	private void onFilterTextChanged(String filterExpression)
	{
		tableWrappersFiltered.setPredicate(candidateTableWrapper -> applyFilterFor(candidateTableWrapper, filterExpression));
	}

	private void onBtnRemove()
	{
		SupplierTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove supplier in db
		xStaffrService.deleteSupplier(selectedItem.getSupplier());

		tableWrappers.remove(selectedItem);

		ActionOnEvent.fire(new SuppliersChanged(this, tableWrappersToSet()));
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		Supplier supplier = selected.get();

		if (supplier == null) return;

		supplier.setName(txtFldName.getText());
		supplier.setText(txtAreaText.getText());

		// update supplier in db
		xStaffrService.updateSupplier(supplier);

		// update supplier in table
		tblVw.refresh();

		ActionOnEvent.fire(new SuppliersChanged(this, tableWrappersToSet()));
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		Supplier supplier = new Supplier(txtFldName.getText());
		supplier.setText(txtAreaText.getText());

		xStaffrService.persistSupplier(supplier);

		SupplierTableWrapper supplierTableWrapper = new SupplierTableWrapper(supplier);

		tableWrappers.add(supplierTableWrapper);

		ActionOnEvent.fire(new SuppliersChanged(this, tableWrappersToSet()));

		tblVw.getSelectionModel().select(supplierTableWrapper);

		mode.set(Mode.SELECT);
	}

	private void onBtnCancel()
	{
		updateDetails(selected.get());
		setDetailsReadOnly(true);
		tblVw.setDisable(false);

		mode.set(Mode.SELECT);
	}

	private void clearDetails()
	{
		txtFldName.setText("");
		txtAreaText.setText("");
	}

	private void setDetailsReadOnly(boolean flag)
	{
		txtFldName.setEditable(!flag);
		txtAreaText.setEditable(!flag);
	}

	private void updateDetails(Supplier newValue)
	{
		txtFldName.setText(newValue.getName());
		txtAreaText.setText(newValue.getText());
	}

	private boolean applyFilterFor(SupplierTableWrapper supplierTableWrapper, String filterExpression)
	{
		// if filterExpression is empty all candidates match
		if (filterExpression == null || filterExpression.isEmpty()) return true;

		String lowerCaseFilterExpression = filterExpression.toLowerCase();

		// check name
		if (supplierTableWrapper.getSupplier().getName().toLowerCase().contains(lowerCaseFilterExpression)) return true;

		// supplier does not match
		return false;
	}
}