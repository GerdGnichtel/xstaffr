package de.jmda.app.xstaffr.client.fx.settings.filter;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.client.fx.edit.candidate.CandidateEditorService.CandidatesChanged;
import de.jmda.app.xstaffr.client.fx.edit.customer.CustomerEditorService.CustomersChanged;
import de.jmda.app.xstaffr.client.fx.edit.project.ProjectEditorService.ProjectsChanged;
import de.jmda.app.xstaffr.client.fx.edit.requester.RequesterEditorService.RequestersChanged;
import de.jmda.app.xstaffr.client.fx.edit.supplier.SupplierEditorService.SuppliersChanged;
import de.jmda.app.xstaffr.client.fx.util.CandidateFXUtils;
import de.jmda.app.xstaffr.client.fx.util.CandidateFXUtils.StringConverterTestCandidateUniqueName;
import de.jmda.app.xstaffr.client.fx.util.CustomerFXUtils;
import de.jmda.app.xstaffr.client.fx.util.CustomerFXUtils.StringConverterTestCustomerName;
import de.jmda.app.xstaffr.client.fx.util.ProjectFXUtils;
import de.jmda.app.xstaffr.client.fx.util.ProjectFXUtils.StringConverterTestProjectName;
import de.jmda.app.xstaffr.client.fx.util.RequesterFXUtils;
import de.jmda.app.xstaffr.client.fx.util.RequesterFXUtils.StringConverterTestRequesterName;
import de.jmda.app.xstaffr.client.fx.util.SupplierFXUtils;
import de.jmda.app.xstaffr.client.fx.util.SupplierFXUtils.StringConverterTestSupplierName;
import de.jmda.app.xstaffr.client.fx.util.StateFXUtils;
import de.jmda.app.xstaffr.client.fx.util.StateFXUtils.StringConverterTestState;
import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.SearchRequest.State;
import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.core.cdi.event.ActionOnEvent;
import de.jmda.fx.FXUtil;
import de.jmda.fx.control.combobox.AutoCompleteComboBoxListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class FilterEditorController implements FilterEditorService
{
	private final static Logger LOGGER = LogManager.getLogger(FilterEditorController.class);

	@FXML private ComboBox<Candidate> cmbBxCandidate;
	@FXML private ComboBox<Customer>  cmbBxCustomer;
	@FXML private ComboBox<Project>   cmbBxProject;
	@FXML private ComboBox<Requester> cmbBxRequester;
	@FXML private ComboBox<Supplier>  cmbBxSupplier;
	@FXML private ComboBox<State>     cmbBxState;

	@FXML private CheckBox addCriterionCandidate;
	@FXML private CheckBox addCriterionCustomer;
	@FXML private CheckBox addCriterionProject;
	@FXML private CheckBox addCriterionRequester;
	@FXML private CheckBox addCriterionSupplier;
	@FXML private CheckBox addCriterionState;

	@FXML private Button btnActivate;
	@FXML private Button btnDeactivate;

	private ObjectProperty<Long> cmbBxWidthProperty = new SimpleObjectProperty<>();

	private XStaffrService xStaffrService;
	private FilterManager filterManager;

	@Override public void reload()
	{
		// fire change events to trigger change event procedures
		ActionOnEvent.fire(new CandidatesChanged(this, xStaffrService.candidates()));
		ActionOnEvent.fire(new CustomersChanged(this, xStaffrService.customers()));
		ActionOnEvent.fire(new ProjectsChanged(this, xStaffrService.projects()));
		ActionOnEvent.fire(new RequestersChanged(this, xStaffrService.requesters()));
		ActionOnEvent.fire(new SuppliersChanged(this, xStaffrService.suppliers()));
	}

	@FXML private void initialize()
	{
		LOGGER.debug("initialising");

		xStaffrService = XStaffrServiceProviderJPASE.instance().provide();
		filterManager = new FilterManager();

		// register change event procedures
		ActionOnEvent.instance().register(CandidatesChanged.class, e -> onCandidatesChanged((CandidatesChanged) e));
		ActionOnEvent.instance().register(CustomersChanged.class , e -> onCustomersChanged( (CustomersChanged)  e));
		ActionOnEvent.instance().register(ProjectsChanged.class  , e -> onProjectsChanged(  (ProjectsChanged)   e));
		ActionOnEvent.instance().register(RequestersChanged.class, e -> onRequestersChanged((RequestersChanged) e));
		ActionOnEvent.instance().register(SuppliersChanged.class , e -> onSuppliersChanged( (SuppliersChanged)  e));

		reload();

		initDetails();

		ActionOnEvent.instance().register(PublishFilterManagerRequest.class, e -> onPublishFilterManager((PublishFilterManagerRequest) e));
	}

	private void initDetails()
	{
		initDetailsCmbBxCandidate();
		initDetailsCmbBxCustomer();
		initDetailsCmbBxProject();
		initDetailsCmbBxRequester();
		initDetailsCmbBxSupplier();
		initDetailsCmbBxState();

		cmbBxWidthProperty.set(0L);
		cmbBxWidthProperty.addListener((obs, old, newValue) -> synchroniseCmbBxWidths());

		cmbBxCandidate.widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxCustomer .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxProject  .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxRequester.widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxSupplier .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxState.    widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));

		btnActivate  .setOnAction(e -> onActivateFilter(e));
		btnDeactivate.setOnAction(e -> onDeactivateFilter(e));
	}

	private void onCandidatesChanged(CandidatesChanged e)
	{
		ObservableList<Candidate> candidates = FXCollections.observableArrayList(e.getCandidates());
		SortedList<Candidate> candidatesSorted = new SortedList<>(candidates);
		candidatesSorted.comparatorProperty().set((o1, o2) -> o1.getUniqueName().compareTo(o2.getUniqueName()));
		cmbBxCandidate.setItems(candidatesSorted);
		getStringConverterCandidates().repopulate(e.getCandidates());
	}

	private void onCustomersChanged(CustomersChanged e)
	{
		ObservableList<Customer> customers = FXCollections.observableArrayList(e.getCustomers());
		SortedList<Customer> customersSorted = new SortedList<>(customers);
		customersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxCustomer.setItems(customersSorted);
		getStringConverterCustomers().repopulate(e.getCustomers());
	}

	private void onProjectsChanged(ProjectsChanged e)
	{
		ObservableList<Project> projects = FXCollections.observableArrayList(e.getProjects());
		SortedList<Project> projectsSorted = new SortedList<>(projects);
		projectsSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxProject.setItems(projectsSorted);
		getStringConverterProjects().repopulate(e.getProjects());
	}

	private void onRequestersChanged(RequestersChanged e)
	{
		ObservableList<Requester> requesters = FXCollections.observableArrayList(e.getRequesters());
		SortedList<Requester> requestersSorted = new SortedList<>(requesters);
		requestersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxRequester.setItems(requestersSorted);
		getStringConverterRequesters().repopulate(e.getRequesters());
	}

	private void onSuppliersChanged(SuppliersChanged e)
	{
		ObservableList<Supplier> suppliers = FXCollections.observableArrayList(e.getSuppliers());
		SortedList<Supplier> suppliersSorted = new SortedList<>(suppliers);
		suppliersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxSupplier.setItems(suppliersSorted);
		getStringConverterSuppliers().repopulate(e.getSuppliers());
	}

	private void onCmbBxWidthChanged(ObservableValue<? extends Number> obs, Number old, Number newValue)
	{
		if (newValue.longValue() > old.longValue()) cmbBxWidthProperty.set(newValue.longValue());
	}

	private void synchroniseCmbBxWidths()
	{
		cmbBxCandidate.setPrefWidth(cmbBxWidthProperty.get());
		cmbBxCustomer .setPrefWidth(cmbBxWidthProperty.get());
		cmbBxProject  .setPrefWidth(cmbBxWidthProperty.get());
		cmbBxRequester.setPrefWidth(cmbBxWidthProperty.get());
		cmbBxSupplier .setPrefWidth(cmbBxWidthProperty.get());
		cmbBxState    .setPrefWidth(cmbBxWidthProperty.get());
	}

	/** updates filter manager based on user settings in ui and then fires {@link PublishFilterManagerResponse} */
	private void onPublishFilterManager(PublishFilterManagerRequest e)
	{
		if (addCriterionCandidate.isSelected()) filterManager.filterByCandidate(cmbBxCandidate.getValue());
		else filterManager.filterByCandidate(null);
		if (addCriterionCustomer.isSelected()) filterManager.filterByCustomer(cmbBxCustomer.getValue());
		else filterManager.filterByCustomer(null);
		if (addCriterionProject.isSelected()) filterManager.filterByProject(cmbBxProject.getValue());
		else filterManager.filterByProject(null);
		if (addCriterionRequester.isSelected()) filterManager.filterByRequester(cmbBxRequester.getValue());
		else filterManager.filterByRequester(null);
		if (addCriterionSupplier.isSelected()) filterManager.filterBySupplier(cmbBxSupplier.getValue());
		else filterManager.filterBySupplier(null);
		if (addCriterionState.isSelected()) filterManager.filterByState(cmbBxState.getValue());
		else filterManager.filterByState(null);

		ActionOnEvent.fire(new PublishFilterManagerResponse(this, filterManager));
	}

	/** delegates to {@link #onPublishFilterManager(PublishFilterManagerRequest)} */
	private void onActivateFilter(ActionEvent e)
	{
		onPublishFilterManager(new PublishFilterManagerRequest(this));

		Optional<Stage> optional = FXUtil.getStage(btnActivate);
		if (optional.isPresent())
		{
			optional.get().close();
		}
	}

	/**
	 * updates filter manager to <b>deactivate</b> filtering regardless on what is configured by user settings in ui and
	 * then fires {@link PublishFilterManagerResponse}
	 */
	private void onDeactivateFilter(ActionEvent e)
	{
		filterManager.filterByCandidate(null);
		filterManager.filterByCustomer(null);
		filterManager.filterByProject(null);
		filterManager.filterByRequester(null);
		filterManager.filterBySupplier(null);
		filterManager.filterByState(null);

		ActionOnEvent.fire(new PublishFilterManagerResponse(this, filterManager));

		Optional<Stage> optional = FXUtil.getStage(btnDeactivate);
		if (optional.isPresent())
		{
			optional.get().close();
		}
	}

	private class AutoCompletePredicateCandidateUniqueName implements Predicate<Candidate>
	{
		@Override public boolean test(Candidate candidate)
		{
			return
					candidate
							.getUniqueName()
							.toLowerCase()
							.contains(cmbBxCandidate.getEditor().getText().toLowerCase());
		}
	}

	private CandidateFXUtils.StringConverter stringConverterCandidates;
	private CandidateFXUtils.StringConverter getStringConverterCandidates()
	{
		if (stringConverterCandidates == null)
		{
			stringConverterCandidates =
					new CandidateFXUtils.StringConverter(
							cmbBxCandidate.getItems(), new StringConverterTestCandidateUniqueName());
		}
		return stringConverterCandidates;
	}

	private void initDetailsCmbBxCandidate()
	{
		CandidateFXUtils.CellFactory cellFactory = new CandidateFXUtils.CellFactory();

		cmbBxCandidate.setButtonCell(cellFactory.call(null));
		cmbBxCandidate.setCellFactory(cellFactory);
		cmbBxCandidate.setConverter(getStringConverterCandidates());

		Predicate<Candidate> predicate = new AutoCompletePredicateCandidateUniqueName();

		new AutoCompleteComboBoxListener<>(cmbBxCandidate, predicate);
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
		CustomerFXUtils.CellFactory cellFactory = new CustomerFXUtils.CellFactory();

		cmbBxCustomer.setButtonCell(cellFactory.call(null));
		cmbBxCustomer.setCellFactory(cellFactory);
		cmbBxCustomer.setConverter(getStringConverterCustomers());

		Predicate<Customer> predicate = new AutoCompletePredicateCustomerName();

		new AutoCompleteComboBoxListener<>(cmbBxCustomer, predicate);
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
		ProjectFXUtils.CellFactory cellFactory = new ProjectFXUtils.CellFactory();

		cmbBxProject.setButtonCell(cellFactory.call(null));
		cmbBxProject.setCellFactory(cellFactory);
		cmbBxProject.setConverter(getStringConverterProjects());

		Predicate<Project> predicate = new AutoCompletePredicateProjectName();

		new AutoCompleteComboBoxListener<>(cmbBxProject, predicate);
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
		RequesterFXUtils.CellFactory cellFactory = new RequesterFXUtils.CellFactory();

		cmbBxRequester.setButtonCell(cellFactory.call(null));
		cmbBxRequester.setCellFactory(cellFactory);
		cmbBxRequester.setConverter(getStringConverterRequesters());

		Predicate<Requester> predicate = new AutoCompletePredicateRequesterName();

		new AutoCompleteComboBoxListener<>(cmbBxRequester, predicate);
	}


	private SupplierFXUtils.StringConverter stringConverterSuppliers;
	private SupplierFXUtils.StringConverter getStringConverterSuppliers()
	{
		if (stringConverterSuppliers == null)
		{
			stringConverterSuppliers =
					new SupplierFXUtils.StringConverter(
							cmbBxSupplier.getItems(), new StringConverterTestSupplierName());
		}
		return stringConverterSuppliers;
	}

	private void initDetailsCmbBxSupplier()
	{
		SupplierFXUtils.CellFactory cellFactory = new SupplierFXUtils.CellFactory();

		cmbBxSupplier.setButtonCell(cellFactory.call(null));
		cmbBxSupplier.setCellFactory(cellFactory);
		cmbBxSupplier.setConverter(getStringConverterSuppliers());

		Predicate<Supplier> predicate = new AutoCompletePredicateSupplierName();

		new AutoCompleteComboBoxListener<>(cmbBxSupplier, predicate);
	}

	private class AutoCompletePredicateSupplierName implements Predicate<Supplier>
	{
		@Override public boolean test(Supplier supplier)
		{
			return
					supplier
							.getName()
							.toLowerCase()
							.contains(cmbBxSupplier.getEditor().getText().toLowerCase());
		}
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

//		stringConverterState =
//				new StateFXUtils.StringConverter(
//						cmbBxState.getItems(), new StringConverterTestState());

		cmbBxState.setButtonCell(cellFactory.call(null));
		cmbBxState.setCellFactory(cellFactory);
		cmbBxState.setConverter(getStringConverterState());

		Predicate<State> predicate = new AutoCompletePredicateState();

		new AutoCompleteComboBoxListener<>(cmbBxState, predicate);
	}
}