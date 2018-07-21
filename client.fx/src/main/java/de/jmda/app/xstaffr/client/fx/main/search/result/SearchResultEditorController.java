package de.jmda.app.xstaffr.client.fx.main.search.result;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.client.fx.edit.candidate.CandidateEditorService.CandidatesChanged;
import de.jmda.app.xstaffr.client.fx.edit.supplier.SupplierEditorService.SuppliersChanged;
import de.jmda.app.xstaffr.client.fx.main.search.request.SearchRequestEditorService.SearchRequestSelectedChanged;
import de.jmda.app.xstaffr.client.fx.util.CandidateFXUtils;
import de.jmda.app.xstaffr.client.fx.util.CandidateFXUtils.StringConverterTestCandidateUniqueName;
import de.jmda.app.xstaffr.client.fx.util.SupplierFXUtils;
import de.jmda.app.xstaffr.client.fx.util.SupplierFXUtils.StringConverterTestSupplierName;
import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.core.cdi.event.ActionOnEvent;
import de.jmda.fx.FXUtil;
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

public class SearchResultEditorController implements SearchResultEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(SearchResultEditorController.class);

	@FXML private TitledPane ttldPnTable;
	@FXML private TableView<SearchResultTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
	@FXML private TitledPane ttldPnDetails;
	@FXML private TextField txtFldRequestCustomerName;
	@FXML private TextField txtFldRequestProjectName;
	@FXML private TextField txtFldRequestRequesterName;
	@FXML private TextField txtFldRequestDate;
	@FXML private TextField txtFldRequestRoleName;
	@FXML private TextField txtFldRequestHourlyRate;
	@FXML private DatePicker dtPckrDate;
	@FXML private ComboBox<Supplier> cmbBxSupplier;
	@FXML private ComboBox<Candidate> cmbBxCandidate;
	@FXML private TextField txtFldHourlyRate;
	@FXML private TextArea txtAreaRatingInternal;
	@FXML private TextArea txtAreaRatingByCustomer;
	@FXML private TextArea txtAreaText;
	@FXML private DatePicker dtPckrForwardToRequester;
	@FXML private DatePicker dtPckrForwardToCustomer;
	@FXML private DatePicker dtPckrFeedbackFromCustomer;
	@FXML private DatePicker dtPckrFeedbackToSupplier;
	@FXML private Button btnEdit;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;
	@FXML private Button btnDirectory;
	@FXML private Button btnRequests;

	private ObjectProperty<Long> cmbBxWidthProperty = new SimpleObjectProperty<>();

	private XStaffrService xStaffrService;
	private ObservableList<SearchResultTableWrapper> tableWrappers;
	private FilteredList<SearchResultTableWrapper> tableWrappersFiltered;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<SearchResult> selected = new SimpleObjectProperty<>();
	private AutoCompleteComboBoxListener<Supplier> autoCompleteComboBoxListenerSupplier;
	private AutoCompleteComboBoxListener<Candidate> autoCompleteComboBoxListenerCandidate;
	private ObjectProperty<SearchRequest> searchRequest = new SimpleObjectProperty<>();

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

		ActionOnEvent.instance().register(SearchRequestSelectedChanged.class, e -> onSearchRequestSelectedChanged((SearchRequestSelectedChanged) e));
		ActionOnEvent.instance().register(SuppliersChanged.class, e -> onSuppliersChanged((SuppliersChanged) e));
		ActionOnEvent.instance().register(CandidatesChanged.class, e -> onCandidatesChanged((CandidatesChanged) e));

		// trigger updates in ui
		ActionOnEvent.fire(new SuppliersChanged(this, xStaffrService.suppliers()));
		ActionOnEvent.fire(new CandidatesChanged(this, xStaffrService.candidates()));
	}

	private void initTable()
	{
		TableColumn<SearchResultTableWrapper, Boolean> tcContracts = new TableColumn<>();
		Function<SearchResultTableWrapper, Boolean> functionForTCContracts =
				p ->
				{
					ActionOnEvent.fire(new DisplayContractsRequest(SearchResultEditorController.this, p.getSearchResult()));
					return p != null;
				};
		ActionButtonTableCell<SearchResultTableWrapper> actionButtonTableCellForTCContracts =
				new ActionButtonTableCell<>(">", "display contracts", functionForTCContracts);
		actionButtonTableCellForTCContracts.setButtonFontSize(8.0);
		tcContracts.setCellValueFactory(actionButtonTableCellForTCContracts.cellValueFactory());
		tcContracts.setCellFactory(actionButtonTableCellForTCContracts.cellFactory());

//		TableColumn<SearchResultTableWrapper, String> tcCustomerName = new TableColumn<>("customer");
//		tcCustomerName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchResult().getSearchRequest().getProject().getCustomer().getName()));
//
//		TableColumn<SearchResultTableWrapper, String> tcProjectName = new TableColumn<>("project");
//		tcProjectName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchResult().getSearchRequest().getProject().getName()));
//
//		TableColumn<SearchResultTableWrapper, String> tcRequesterName = new TableColumn<>("requester");
//		tcRequesterName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSearchRequest().getRequester().getName()));

		TableColumn<SearchResultTableWrapper, String> tcSupplierName = new TableColumn<>("supplier");
		tcSupplierName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSupplier().getName()));

		TableColumn<SearchResultTableWrapper, String> tcDate = new TableColumn<>("date");
		tcDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDate()));

		TableColumn<SearchResultTableWrapper, String> tcCandidate = new TableColumn<>("candidate");
		tcCandidate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getCandidateUniqueName()));

		TableColumn<SearchResultTableWrapper, String> tcHourlyRate = new TableColumn<>("hourly rate");
		tcHourlyRate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getHourlyRate()));

