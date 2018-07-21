package de.jmda.app.xstaffr.client.fx.edit.candidate;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.client.fx.XStaffrServiceProviderJPASE;
import de.jmda.app.xstaffr.common.domain.Candidate;
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

public class CandidateEditorController implements CandidateEditorService
{
	private enum Mode { SELECT, EDIT, NEW };

	private final static Logger LOGGER = LogManager.getLogger(CandidateEditorController.class);

	@FXML private TableView<CandidateTableWrapper> tblVw;
	@FXML private HBox hbx;
	@FXML private Button btnRemove;
	@FXML private TextField txtFldFilter;
	@FXML private TitledPane ttldPnDetails;
	@FXML private TextField txtFldFirstName;
	@FXML private TextField txtFldLastName;
	@FXML private TextField txtFldUniqueName;
	@FXML private TextArea txtAreaNotes;
	@FXML private Button btnEdit;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private Button btnCreate;
	@FXML private Button btnCancel;

	private XStaffrService xStaffrService;
	private ObservableList<CandidateTableWrapper> tableWrappers;
	private FilteredList<CandidateTableWrapper> tableWrappersFiltered;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<Candidate> selected = new SimpleObjectProperty<>();

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
		TableColumn<CandidateTableWrapper, String> tcLastName = new TableColumn<>("last name");
		tcLastName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getLastName()));

		TableColumn<CandidateTableWrapper, String> tcFirstName = new TableColumn<>("first name");
		tcFirstName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFirstName()));

		TableColumn<CandidateTableWrapper, String> tcUniqueName = new TableColumn<>("unique name");
		tcUniqueName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getUniqueName()));

		tblVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblVw
				.getSelectionModel()
				.selectedItemProperty().addListener((obs, old, newValue) -> onTableSelectionChanged(newValue));

		tblVw.getColumns().add(tcLastName);
		tblVw.getColumns().add(tcFirstName);
		tblVw.getColumns().add(tcUniqueName);
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

	private Set<CandidateTableWrapper> toTableWrappers(Set<Candidate> candidates)
	{
		Set<CandidateTableWrapper> result = new HashSet<>();
		candidates.forEach(candidate -> result.add(new CandidateTableWrapper(candidate)));
		return result;
	}

	private Set<Candidate> tableWrappersToSet()
	{
		Set<Candidate> result = new HashSet<>();
		tableWrappers.forEach(tableWrapper -> result.add(tableWrapper.getCandidate()));
		return result;
	}

	@Override public void reload()
	{
		tableWrappers = FXCollections.observableArrayList(toTableWrappers(xStaffrService.candidates()));
		// wrap observable list in a filtered list, display all initially
		tableWrappersFiltered = new FilteredList<>(tableWrappers, p -> true);
		// wrap filtered list in a sorted list
		SortedList<CandidateTableWrapper> sortedList = new SortedList<>(tableWrappersFiltered);
		// sort the list in the same way the table is sorted (click on headers)
		sortedList.comparatorProperty().bind(tblVw.comparatorProperty());
		tblVw.setItems(sortedList);
	}

	private void onTableSelectionChanged(CandidateTableWrapper newValue)
	{
		if (newValue == null)
		{
			selected.set(null);
		}
		else
		{
			selected.set(newValue.getCandidate());
		}
		btnEdit.setDisable(selected.get() == null);
	}

	private void onSelectedChanged(Candidate newValue)
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

			txtFldFirstName.requestFocus();

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

			txtFldFirstName.requestFocus();

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
		CandidateTableWrapper selectedItem = tblVw.getSelectionModel().getSelectedItem();

		if (selectedItem == null) return;

		// remove candidate in db
		xStaffrService.deleteCandidate(selectedItem.getCandidate());

		tableWrappers.remove(selectedItem);

		ActionOnEvent.fire(new CandidatesChanged(this, tableWrappersToSet()));
	}

	private void onBtnEdit() { mode.set(Mode.EDIT); }

	private void onBtnUpdate()
	{
		Candidate candidate = selected.get();

		if (candidate == null) return;

		candidate.setFirstName(txtFldFirstName.getText());
		candidate.setLastName(txtFldLastName.getText());
		candidate.setUniqueName(txtFldUniqueName.getText());
		candidate.setText(txtAreaNotes.getText());

		// update candidate in db
		xStaffrService.updateCandidate(candidate);

		// update candidate in table
		tblVw.refresh();

		ActionOnEvent.fire(new CandidatesChanged(this, tableWrappersToSet()));

		mode.set(Mode.SELECT);
	}

	private void onBtnNew() { mode.set(Mode.NEW); }

	private void onBtnCreate()
	{
		Candidate candidate = new Candidate(txtFldLastName.getText(), txtFldFirstName.getText(), txtFldUniqueName.getText());
		candidate.setText(txtAreaNotes.getText());

		xStaffrService.persistCandidate(candidate);

		CandidateTableWrapper candidateTableWrapper = new CandidateTableWrapper(candidate);

		tableWrappers.add(candidateTableWrapper);

		ActionOnEvent.fire(new CandidatesChanged(this, tableWrappersToSet()));

		tblVw.getSelectionModel().select(candidateTableWrapper);

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
		txtFldFirstName.setText("");
		txtFldLastName.setText("");
		txtFldUniqueName.setText("");
		txtAreaNotes.setText("");
	}

	private void setDetailsReadOnly(boolean flag)
	{
		txtFldFirstName.setEditable(!flag);
		txtFldLastName.setEditable(!flag);
		txtFldUniqueName.setEditable(!flag);
		txtAreaNotes.setEditable(!flag);
	}

	private void updateDetails(Candidate newValue)
	{
		txtFldFirstName.setText(newValue.getFirstName());
		txtFldLastName.setText(newValue.getLastName());
		txtFldUniqueName.setText(newValue.getUniqueName());
		txtAreaNotes.setText(newValue.getText());
	}

	private boolean applyFilterFor(CandidateTableWrapper candidateTableWrapper, String filterExpression)
	{
		// if filterExpression is empty all candidates match
		if (filterExpression == null || filterExpression.isEmpty()) return true;

		String lowerCaseFilterExpression = filterExpression.toLowerCase();

		// check first name
		if (candidateTableWrapper.getCandidate().getFirstName().toLowerCase().contains(lowerCaseFilterExpression)) return true;
		// check last name
		else if (candidateTableWrapper.getCandidate().getLastName().toLowerCase().contains(lowerCaseFilterExpression)) return true;

		// candidate does not match
		return false;
	}
}	