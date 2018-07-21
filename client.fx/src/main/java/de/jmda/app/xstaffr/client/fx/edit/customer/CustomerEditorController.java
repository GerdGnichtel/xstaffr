package de.jmda.app.xstaffr.client.fx.edit.customer;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.common.domain.Customer;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class CustomerEditorController implements CustomerEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(CustomerEditorController.class);

	@FXML private TableView<CustomerTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
  @FXML private TextField txtFldFilter;
	@FXML private TitledPane ttldPnDetails;
  @FXML private TextField txtFldName;
	@FXML private Button btnEdit;
  @FXML private Button btnUpdate;
  @FXML private Button btnNew;
  @FXML private Button btnCreate;
  @FXML private Button btnCancel;

	private XStaffrService xStaffrService;
	private ObservableList<CustomerTableWrapper> tableWrappers;
	private FilteredList<CustomerTableWrapper> tableWrappersFiltered;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<Customer> selected = new SimpleObjectProperty<>();

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
		TableColumn<CustomerTableWrapper, String> tcName = new TableColumn<>("name");
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

	private Set<CustomerTableWrapper> toTableWrappers(Set<Customer> customers)
	{
		Set<CustomerTableWrapper> result = new HashSet<>();
		customers.forEach(customer -> result.add(new CustomerTableWrapper(customer)));
		return result;
	}

	private Set<Customer> tableWrappersToSet()
	{
		Set<Customer> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getCustomer()));
		return result;
	}

	@Override public void reload()
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(xStaffrService.customers()));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		SortedList<CustomerTableWrapper> tableWrappersFilteredSorted = new SortedList<>(tableWrappersFiltered);
		// sort the list in the same way the table is sorted (click on headers)
//		tableWrappersFilteredSorted.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(tableWrappersFilteredSorted);
	}

	private void onTableSelectionChanged(CustomerTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getCustomer());
		}
	}

	private void onSelectedChanged(Customer newValue)
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
		tableWrappersFiltered.setPredicate(tableWrapper -> applyFilterFor(tableWrapper, filterExpression));
	}

	private void onBtnRemove()
	{
		CustomerTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove candidate in db
		xStaffrService.deleteCustomer(selectedItem.getCustomer());

		tableWrappers.remove(selectedItem);

		ActionOnEvent.fire(new CustomersChanged(this, tableWrappersToSet()));
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		Customer customer = selected.get();

		if (customer == null) return;

		customer.setName(txtFldName.getText());

		// update customer in db
		xStaffrService.updateCustomer(customer);

		// update customer in table
		tblVw.refresh();

		ActionOnEvent.fire(new CustomersChanged(this, tableWrappersToSet()));
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		Customer customer = new Customer(txtFldName.getText());

		xStaffrService.persistCustomer(customer);

		CustomerTableWrapper tableWrapper = new CustomerTableWrapper(customer);

		tableWrappers.add(tableWrapper);

		ActionOnEvent.fire(new CustomersChanged(this, tableWrappersToSet()));

		tblVw.getSelectionModel().select(tableWrapper);

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
	}

	private void setDetailsReadOnly(boolean flag)
	{
		txtFldName.setEditable(!flag);
	}

	private void updateDetails(Customer newValue)
	{
		txtFldName.setText(newValue.getName());
	}

	private boolean applyFilterFor(CustomerTableWrapper tableWrapper, String filterExpression)
	{
		// if filterExpression is empty all candidates match
		if (filterExpression == null || filterExpression.isEmpty()) return true;

		String lowerCaseFilterExpression = filterExpression.toLowerCase();

		// check name
		if (tableWrapper.getCustomer().getName().toLowerCase().contains(lowerCaseFilterExpression)) return true;

		// customer does not match
		return false;
	}
}