//		TableColumn<SearchResultTableWrapper, String> tcRatingInternal = new TableColumn<>("internal rating");
//		tcRatingInternal.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getRatingInternal()));
//
//		TableColumn<SearchResultTableWrapper, String> tcRatingByCustomer = new TableColumn<>("rating by customer");
//		tcRatingByCustomer.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getRatingByCustomer()));
//
//		TableColumn<SearchResultTableWrapper, String> tcText = new TableColumn<>("text");
//		tcText.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getText()));

		TableColumn<SearchResultTableWrapper, String> tcForwardToRequester = new TableColumn<>("forward to requester");
		tcForwardToRequester.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getForwardToRequester()));

		TableColumn<SearchResultTableWrapper, String> tcForwardToCustomer = new TableColumn<>("forward to customer");
		tcForwardToCustomer.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getForwardToCustomer()));

		TableColumn<SearchResultTableWrapper, String> tcFeedbackFromCustomer = new TableColumn<>("feedback from customer");
		tcFeedbackFromCustomer.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFeedbackFromCusomer()));

		TableColumn<SearchResultTableWrapper, String> tcFeedbackToSupplier = new TableColumn<>("feedback to supplier");
		tcFeedbackToSupplier.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFeedbackToSupplier()));

		tblVw.getColumns().add(tcContracts);
//		tblVw.getColumns().add(tcCustomerName);
//		tblVw.getColumns().add(tcProjectName);
//		tblVw.getColumns().add(tcRequesterName);
		tblVw.getColumns().add(tcDate);
		tblVw.getColumns().add(tcCandidate);
		tblVw.getColumns().add(tcHourlyRate);
