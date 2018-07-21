package de.jmda.app.xstaffr.client.fx.edit.project;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.client.fx.edit.customer.CustomerEditorService.CustomersChanged;
import de.jmda.app.xstaffr.client.fx.util.CustomerFXUtils;
import de.jmda.app.xstaffr.client.fx.util.CustomerFXUtils.StringConverterTestCustomerName;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.core.cdi.event.ActionOnEvent;
import de.jmda.fx.control.combobox.AutoCompleteComboBoxListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class ProjectEditorController implements ProjectEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(ProjectEditorController.class);

	@FXML private TableView<ProjectTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
	@FXML private TextField txtFldFilter;
	@FXML private TitledPane ttldPnDetails;
	@FXML private ComboBox<Customer> cmbBxCustomer;
	@FXML private TextField txtFldName;
	@FXML private Button btnEdit;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;

	private XStaffrService xStaffrService;
	private ObservableList<ProjectTableWrapper> tableWrappers;
	private FilteredList<ProjectTableWrapper> tableWrappersFiltered;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<Project> selected = new SimpleObjectProperty<>();
	private AutoCompleteComboBoxListener<Customer> autoCompleteComboBoxListener;

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

		ActionOnEvent.instance().register(CustomersChanged.class, e -> onCustomersChanged((CustomersChanged) e));
		ActionOnEvent.fire(new CustomersChanged(this, xStaffrService.customers()));
	}

	private void initTable()
	{
		TableColumn<ProjectTableWrapper, String> tcCustomerName = new TableColumn<>("customer name");
		tcCustomerName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getProject().getCustomer().getName()));

		TableColumn<ProjectTableWrapper, String> tcProjectName = new TableColumn<>("project name");
		tcProjectName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));

		tblVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblVw
				.getSelectionModel()
				.selectedItemProperty().addListener((obs, old, newValue) -> onTableSelectionChanged(newValue));

		tblVw.getColumns().add(tcCustomerName);
		tblVw.getColumns().add(tcProjectName);
	}

	private void initTableControlBar()
	{
		btnRemove.setOnAction(e -> onBtnRemove());
		txtFldFilter.textProperty().addListener((obs, old, newValue) -> onFilterTextChanged(newValue));
	}

	private void initDetails()
	{
		ttldPnDetails.setExpanded(true);

		initDetailsCmbBxCustomer();

		selected.addListener((obs, old, newValue) -> cmbBxCustomer.setValue(newValue.getCustomer()));

		btnEdit.setOnAction(e -> onBtnEdit());
		btnUpdate.setOnAction(e -> onBtnUpdate());
		btnNew.setOnAction(e -> onBtnNew());
		btnCreate.setOnAction(e -> onBtnCreate());
		btnCancel.setOnAction(e -> onBtnCancel());
	}

	private class AutoCompletePredicateCustomerName implements Predicate<Customer>
	{
		@Override public boolean test(Customer customer)
		{
			return
					customer
							.getName()
							.toLowerCase()
							.contains(cmbBxCustomer.getEditor().getText().toLowerCase());
		}
	}

	private CustomerFXUtils.StringConverter stringConverter;

	private void initDetailsCmbBxCustomer()
	{
		CustomerFXUtils.CellFactory cellFactory = new CustomerFXUtils.CellFactory();

		stringConverter =
				new CustomerFXUtils.StringConverter(
						cmbBxCustomer.getItems(), new StringConverterTestCustomerName());

		cmbBxCustomer.setButtonCell(cellFactory.call(null));
		cmbBxCustomer.setCellFactory(cellFactory);
		cmbBxCustomer.setConverter(stringConverter);

		Predicate<Customer> predicate = new AutoCompletePredicateCustomerName();
		autoCompleteComboBoxListener = new AutoCompleteComboBoxListener<>(cmbBxCustomer, predicate);
	}

	private Set<ProjectTableWrapper> toTableWrappers(Set<Project> projects)
	{
		Set<ProjectTableWrapper> result = new HashSet<>();
		projects.forEach(customer -> result.add(new ProjectTableWrapper(customer)));
		return result;
	}

	private Set<Project> tableWrappersToSet()
	{
		Set<Project> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getProject()));
		return result;
	}

	@Override public void reload()
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(xStaffrService.projects()));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		Comparator<ProjectTableWrapper> comparator = (ptw1, ptw2) ->
		{
			int customerNameComparison =
					ptw1.getProject().getCustomer().getName().compareTo(
					ptw2.getProject().getCustomer().getName());
			if (customerNameComparison == 0)
			{
				return ptw1.getProject().getName().compareTo(ptw2.getProject().getName());
			}
			else return customerNameComparison;
		};
		SortedList<ProjectTableWrapper> tableWrappersFilteredSorted =
				new SortedList<>(tableWrappersFiltered, comparator);
		// sort the list in the same way the table is sorted (click on headers)
//		tableWrappersFilteredSorted.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(tableWrappersFilteredSorted);
	}

	private void onTableSelectionChanged(ProjectTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getProject());
		}
	}

	private void onSelectedChanged(Project newValue)
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

	private void onCustomersChanged(CustomersChanged e)
	{
		ObservableList<Customer> customers = FXCollections.observableArrayList(e.getCustomers());
		SortedList<Customer> customersSorted = new SortedList<>(customers);
		customersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		autoCompleteComboBoxListener.repopulate(customersSorted);
		stringConverter.repopulate(e.getCustomers());
	}

	private void onFilterTextChanged(String filterExpression)
	{
		tableWrappersFiltered.setPredicate(tableWrapper -> applyFilterFor(tableWrapper, filterExpression));
	}

	private void onBtnRemove()
	{
		ProjectTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove project in db
		xStaffrService.deleteProject(selectedItem.getProject());

		tableWrappers.remove(selectedItem);

		ActionOnEvent.fire(new ProjectsChanged(this, tableWrappersToSet()));
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		Project project = selected.get();

		if (project == null) return;

		project.setName(txtFldName.getText());

		// update project in db
		xStaffrService.updateProject(project);

		// update project in table
		tblVw.refresh();

		ActionOnEvent.fire(new ProjectsChanged(this, tableWrappersToSet()));
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		Customer customer = cmbBxCustomer.getValue();
		Project project = new Project(customer, txtFldName.getText());

		xStaffrService.persistProject(project);

		ProjectTableWrapper tableWrapper = new ProjectTableWrapper(project);

		tableWrappers.add(tableWrapper);

		ActionOnEvent.fire(new ProjectsChanged(this, tableWrappersToSet()));

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
		cmbBxCustomer.setValue(null);
		txtFldName.setText("");
	}

	private void setDetailsReadOnly(boolean flag)
	{
		cmbBxCustomer.setEditable(!flag);
		cmbBxCustomer.setDisable(flag);
		txtFldName.setEditable(!flag);
	}

//	private void disableDetails(boolean flag)
//	{
//		cmbBxCustomer.setDisable(flag);
//		txtFldName.setDisable(flag);
//	}

	private void updateDetails(Project newValue)
	{
		cmbBxCustomer.setValue(newValue.getCustomer());
		txtFldName.setText(newValue.getName());
	}

	private boolean applyFilterFor(ProjectTableWrapper tableWrapper, String filterExpression)
	{
		// if filterExpression is empty all projects match
		if (filterExpression == null || filterExpression.isEmpty()) return true;

		String lowerCaseFilterExpression = filterExpression.toLowerCase();

		// check name
		if (tableWrapper.getProject().getName().toLowerCase().contains(lowerCaseFilterExpression)) return true;

		// project does not match
		return false;
	}
}