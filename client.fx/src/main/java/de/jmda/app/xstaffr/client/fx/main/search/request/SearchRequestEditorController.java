package de.jmda.app.xstaffr.client.fx.main.search.request;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.client.fx.edit.customer.CustomerEditorService.CustomersChanged;
import de.jmda.app.xstaffr.client.fx.edit.project.ProjectEditorService.ProjectsChanged;
import de.jmda.app.xstaffr.client.fx.edit.requester.RequesterEditorService.RequestersChanged;
import de.jmda.app.xstaffr.client.fx.settings.filter.FilterEditorService.PublishFilterManagerResponse;
import de.jmda.app.xstaffr.client.fx.settings.filter.FilterManager;
import de.jmda.app.xstaffr.client.fx.util.CustomerFXUtils;
import de.jmda.app.xstaffr.client.fx.util.CustomerFXUtils.StringConverterTestCustomerName;
import de.jmda.app.xstaffr.client.fx.util.ProjectFXUtils;
import de.jmda.app.xstaffr.client.fx.util.ProjectFXUtils.StringConverterTestProjectName;
import de.jmda.app.xstaffr.client.fx.util.RequesterFXUtils;
import de.jmda.app.xstaffr.client.fx.util.RequesterFXUtils.StringConverterTestRequesterName;
import de.jmda.app.xstaffr.client.fx.util.StateFXUtils;
import de.jmda.app.xstaffr.client.fx.util.StateFXUtils.StringConverterTestState;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchRequest.State;
import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.core.cdi.event.ActionOnEvent;
import de.jmda.fx.control.combobox.AutoCompleteComboBoxListener;
import de.jmda.fx.control.table.ActionButtonTableCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class SearchRequestEditorController implements SearchRequestEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(SearchRequestEditorController.class);

	@FXML private TitledPane ttldPnTable;
	@FXML private TableView<SearchRequestTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
	@FXML private TitledPane ttldPnDetails;
	@FXML private ComboBox<Customer> cmbBxCustomer;
	@FXML private ComboBox<Project> cmbBxProject;
	@FXML private ComboBox<Requester> cmbBxRequester;
	@FXML private ComboBox<State> cmbBxState;
	@FXML private TextField txtFldRoleName;
	@FXML private DatePicker dtPckrRequestReceiptDate;
	@FXML private DatePicker dtPckrPeriodStartDate;
	@FXML private DatePicker dtPckrPeriodEndDate;
	@FXML private TextField txtFldHourlyRate;
	@FXML private TextArea txtAreaText;
	@FXML private Button btnEdit;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;
	@FXML private Button btnFilter;

	private ObjectProperty<Long> cmbBxWidthProperty = new SimpleObjectProperty<>();

	private XStaffrService xStaffrService;
	private FilterManager filterManager = new FilterManager();

	private ObservableList<SearchRequestTableWrapper> tableWrappers;
	private FilteredList<SearchRequestTableWrapper> tableWrappersFiltered;

	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<SearchRequest> selected = new SimpleObjectProperty<>();

	private AutoCompleteComboBoxListener<Customer>  autoCompleteComboBoxListenerCustomer;
	private AutoCompleteComboBoxListener<Project>   autoCompleteComboBoxListenerProject;
	private AutoCompleteComboBoxListener<Requester> autoCompleteComboBoxListenerRequester;

	@FXML private void initialize()
	{
		LOGGER.debug("initialising");

		xStaffrService = XStaffrServiceProviderJPASE.instance().provide();

		selected.addListener((obs, old, newValue) -> onSelectedChanged(newValue));
		mode.addListener((obs, old, newValue) -> onModeChanged(newValue));

		mode.set(Mode.SELECT);

		ttldPnTable.setExpanded(true);
		ttldPnDetails.setExpanded(true);

		initTable();
		initTableControlBar();
		initDetails();

		reload();

		ActionOnEvent.instance().register(PublishFilterManagerResponse.class, e -> onFilterManagerResponse((PublishFilterManagerResponse) e));

		// fire fake events to trigger handler methods
		ActionOnEvent.instance().register(CustomersChanged.class, e -> onCustomersChanged((CustomersChanged) e));
//		ActionOnEvent.fire(new CustomersChanged(this, xStaffrService.customers()));

		ActionOnEvent.instance().register(ProjectsChanged.class, e -> onProjectsChanged((ProjectsChanged) e));
//		ActionOnEvent.fire(new ProjectsChanged(this, xStaffrService.projects()));

		ActionOnEvent.instance().register(RequestersChanged.class, e -> onRequestersChanged((RequestersChanged) e));
//		ActionOnEvent.fire(new RequestersChanged(this, xStaffrService.requesters()));
	}

	private void initTable()
	{
		TableColumn<SearchRequestTableWrapper, Boolean> tcResults = new TableColumn<>();
		Function<SearchRequestTableWrapper, Boolean> function =
				p ->
				{
					ActionOnEvent.fire(new DisplaySearchResultsRequest(SearchRequestEditorController.this, p.getSearchRequest()));
					return p != null;
				};
		ActionButtonTableCell<SearchRequestTableWrapper> actionButtonTableCell =
				new ActionButtonTableCell<>(">", "display results", function);
		actionButtonTableCell.setButtonFontSize(8.0);
		tcResults.setCellValueFactory(actionButtonTableCell.cellValueFactory());
		tcResults.setCellFactory(actionButtonTableCell.cellFactory());

		TableColumn<SearchRequestTableWrapper, String> tcState = new TableColumn<>("state");
		tcState.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchRequest().getState().toString()));

		TableColumn<SearchRequestTableWrapper, String> tcCustomerName = new TableColumn<>("customer");
		tcCustomerName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchRequest().getProject().getCustomer().getName()));

		TableColumn<SearchRequestTableWrapper, String> tcProjectName = new TableColumn<>("project");
		tcProjectName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchRequest().getProject().getName()));

		TableColumn<SearchRequestTableWrapper, String> tcRequesterName = new TableColumn<>("requester");
		tcRequesterName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchRequest().getRequester().getName()));

		TableColumn<SearchRequestTableWrapper, String> tcDateOfRequestReceipt = new TableColumn<>("request receipt");
		tcDateOfRequestReceipt.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDateOfRequestReceipt()));

		TableColumn<SearchRequestTableWrapper, String> tcDateOfPeriodStart = new TableColumn<>("period start");
		tcDateOfPeriodStart.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDateOfOfPeriodStart()));

		TableColumn<SearchRequestTableWrapper, String> tcDateOfPeriodEnd = new TableColumn<>("period end");
		tcDateOfPeriodEnd.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDateOfOfPeriodEnd()));

		TableColumn<SearchRequestTableWrapper, String> tcRoleName = new TableColumn<>("role");
		tcRoleName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getRoleName()));

		TableColumn<SearchRequestTableWrapper, String> tcHourlyRate = new TableColumn<>("hourly rate");
		tcHourlyRate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getHourlyRate()));

		tblVw.getColumns().add(tcResults);
		tblVw.getColumns().add(tcState);
		tblVw.getColumns().add(tcCustomerName);
		tblVw.getColumns().add(tcProjectName);
		tblVw.getColumns().add(tcRequesterName);
		tblVw.getColumns().add(tcDateOfRequestReceipt);
		tblVw.getColumns().add(tcDateOfPeriodStart);
		tblVw.getColumns().add(tcDateOfPeriodEnd);
		tblVw.getColumns().add(tcRoleName);
		tblVw.getColumns().add(tcHourlyRate);

		tblVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblVw
				.getSelectionModel()
				.selectedItemProperty().addListener((obs, old, newValue) -> onTableSelectionChanged(newValue));

		// set text in row to bold if request has results
		tblVw.setRowFactory
		(
				tv ->
				new TableRow<SearchRequestTableWrapper>()
				{
					@Override public void updateItem(SearchRequestTableWrapper item, boolean empty)
					{
						super.updateItem(item, empty);

						// fast lane return
						if (item == null) return;

						SearchRequest searchRequest = item.getSearchRequest();

//						// fast lane return
//						if (filterManager.getPredicateFor(searchRequest)) return;

						String style = "";

						if (searchRequest.getSearchResults().size() > 0) style += "-fx-font-weight:bold;";

//						State state = searchRequest.getState();
//						if      (state.equals(State.OPEN))            style += "-fx-background-color:yellow;";
//						else if (state.equals(State.CLOSED_SOLVED))   style += "-fx-background-color:lime;";
//						else if (state.equals(State.CLOSED_UNSOLVED)) style += "-fx-background-color:tomato;";

						setStyle(style);
//						LOGGER.debug("row style: " + getStyle());
					}
				}
		);
	}

	private void initTableControlBar() { btnRemove.setOnAction(e -> onBtnRemove()); }

	private void initDetails()
	{
		initDetailsCmbBxCustomer();
		initDetailsCmbBxProject();
		initDetailsCmbBxRequester();
		initDetailsCmbBxState();

		cmbBxWidthProperty.set(0L);
		cmbBxWidthProperty.addListener((obs, old, newValue) -> synchroniseCmbBxWidths());

		cmbBxCustomer .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxProject  .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxRequester.widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxState    .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));

		btnEdit  .setOnAction(e -> onBtnEdit());
		btnUpdate.setOnAction(e -> onBtnUpdate());
		btnNew   .setOnAction(e -> onBtnNew());
		btnCreate.setOnAction(e -> onBtnCreate());
		btnCancel.setOnAction(e -> onBtnCancel());
		btnFilter.setOnAction(e -> onBtnFilter());
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

	private CustomerFXUtils.StringConverter stringConverterCustomers;
	private CustomerFXUtils.StringConverter getStringConverterCustomers()
	{
		if (stringConverterCustomers == null)
		{
			stringConverterCustomers =
					new CustomerFXUtils.StringConverter(
							cmbBxCustomer.getItems(), new StringConverterTestCustomerName());
		}
		return stringConverterCustomers;
	}

	private void initDetailsCmbBxCustomer()
	{
		ObservableList<Customer> customers = FXCollections.observableArrayList(xStaffrService.customers());
		SortedList<Customer> customersSorted = new SortedList<>(customers);
		customersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxCustomer.setItems(customersSorted);

		CustomerFXUtils.CellFactory cellFactory = new CustomerFXUtils.CellFactory();

//		stringConverterCustomers =
//				new CustomerFXUtils.StringConverter(
//						cmbBxCustomer.getItems(), new StringConverterTestCustomerName());

		cmbBxCustomer.setButtonCell(cellFactory.call(null));
		cmbBxCustomer.setCellFactory(cellFactory);
		cmbBxCustomer.setConverter(getStringConverterCustomers());

		Predicate<Customer> predicate = new AutoCompletePredicateCustomerName();
		autoCompleteComboBoxListenerCustomer = new AutoCompleteComboBoxListener<>(cmbBxCustomer, predicate);
		cmbBxCustomer.getEditor().textProperty().addListener((obs, old, newValue) -> onCmbBxEditorTextChanged(obs, old, newValue));
	}

	private Object onCmbBxEditorTextChanged(ObservableValue<? extends String> obs, String old, String newValue)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private class AutoCompletePredicateProjectName implements Predicate<Project>
	{
		@Override public boolean test(Project project)
		{
			return
					project
							.getName()
							.toLowerCase()
							.contains(cmbBxProject.getEditor().getText().toLowerCase());
		}
	}

	private ProjectFXUtils.StringConverter stringConverterProjects;
	private ProjectFXUtils.StringConverter getStringConverterProjects()
	{
		if (stringConverterProjects == null)
		{
			stringConverterProjects =
					new ProjectFXUtils.StringConverter(
							cmbBxProject.getItems(), new StringConverterTestProjectName());
		}
		return stringConverterProjects;
	}

	private void initDetailsCmbBxProject()
	{
		cmbBxCustomer
				.valueProperty()
				.addListener
				(
						(obs, old, newValue) ->
						{
							if (newValue == null) return;
							ObservableList<Project> projects = FXCollections.observableArrayList(FXCollections.observableArrayList(newValue.getProjects()));
							SortedList<Project> projectsSorted = new SortedList<>(projects);
							projectsSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
							cmbBxProject.setItems(projectsSorted);
						}
				);

		ProjectFXUtils.CellFactory cellFactory = new ProjectFXUtils.CellFactory();

//		stringConverterProjects =
//				new ProjectFXUtils.StringConverter(
//						cmbBxProject.getItems(), new StringConverterTestProjectName());

		cmbBxProject.setButtonCell(cellFactory.call(null));
		cmbBxProject.setCellFactory(cellFactory);
		cmbBxProject.setConverter(getStringConverterProjects());

		Predicate<Project> predicate = new AutoCompletePredicateProjectName();
		autoCompleteComboBoxListenerProject = new AutoCompleteComboBoxListener<>(cmbBxProject, predicate);
	}

	private class AutoCompletePredicateRequesterName implements Predicate<Requester>
	{
		@Override public boolean test(Requester requester)
		{
			return
					requester
							.getName()
							.toLowerCase()
							.contains(cmbBxRequester.getEditor().getText().toLowerCase());
		}
	}

	private RequesterFXUtils.StringConverter stringConverterRequesters;
	private RequesterFXUtils.StringConverter getStringConverterRequesters()
	{
		if (stringConverterRequesters == null)
		{
			stringConverterRequesters =
					new RequesterFXUtils.StringConverter(
							cmbBxRequester.getItems(), new StringConverterTestRequesterName());
		}
		return stringConverterRequesters;
	}

	private void initDetailsCmbBxRequester()
	{
		ObservableList<Requester> requesters = FXCollections.observableArrayList(xStaffrService.requesters());
		SortedList<Requester> requestersSorted = new SortedList<>(requesters);
		requestersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxRequester.setItems(requestersSorted);

		RequesterFXUtils.CellFactory cellFactory = new RequesterFXUtils.CellFactory();

//		stringConverterRequesters =
//				new RequesterFXUtils.StringConverter(
//						cmbBxRequester.getItems(), new StringConverterTestRequesterName());

		cmbBxRequester.setButtonCell(cellFactory.call(null));
		cmbBxRequester.setCellFactory(cellFactory);
		cmbBxRequester.setConverter(getStringConverterRequesters());

		Predicate<Requester> predicate = new AutoCompletePredicateRequesterName();
		autoCompleteComboBoxListenerRequester = new AutoCompleteComboBoxListener<>(cmbBxRequester, predicate);
	}

	private class AutoCompletePredicateState implements Predicate<State>
	{
		@Override public boolean test(State state)
		{
			return
					state
							.toString()
							.toLowerCase()
							.contains(cmbBxState.getEditor().getText().toLowerCase());
		}
	}

	private StateFXUtils.StringConverter stringConverterState;
	private StateFXUtils.StringConverter getStringConverterState()
	{
		if (stringConverterState == null)
		{
			stringConverterState =
					new StateFXUtils.StringConverter(
							cmbBxState.getItems(), new StringConverterTestState());
		}
		return stringConverterState;
	}

	private void initDetailsCmbBxState()
	{
		ObservableList<State> states = FXCollections.observableArrayList(State.values());
		SortedList<State> statesSorted = new SortedList<>(states);
		statesSorted.comparatorProperty().set((o1, o2) -> Integer.compare(o1.ordinal(), o2.ordinal()));
		cmbBxState.setItems(statesSorted);

		StateFXUtils.CellFactory cellFactory = new StateFXUtils.CellFactory();

//		StateFXUtils.StringConverter stringConverterState =
//				new StateFXUtils.StringConverter(
//						cmbBxState.getItems(), new StringConverterTestState());

		cmbBxState.setButtonCell(cellFactory.call(null));
		cmbBxState.setCellFactory(cellFactory);
		cmbBxState.setConverter(getStringConverterState());

		Predicate<State> predicate = new AutoCompletePredicateState();

		new AutoCompleteComboBoxListener<>(cmbBxState, predicate);
	}

	private Set<SearchRequestTableWrapper> toTableWrappers(Set<SearchRequest> searchRequests)
	{
		Set<SearchRequestTableWrapper> result = new HashSet<>();
		searchRequests.forEach(searchRequest -> result.add(new SearchRequestTableWrapper(searchRequest)));
		return result;
	}

	private Set<SearchRequest> tableWrappersToSet()
	{
		Set<SearchRequest> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getSearchRequest()));
		return result;
	}

	@Override public void reload()
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(xStaffrService.searchRequests()));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		SortedList<SearchRequestTableWrapper> sortedList = new SortedList<>(tableWrappersFiltered);
		// sort the list in the same way the table is sorted (click on headers)
		sortedList.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(sortedList);
	}

	private void onTableSelectionChanged(SearchRequestTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getSearchRequest());
		}
		btnEdit.setDisable(selected.get() == null);
	}

	private void onSelectedChanged(SearchRequest newValue)
	{
		if (newValue == null)
		{
			clearDetails();
		}
		else
		{
			updateDetails(newValue);
		}

		// fire event to notify listeners of selection change
		ActionOnEvent.fire(new SearchRequestSelectedChanged(this, newValue));
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
			repopulateComboBoxes();
			setDetailsReadOnly(false);

			tblVw.setDisable(true);
			hbx.setDisable(true); // disable remove button

			cmbBxCustomer.requestFocus();

			btnEdit.setDisable(true);
			btnUpdate.setDisable(false);
			btnNew.setDisable(true);
			btnCreate.setDisable(true);
			btnCancel.setDisable(false);
		}
		else if (newValue == Mode.NEW)
		{
			clearDetails();
			repopulateComboBoxes();
			setDetailsReadOnly(false);

			tblVw.setDisable(true);
			hbx.setDisable(true); // disable remove button

			cmbBxCustomer.requestFocus();

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
		autoCompleteComboBoxListenerCustomer.repopulate(customersSorted);
		stringConverterCustomers.repopulate(e.getCustomers());
	}

	private void onProjectsChanged(ProjectsChanged e)
	{
		ObservableList<Project> projects = FXCollections.observableArrayList(e.getProjects());
		SortedList<Project> projectsSorted = new SortedList<>(projects);
		projectsSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		autoCompleteComboBoxListenerProject.repopulate(projectsSorted);
		stringConverterProjects.repopulate(e.getProjects());
	}

	private void onRequestersChanged(RequestersChanged e)
	{
		ObservableList<Requester> requesters = FXCollections.observableArrayList(e.getRequesters());
		SortedList<Requester> requestersSorted = new SortedList<>(requesters);
		requestersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		autoCompleteComboBoxListenerRequester.repopulate(requestersSorted);
		stringConverterRequesters.repopulate(e.getRequesters());
	}

	private void onBtnRemove()
	{
		SearchRequestTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove search request in db
		xStaffrService.deleteSearchRequest(selectedItem.getSearchRequest());

		tableWrappers.remove(selectedItem);

		ActionOnEvent.fire(new SearchRequestsChanged(this, tableWrappersToSet()));
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		SearchRequest searchRequest = selected.get();

		if (searchRequest == null) return;

		searchRequest.setProject(cmbBxProject.getValue());
		searchRequest.setRequester(cmbBxRequester.getValue());
		searchRequest.setState(cmbBxState.getValue());
		searchRequest.setReceipt(dtPckrRequestReceiptDate.getValue());
		searchRequest.setInception(dtPckrPeriodStartDate.getValue());
		searchRequest.setExpiration(dtPckrPeriodEndDate.getValue());
		searchRequest.setRolename(txtFldRoleName.getText());
		searchRequest.setHourlyRate(new BigDecimal(txtFldHourlyRate.getText()));
		searchRequest.setText(txtAreaText.getText());

		// update search request in db
		xStaffrService.updateSearchRequest(searchRequest);

		// update search request in table
		tblVw.refresh();

		mode.set(Mode.SELECT);

		ActionOnEvent.fire(new SearchRequestsChanged(this, tableWrappersToSet()));
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		SearchRequest searchRequest =
				new SearchRequest(
						cmbBxProject.getValue(),
						cmbBxRequester.getValue(),
						dtPckrRequestReceiptDate.getValue(),
						dtPckrPeriodStartDate.getValue(),
						dtPckrPeriodEndDate.getValue(),
						txtFldRoleName.getText());

		searchRequest.setHourlyRate(new BigDecimal(txtFldHourlyRate.getText()));
		searchRequest.setText(txtAreaText.getText());

		xStaffrService.persistSearchRequest(searchRequest);

		SearchRequestTableWrapper tableWrapper = new SearchRequestTableWrapper(searchRequest);

		tableWrappers.add(tableWrapper);

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

	private void onCmbBxWidthChanged(ObservableValue<? extends Number> obs, Number old, Number newValue)
	{
		if (newValue.longValue() > old.longValue()) cmbBxWidthProperty.set(newValue.longValue());
	}

	private void synchroniseCmbBxWidths()
	{
		cmbBxCustomer .setPrefWidth(cmbBxWidthProperty.get());
		cmbBxProject  .setPrefWidth(cmbBxWidthProperty.get());
		cmbBxRequester.setPrefWidth(cmbBxWidthProperty.get());
		cmbBxState    .setPrefWidth(cmbBxWidthProperty.get());
	}

	private void clearDetails()
	{
		cmbBxCustomer.setValue(null);
		cmbBxProject.setValue(null);
		cmbBxRequester.setValue(null);
		cmbBxState.setValue(null);

		dtPckrRequestReceiptDate.setValue(null);
		dtPckrPeriodStartDate.setValue(null);
		dtPckrPeriodEndDate.setValue(null);
		txtFldRoleName.setText("");
		txtFldHourlyRate.setText("");
		txtAreaText.setText("");
	}

	private void repopulateComboBoxes()
	{
		// trigger onXXXChangedHandlers to repopulate combo boxes
		onCustomersChanged(new CustomersChanged( this, xStaffrService.customers()));
		onProjectsChanged(new ProjectsChanged(this, xStaffrService.projects()));
		onRequestersChanged(new RequestersChanged(this, xStaffrService.requesters()));
	}

	private void setDetailsReadOnly(boolean flag)
	{
//		cmbBxCustomer.setEditable(!flag);
		cmbBxCustomer.setDisable(flag);
//		if (flag)
//		{
//			cmbBxCustomer.getButtonCell().setTextFill(Color.BLACK);
//			cmbBxCustomer.getButtonCell().setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
//			cmbBxCustomer.getButtonCell().setOpacity(1);
////			cmbBxCustomer.getEditor().setOpacity(1);
////			cmbBxCustomer.setOpacity(1);
////			cmbBxCustomer.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
//		}
//		cmbBxProject.setEditable(!flag);
		cmbBxProject.setDisable(flag);
//		cmbBxRequester.setEditable(!flag);
		cmbBxRequester.setDisable(flag);
//		cmbBxState.setEditable(!flag);
		cmbBxState.setDisable(flag);
//		dtPckrRequestReceiptDate.setEditable(!flag);
		dtPckrRequestReceiptDate.setDisable(flag);;
//		dtPckrPeriodStartDate.setEditable(!flag);
		dtPckrPeriodStartDate.setDisable(flag);
//		dtPckrPeriodStartDate.setEditable(!flag);
		dtPckrPeriodEndDate.setDisable(flag);
//		dtPckrPeriodEndDate.setEditable(!flag);
		txtFldRoleName.setEditable(!flag);
		txtFldHourlyRate.setEditable(!flag);
		txtAreaText.setEditable(!flag);
	}

//	private void disableDetails(boolean flag)
//	{
//		cmbBxCustomer.setDisable(flag);
//		cmbBxProject.setDisable(flag);
//		cmbBxRequester.setDisable(flag);
//		cmbBxState.setDisable(flag);
//		dtPckrRequestReceiptDate.setDisable(flag);
//		dtPckrPeriodStartDate.setDisable(flag);
//		dtPckrPeriodEndDate.setDisable(flag);
//		txtFldRoleName.setDisable(flag);
//		txtFldHourlyRate.setDisable(flag);
//		txtAreaText.setDisable(flag);
//	}

	private void updateDetails(SearchRequest searchRequest)
	{
		Project project = searchRequest.getProject();
		// handle lazy loading (fetch customer with projects)
//		Customer customer = xStaffrService.findCustomerByNameWithProjects(project.getCustomer().getName());
		Customer customer = project.getCustomer();

		updateDetailsCmbBxProjects(customer);

		cmbBxCustomer.setValue(customer);
		cmbBxProject.setValue(project);
		cmbBxRequester.setValue(searchRequest.getRequester());
		cmbBxState.setValue(searchRequest.getState());
		dtPckrRequestReceiptDate.setValue(searchRequest.getReceipt());
		dtPckrPeriodStartDate.setValue(searchRequest.getInception());
		dtPckrPeriodEndDate.setValue(searchRequest.getExpiration());
		txtFldRoleName.setText(searchRequest.getRolename());
		txtFldHourlyRate.setText(searchRequest.getHourlyRate() == null ? "" : searchRequest.getHourlyRate().toString());
		txtAreaText.setText(searchRequest.getText());
	}

	private void updateDetailsCmbBxProjects(Customer customer)
	{
		ObservableList<Project> projects = FXCollections.observableArrayList(customer.getProjects());
		SortedList<Project> projectsSorted = new SortedList<>(projects);
		projectsSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxProject.setItems(projectsSorted);
	}

	/** Non lambda {@code Consumer} is easier to trace ... */
	private class FilterManagerResponseConsumer implements Consumer<PublishFilterManagerResponse>
	{
		@Override public void accept(PublishFilterManagerResponse response)
		{
			filterManager = response.getFilterManager();
			tableWrappersFiltered.setPredicate
			(
					searchRequestTableWrapper -> filterManager.getPredicateFor(searchRequestTableWrapper.getSearchRequest())
			);
			tblVw.refresh();
		}
	}

	private void onFilterManagerResponse(PublishFilterManagerResponse e)
	{
		FilterManagerResponseConsumer filterManagerResponseConsumer = new FilterManagerResponseConsumer();
		filterManagerResponseConsumer.accept(e);
	}

	private void onBtnFilter()
	{
		ActionOnEvent.fire(new DisplayFilterEditorRequest(this));
//		PublishFilterManagerRequest         eventRequest          = new PublishFilterManagerRequest(this);
//		Class<PublishFilterManagerResponse> eventResponseType     = PublishFilterManagerResponse.class;
//		FilterManagerResponseConsumer       eventResponseConsumer = new FilterManagerResponseConsumer();
//
//		EventDrivenProcessStep eventDrivenProcessStep =
//				new EventDrivenProcessStep(eventRequest, eventResponseType, eventResponseConsumer);
//		EventDrivenProcess     eventDrivenProcess     =
//				new EventDrivenProcess(asList(eventDrivenProcessStep));
//
//		eventDrivenProcess.execute();
//
////		tableWrappersFiltered.setPredicate
////		(
////				searchRequestTableWrapper -> filterManager.getPredicateFor(searchRequestTableWrapper.getSearchRequest())
////		);
////		tblVw.refresh();
//		btnFilter.setDisable(true);
	}
//
//	private void onBtnFilterDeactivate()
//	{
////		tableWrappersFiltered.setPredicate(null);
////		tblVw.refresh();
//		btnFilter.setDisable(false);
//		btnFilterDeactivate.setDisable(true);
//	}
}