//		tblVw.getColumns().add(tcRatingInternal);
//		tblVw.getColumns().add(tcRatingByCustomer);
//		tblVw.getColumns().add(tcText);
		tblVw.getColumns().add(tcForwardToRequester);
		tblVw.getColumns().add(tcForwardToCustomer);
		tblVw.getColumns().add(tcFeedbackFromCustomer);
		tblVw.getColumns().add(tcFeedbackToSupplier);

		tblVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblVw
				.getSelectionModel()
				.selectedItemProperty().addListener((obs, old, newValue) -> onTableSelectionChanged(newValue));

		// set text in row to bold if result has contracts
		tblVw.setRowFactory
		(
				tv ->
				new TableRow<SearchResultTableWrapper>()
				{
					@Override public void updateItem(SearchResultTableWrapper item, boolean empty)
					{
						super.updateItem(item, empty);
						if (item == null)
						{
							setStyle("");
						}
						else if (item.getSearchResult().getContracts().size() > 0)
						{
							setStyle("-fx-font-weight: bold;");
						}
						else
						{
							setStyle("");
						}
					}
				}
		);
	}

	private void initTableControlBar() { btnRemove.setOnAction(e -> onBtnRemove()); }

	private void initDetails()
	{
		initDetailsCmbBxSupplier();
		initDetailsCmbBxCandidate();

		cmbBxWidthProperty.set(0L);
		cmbBxWidthProperty.addListener((obs, old, newValue) -> synchroniseCmbBxWidths());

		cmbBxCandidate .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));
		cmbBxSupplier  .widthProperty().addListener((obs, old, newValue) -> onCmbBxWidthChanged(obs, old, newValue));

		btnEdit.setOnAction(e -> onBtnEdit());
		btnUpdate.setOnAction(e -> onBtnUpdate());
		btnNew.setOnAction(e -> onBtnNew());
		btnCreate.setOnAction(e -> onBtnCreate());
		btnCancel.setOnAction(e -> onBtnCancel());
		btnDirectory.setOnAction(e -> onBtnDirectory());
		btnRequests.setOnAction(e -> onBtnRequests());
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
		ObservableList<Supplier> suppliers = FXCollections.observableArrayList(xStaffrService.suppliers());
		SortedList<Supplier> suppliersSorted = new SortedList<>(suppliers);
		suppliersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		cmbBxSupplier.setItems(suppliersSorted);

		SupplierFXUtils.CellFactory cellFactory = new SupplierFXUtils.CellFactory();

//		stringConverterSuppliers =
//				new SupplierFXUtils.StringConverter(
//						cmbBxSupplier.getItems(), new StringConverterTestSupplierName());

		cmbBxSupplier.setButtonCell(cellFactory.call(null));
		cmbBxSupplier.setCellFactory(cellFactory);
		cmbBxSupplier.setConverter(getStringConverterSuppliers());

		Predicate<Supplier> predicate = new AutoCompletePredicateSupplierName();
		autoCompleteComboBoxListenerSupplier = new AutoCompleteComboBoxListener<>(cmbBxSupplier, predicate);
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
		ObservableList<Candidate> candidates = FXCollections.observableArrayList(xStaffrService.candidates());
		SortedList<Candidate> candidatesSorted = new SortedList<>(candidates);
		candidatesSorted.comparatorProperty().set((o1, o2) -> o1.getUniqueName().compareTo(o2.getUniqueName()));
		cmbBxCandidate.setItems(candidatesSorted);

		CandidateFXUtils.CellFactory cellFactory = new CandidateFXUtils.CellFactory();

