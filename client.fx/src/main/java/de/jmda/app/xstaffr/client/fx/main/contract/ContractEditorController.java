package de.jmda.app.xstaffr.client.fx.main.contract;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.client.fx.main.search.result.SearchResultEditorService.SearchResultSelectedChanged;
import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.app.xstaffr.common.domain.SearchResult;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class ContractEditorController implements ContractEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(ContractEditorController.class);

	@FXML private TitledPane ttldPnTable;
	@FXML private TextField txtFldSearchResultCandidateUniqueName;
	@FXML private TextField txtFldSearchResultSupplierName;
	@FXML private TextField txtFldSearchResultHourlyRate;
	@FXML private TableView<ContractTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
	@FXML private TitledPane ttldPnDetails;
	@FXML private DatePicker dtPckrInception;
	@FXML private DatePicker dtPckrExpiration;
	@FXML private TextField txtFldHourlyRate;
	@FXML private TextArea txtAreaText;
	@FXML private Button btnEdit;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;
	@FXML private Button btnResults;

	private XStaffrService xStaffrService;
	private ObservableList<ContractTableWrapper> tableWrappers;
	private FilteredList<ContractTableWrapper> tableWrappersFiltered;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<Contract> selected = new SimpleObjectProperty<>();
	private ObjectProperty<SearchResult> searchResult = new SimpleObjectProperty<>();

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

		ActionOnEvent.instance().register(SearchResultSelectedChanged.class, e -> onSearchResultChanged((SearchResultSelectedChanged) e));
	}

	private void initTable()
	{
		TableColumn<ContractTableWrapper, String> tcInception = new TableColumn<>("inception");
		tcInception.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getInception().toString()));

		TableColumn<ContractTableWrapper, String> tcExpiration = new TableColumn<>("expiration");
		tcExpiration.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getExpiration().toString()));

		TableColumn<ContractTableWrapper, String> tcHourlyRate = new TableColumn<>("hourly rate");
		tcHourlyRate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getHourlyRate().toString()));

		TableColumn<ContractTableWrapper, String> tcText = new TableColumn<>("text");
		tcText.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getText()));

		tblVw.getColumns().add(tcInception);
		tblVw.getColumns().add(tcExpiration);
		tblVw.getColumns().add(tcHourlyRate);
		tblVw.getColumns().add(tcText);

		tblVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblVw
				.getSelectionModel()
				.selectedItemProperty().addListener((obs, old, newValue) -> onTableSelectionChanged(newValue));
	}

	private void initTableControlBar() { btnRemove.setOnAction(e -> onBtnRemove()); }

	private void initDetails()
	{
		btnEdit.setOnAction(e -> onBtnEdit());
		btnUpdate.setOnAction(e -> onBtnUpdate());
		btnNew.setOnAction(e -> onBtnNew());
		btnCreate.setOnAction(e -> onBtnCreate());
		btnCancel.setOnAction(e -> onBtnCancel());
		btnResults.setOnAction(e -> onBtnResults());
	}

	private Set<ContractTableWrapper> toTableWrappers(Set<Contract> contracts)
	{
		Set<ContractTableWrapper> result = new HashSet<>();
		contracts.forEach(customer -> result.add(new ContractTableWrapper(customer)));
		return result;
	}

	private Set<Contract> tableWrappersToSet()
	{
		Set<Contract> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getContract()));
		return result;
	}

	@Override public void reload()
	{
		SearchResult searchResultLocal = searchResult.getValue();
		if (searchResultLocal == null) return;
		updateTableFrom(searchResultLocal.getContracts());
	}

	private void onTableSelectionChanged(ContractTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getContract());
		}
		btnEdit.setDisable(selected.get() == null);
	}

	private void onSelectedChanged(Contract newValue)
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
			hbx.setDisable(false); // enable remove button

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
			hbx.setDisable(true); // disable remove button

			dtPckrInception.requestFocus();

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
			hbx.setDisable(true); // disable remove button

			dtPckrInception.requestFocus();

			btnEdit.setDisable(true);
			btnUpdate.setDisable(true);
			btnNew.setDisable(true);
			btnCreate.setDisable(false);
			btnCancel.setDisable(false);
		}
	}

	private void onSearchResultChanged(SearchResultSelectedChanged e)
	{
		if (e.getData().isPresent() == false) return;

		SearchResult searchResultLocal = e.getData().get();

		searchResult.set(searchResultLocal);

		txtFldSearchResultCandidateUniqueName.setText(searchResultLocal.getCandidate().getUniqueName());
		txtFldSearchResultSupplierName.setText(searchResultLocal.getSupplier().getName());
		txtFldSearchResultHourlyRate.setText(searchResultLocal.getHourlyRate().toString());

		updateTableFrom(searchResultLocal.getContracts());

		clearDetails();
	}

	private void onBtnRemove()
	{
		ContractTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove contract in db
		xStaffrService.deleteContract(selectedItem.getContract());

		tableWrappers.remove(selectedItem);
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		Contract contract = selected.get();

		if (contract == null) return;

		contract.setInception(dtPckrInception.getValue());
		contract.setExpiration(dtPckrExpiration.getValue());
		contract.setHourlyRate(new BigDecimal(txtFldHourlyRate.getText()));
		contract.setText(txtAreaText.getText());

		// update contract in db
		xStaffrService.updateContract(contract);

		// update project in table
		tblVw.refresh();

		mode.set(Mode.SELECT);

		ActionOnEvent.fire(new ContractsChanged(this, tableWrappersToSet()));
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		Contract contract =
				new Contract(
						searchResult.get(),
						dtPckrInception.getValue(),
						dtPckrExpiration.getValue(),
						new BigDecimal(txtFldHourlyRate.getText()));

		contract.setText(txtAreaText.getText());

		xStaffrService.persistContract(contract);

		ContractTableWrapper tableWrapper = new ContractTableWrapper(contract);

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

	private void onBtnResults()
	{
		ActionOnEvent.fire(new DisplaySearchResultsRequest(ContractEditorController.this, selected.get()));
	}

	private void clearDetails()
	{
		dtPckrInception.setValue(null);
		dtPckrExpiration.setValue(null);
		txtFldHourlyRate.setText("");
		txtAreaText.setText("");
	}

	private void setDetailsReadOnly(boolean flag)
	{
		dtPckrInception.setEditable(!flag);
		dtPckrExpiration.setEditable(!flag);
		txtFldHourlyRate.setEditable(!flag);
		txtAreaText.setEditable(!flag);
	}

	private void updateTableFrom(Set<Contract> contracts)
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(contracts));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		SortedList<ContractTableWrapper> sortedList = new SortedList<>(tableWrappersFiltered);
		// sort the list in the same way the table is sorted (click on headers)
		sortedList.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(sortedList);
	}

	private void updateDetails(Contract newValue)
	{
		dtPckrInception.setValue(newValue.getInception());
		dtPckrExpiration.setValue(newValue.getExpiration());
		txtFldHourlyRate.setText(newValue.getHourlyRate().toString());
		txtAreaText.setText(newValue.getText());
	}
}