//		stringConverterCandidates =
//				new CandidateFXUtils.StringConverter(
//						cmbBxCandidate.getItems(), new StringConverterTestCandidateUniqueName());

		cmbBxCandidate.setButtonCell(cellFactory.call(null));
		cmbBxCandidate.setCellFactory(cellFactory);
		cmbBxCandidate.setConverter(getStringConverterCandidates());

		Predicate<Candidate> predicate = new AutoCompletePredicateCandidateUniqueName();
		autoCompleteComboBoxListenerCandidate = new AutoCompleteComboBoxListener<>(cmbBxCandidate, predicate);
		cmbBxCandidate.valueProperty().addListener
		(
				(obs, old, newValue) ->
				{
					if (newValue == null) LOGGER.debug("value: null");
					else LOGGER.debug("value: " + newValue.getUniqueName());
				}
		);
	}

	private Set<SearchResultTableWrapper> toTableWrappers(Set<SearchResult> searchResults)
	{
		Set<SearchResultTableWrapper> result = new HashSet<>();
		searchResults.forEach(searchResult -> result.add(new SearchResultTableWrapper(searchResult)));
		return result;
	}

	private Set<SearchResult> tableWrappersToSet()
	{
		Set<SearchResult> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getSearchResult()));
		return result;
	}

	private SearchResultTableWrapper findSearchResultTableWrapperFor(SearchResult searchResult)
	{
		SearchResultTableWrapper result = null;
		for (SearchResultTableWrapper tableWrapper : tableWrappers)
		{
			if (tableWrapper.getSearchResult() == searchResult)
			{
				result = tableWrapper;
				break;
			}
		}
		return result;
	}

	@Override public void reload()
	{
		SearchRequest searchRequestLocal = searchRequest.getValue();
		if (searchRequestLocal == null) return;
		updateTableFrom(searchRequestLocal.getSearchResults());
	}

	private void onTableSelectionChanged(SearchResultTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getSearchResult());
		}
		btnEdit.setDisable(selected.get() == null);
	}

	private void onSelectedChanged(SearchResult newValue)
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
		ActionOnEvent.fire(new SearchResultSelectedChanged(this, newValue));
	}

	private void onModeChanged(Mode newValue)
	{
		if (newValue == Mode.SELECT)
		{
			setDetailsReadOnly(true);

			tblVw.setDisable(false);
			hbx.setDisable(false); // enable remove button

			btnEdit.setDisable(selected.get() == null);
			btnUpdate.setDisable(true);
			btnNew.setDisable(false);
			btnCreate.setDisable(true);
			btnCancel.setDisable(true);
			btnDirectory.setDisable(false);
		}
		else if (newValue == Mode.EDIT)
		{
			repopulateComboBoxes();
			setDetailsReadOnly(false);

			tblVw.setDisable(true);
			hbx.setDisable(true); // disable remove button

			dtPckrDate.requestFocus();
			cmbBxSupplier.setValue(selected.get().getSupplier());
			cmbBxCandidate.setValue(selected.get().getCandidate());

			btnEdit.setDisable(true);
			btnUpdate.setDisable(false);
			btnNew.setDisable(true);
			btnCreate.setDisable(true);
			btnCancel.setDisable(false);
			btnDirectory.setDisable(true);
		}
		else if (newValue == Mode.NEW)
		{
			clearDetails();
			repopulateComboBoxes();
			setDetailsReadOnly(false);

			tblVw.setDisable(true);
			hbx.setDisable(true); // disable remove button

			cmbBxSupplier.requestFocus();

			btnEdit.setDisable(true);
			btnUpdate.setDisable(true);
			btnNew.setDisable(true);
			btnCreate.setDisable(false);
			btnCancel.setDisable(false);
			btnDirectory.setDisable(true);
		}
	}

	private void onSearchRequestSelectedChanged(SearchRequestSelectedChanged e)
	{
		if (e.getData().isPresent() == false) return;

		SearchRequest searchRequestLocal = e.getData().get();

		searchRequest.set(searchRequestLocal);

		txtFldRequestCustomerName.setText(searchRequestLocal.getProject().getCustomer().getName());
		txtFldRequestDate.setText(searchRequestLocal.getReceipt().toString());
		txtFldRequestHourlyRate.setText(searchRequestLocal.getHourlyRate().toString());
		txtFldRequestProjectName.setText(searchRequestLocal.getProject().getName());
		txtFldRequestRequesterName.setText(searchRequestLocal.getRequester().getName());
		txtFldRequestRoleName.setText(searchRequestLocal.getRolename());

		updateTableFrom(searchRequestLocal.getSearchResults());

		clearDetails();
	}

	private void onSuppliersChanged(SuppliersChanged e)
	{
		ObservableList<Supplier> suppliers = FXCollections.observableArrayList(e.getSuppliers());
		SortedList<Supplier> suppliersSorted = new SortedList<>(suppliers);
		suppliersSorted.comparatorProperty().set((o1, o2) -> o1.getName().compareTo(o2.getName()));
		autoCompleteComboBoxListenerSupplier.repopulate(suppliersSorted);
		stringConverterSuppliers.repopulate(e.getSuppliers());
	}

	private void onCandidatesChanged(CandidatesChanged e)
	{
		ObservableList<Candidate> candidates = FXCollections.observableArrayList(e.getCandidates());
		SortedList<Candidate> candidatesSorted = new SortedList<>(candidates);
		Comparator<Candidate> comparator = (o1, o2) ->
		{
			int lastNameComparison = o1.getLastName().compareTo(o2.getLastName());
			if (lastNameComparison == 0)
			{
				return o1.getFirstName().compareTo(o2.getFirstName());
			}
			return lastNameComparison;
		};
		candidatesSorted.comparatorProperty().set(comparator);
		autoCompleteComboBoxListenerCandidate.repopulate(candidatesSorted);
		stringConverterCandidates.repopulate(e.getCandidates());
	}

	private void onBtnRemove()
	{
		SearchResultTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove search result in db
		xStaffrService.deleteSearchResult(selectedItem.getSearchResult());

		tableWrappers.remove(selectedItem);
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		SearchResult searchResult = selected.get();

		if (searchResult == null) return;

		searchResult.setReceipt(dtPckrDate.getValue());
		searchResult.setSupplier(cmbBxSupplier.getValue());
		searchResult.setCandidate(cmbBxCandidate.getValue());
		searchResult.setHourlyRate(new BigDecimal(txtFldHourlyRate.getText()));
		searchResult.setRatingInternal(txtAreaRatingInternal.getText());
		searchResult.setRatingByCustomer(txtAreaRatingByCustomer.getText());
		searchResult.setText(txtAreaText.getText());
		searchResult.setForwardToRequester(dtPckrForwardToRequester.getValue());
		searchResult.setForwardToCustomer(dtPckrForwardToCustomer.getValue());
		searchResult.setFeedbackFromCustomer(dtPckrFeedbackFromCustomer.getValue());
		searchResult.setFeedbackToSupplier(dtPckrFeedbackToSupplier.getValue());

		// update search result in db
		xStaffrService.updateSearchResult(searchResult);

		// update search result in table
		tblVw.refresh();

		ActionOnEvent.fire(new SearchResultsChanged(this, tableWrappersToSet()));

		mode.set(Mode.SELECT);

		// force unselect and select of table row to trigger update of details
		tblVw.getSelectionModel().select(-1); // force unselect

		SearchResultTableWrapper searchResultTableWrapper = findSearchResultTableWrapperFor(searchResult);

		// force select
		if (searchResultTableWrapper != null)
		{
			tblVw.getSelectionModel().select(searchResultTableWrapper);
		}
		else
		{
			tblVw.getSelectionModel().select(0);
		}
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		SearchResult searchResult =
				new SearchResult(
						searchRequest.get(),
						cmbBxCandidate.getValue(),
						cmbBxSupplier.getValue(),
						dtPckrDate.getValue(),
						new BigDecimal(txtFldHourlyRate.getText()));

		searchResult.setRatingInternal(txtAreaRatingInternal.getText());
		searchResult.setRatingByCustomer(txtAreaRatingByCustomer.getText());
		searchResult.setText(txtAreaText.getText());
		searchResult.setForwardToRequester(dtPckrForwardToRequester.getValue());
		searchResult.setForwardToCustomer(dtPckrForwardToCustomer.getValue());
		searchResult.setFeedbackFromCustomer(dtPckrFeedbackFromCustomer.getValue());
		searchResult.setFeedbackToSupplier(dtPckrFeedbackToSupplier.getValue());

		xStaffrService.persistSearchResult(searchResult);

		SearchResultTableWrapper tableWrapper = new SearchResultTableWrapper(searchResult);

		tableWrappers.add(tableWrapper);

		mode.set(Mode.SELECT);

		// force select of table row to trigger update of details
		tblVw.getSelectionModel().select(tableWrapper);
	}

	private void onBtnCancel()
	{
		updateDetails(selected.get());
		setDetailsReadOnly(true);
		tblVw.setDisable(false);

		mode.set(Mode.SELECT);
	}

	private void onBtnDirectory()
	{
		SearchResult searchResultLocal = selected.get();
		SearchRequest searchRequestLocal = searchResultLocal.getSearchRequest();
		Project projectLocal = searchRequestLocal.getProject();
		String path =
				  "x:/xstaffr/doc"
				+ "/" + projectLocal.getCustomer().getId()
				+ "/" + projectLocal.getId()
				+ "/" + searchRequestLocal.getId()
				+ "/" + searchResultLocal.getId()
				;

		createUpdateDirectoriesForResult(searchResultLocal, searchRequestLocal, projectLocal);

		// create and open directory
		File file = new File(path);
		if (file.exists() == false) file.mkdirs();
		FXUtil.getHostServices().showDocument(path);
	}

	private void onBtnRequests()
	{
		ActionOnEvent.fire(new DisplaySearchRequestsRequest(SearchResultEditorController.this, selected.get()));
	}

	private void onCmbBxWidthChanged(ObservableValue<? extends Number> obs, Number old, Number newValue)
	{
		if (newValue.longValue() > old.longValue()) cmbBxWidthProperty.set(newValue.longValue());
	}

	private void synchroniseCmbBxWidths()
	{
		cmbBxCandidate.setPrefWidth(cmbBxWidthProperty.get());
		cmbBxSupplier .setPrefWidth(cmbBxWidthProperty.get());
	}

	private void createUpdateDirectoriesForResult(
			SearchResult searchResultLocal, SearchRequest searchRequestLocal, Project projectLocal)
	{
		// create path to directories and readmes
		File customerDirectory = new File("x:/xstaffr/doc/" + projectLocal.getCustomer().getId());
		try
		{
			FileUtils.write(
					new File(customerDirectory.getAbsolutePath() + "/readme.txt"),
					"customer id " + projectLocal.getCustomer().getId() + "(" + projectLocal.getCustomer().getName() + ")",
					StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			LOGGER.debug("failure writing readme.txt to customer directory " + customerDirectory.getAbsolutePath(), e);
		}
		File projectDirectory = new File(customerDirectory.getAbsolutePath() + "/" + projectLocal.getId());
		try
		{
			FileUtils.write(
					new File(projectDirectory.getAbsolutePath() + "/readme.txt"),
					  "customer id " + projectLocal.getCustomer().getId() + " (" + projectLocal.getCustomer().getName() + ")\n"
					+ "project id " + projectLocal.getId() + " (" + projectLocal.getName() + ")",
					StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			LOGGER.debug("failure writing readme.txt to project directory " + projectDirectory.getAbsolutePath(), e);
		}
		File searchRequestDirectory = new File(projectDirectory.getAbsolutePath() + "/" + searchRequestLocal.getId());
		try
		{
			FileUtils.write(
					new File(searchRequestDirectory.getAbsolutePath() + "/readme.txt"),
					  "customer id " + projectLocal.getCustomer().getId() + " (" + projectLocal.getCustomer().getName() + ")\n"
					+ "project id " + projectLocal.getId() + " (" + projectLocal.getName() + ")\n"
					+ "request id " + searchRequestLocal.getId() + " (" + searchRequestLocal.getRolename() + ")",
					StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			LOGGER.debug("failure writing readme.txt to search request directory " + searchRequestDirectory.getAbsolutePath(), e);
		}
		File searchResultDirectory = new File(searchRequestDirectory.getAbsolutePath() + "/" + searchResultLocal.getId());
		try
		{
			FileUtils.write(
					new File(searchResultDirectory.getAbsolutePath() + "/readme.txt"),
					  "customer id " + projectLocal.getCustomer().getId() + " (" + projectLocal.getCustomer().getName() + ")\n"
					+ "project id " + projectLocal.getId() + " (" + projectLocal.getName() + ")\n"
					+ "request id " + searchRequestLocal.getId() + " (" + searchRequestLocal.getRolename() + ")\n"
					+ "result id " + searchResultLocal.getId() + " (" + searchResultLocal.getCandidate().getUniqueName() + ")",
					StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			LOGGER.debug("failure writing readme.txt to search result directory " + searchResultDirectory.getAbsolutePath(), e);
		}
	}

	private void clearDetails()
	{
		dtPckrDate.setValue(null);
		cmbBxSupplier.setValue(null);
		cmbBxCandidate.setValue(null);
		txtFldHourlyRate.setText("");
		txtAreaRatingInternal.setText("");
		txtAreaRatingByCustomer.setText("");
		txtAreaText.setText("");
		dtPckrForwardToRequester.setValue(null);
		dtPckrForwardToCustomer.setValue(null);
		dtPckrFeedbackFromCustomer.setValue(null);
		dtPckrFeedbackToSupplier.setValue(null);
	}

	private void repopulateComboBoxes()
	{
		// trigger onXXXChangedHandlers to repopulate combo boxes
		onSuppliersChanged(new SuppliersChanged( this, xStaffrService.suppliers()));
		onCandidatesChanged(new CandidatesChanged( this, xStaffrService.candidates()));
	}

	private void setDetailsReadOnly(boolean flag)
	{
		dtPckrDate.setEditable(!flag);
		cmbBxSupplier.setEditable(!flag);
		cmbBxSupplier.setDisable(flag);
		cmbBxCandidate.setEditable(!flag);
		cmbBxCandidate.setDisable(flag);
		txtFldHourlyRate.setEditable(!flag);
		txtAreaRatingInternal.setEditable(!flag);
		txtAreaRatingByCustomer.setEditable(!flag);
		txtAreaText.setEditable(!flag);
		dtPckrForwardToRequester.setEditable(!flag);
		dtPckrForwardToCustomer.setEditable(!flag);
		dtPckrFeedbackFromCustomer.setEditable(!flag);
		dtPckrFeedbackToSupplier.setEditable(!flag);
	}

//	private void disableDetails(boolean flag)
//	{
//		dtPckrDate.setDisable(flag);
//		cmbBxSupplier.setDisable(flag);
//		cmbBxCandidate.setDisable(flag);
//		txtFldHourlyRate.setDisable(flag);
//		txtAreaRatingInternal.setDisable(flag);
//		txtAreaRatingByCustomer.setDisable(flag);
//		txtAreaText.setDisable(flag);
//		dtPckrForwardToRequester.setDisable(flag);
//		dtPckrForwardToCustomer.setDisable(flag);
//		dtPckrFeedbackFromCustomer.setDisable(flag);
//		dtPckrFeedbackToSupplier.setDisable(flag);
//	}

//	@Override public void updateFrom(SearchRequest searchRequest)
//	{
//		Set<SearchResult> searchResults = searchRequest.getSearchResults();
//		updateFrom(searchResults);
//	}

	private void updateTableFrom(Set<SearchResult> searchResults)
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(searchResults));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		SortedList<SearchResultTableWrapper> sortedList = new SortedList<>(tableWrappersFiltered);
		// sort the list in the same way the table is sorted (click on headers)
		sortedList.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(sortedList);
	}

	private void updateDetails(SearchResult searchResult)
	{
		if (searchResult == null) return;

		dtPckrDate.setValue(searchResult.getReceipt());
		cmbBxSupplier.setValue(searchResult.getSupplier());
		cmbBxCandidate.setValue(searchResult.getCandidate());
		txtFldHourlyRate.setText(searchResult.getHourlyRate().toString());
		txtAreaRatingInternal.setText(searchResult.getRatingInternal());
		txtAreaRatingByCustomer.setText(searchResult.getRatingByCustomer());
		txtAreaText.setText(searchResult.getText());
		dtPckrForwardToRequester.setValue(searchResult.getForwardToRequester());
		dtPckrForwardToCustomer.setValue(searchResult.getForwardToCustomer());
		dtPckrFeedbackFromCustomer.setValue(searchResult.getFeedbackFromCustomer());
		dtPckrFeedbackToSupplier.setValue(searchResult.getFeedbackToSupplier());
	}
}