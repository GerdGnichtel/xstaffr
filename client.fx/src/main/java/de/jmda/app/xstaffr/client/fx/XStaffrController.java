package de.jmda.app.xstaffr.client.fx;

import java.util.Optional;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.apache.derby.jdbc.ClientDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaff.common.domain.test.DBUtils;
import de.jmda.app.xstaffr.client.fx.edit.candidate.CandidateEditor;
import de.jmda.app.xstaffr.client.fx.edit.customer.CustomerEditor;
import de.jmda.app.xstaffr.client.fx.edit.project.ProjectEditor;
import de.jmda.app.xstaffr.client.fx.edit.requester.RequesterEditor;
import de.jmda.app.xstaffr.client.fx.edit.supplier.SupplierEditor;
import de.jmda.app.xstaffr.client.fx.main.contract.ContractEditor;
import de.jmda.app.xstaffr.client.fx.main.search.request.SearchRequestEditor;
import de.jmda.app.xstaffr.client.fx.main.search.request.SearchRequestEditorService.DisplayFilterEditorRequest;
import de.jmda.app.xstaffr.client.fx.main.search.result.SearchResultEditor;
import de.jmda.app.xstaffr.client.fx.main.search.result.SearchResultEditorService.DisplayContractsRequest;
import de.jmda.app.xstaffr.client.fx.main.search.result.SearchResultEditorService.DisplaySearchRequestsRequest;
import de.jmda.app.xstaffr.client.fx.menu.Menu;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditBackupEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditCandidatesEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditCustomersEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditProjectsEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditRequestersEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditRestoreEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.EditSuppliersEvent;
import de.jmda.app.xstaffr.client.fx.menu.MenuService.ReportSearchRequestsEvent;
import de.jmda.app.xstaffr.client.fx.report.SearchRequestReporter;
import de.jmda.app.xstaffr.client.fx.settings.filter.FilterEditor;
import de.jmda.core.cdi.event.ActionOnEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class XStaffrController implements XStaffrService
{
	private final static Logger LOGGER = LogManager.getLogger(XStaffrController.class);

	@FXML private VBox vbxRoot;

	@FXML private HBox hbxTop;

	@FXML private StackPane stckPn;
	@FXML private AnchorPane nchrPnSearchRequests;
	@FXML private AnchorPane nchrPnSearchResults;
	@FXML private AnchorPane nchrPnContracts;

	@Inject private Event<ViewServiceAvailable> eventServiceAvailable;

	private SearchRequestEditor searchRequestEditor;
	private SearchResultEditor searchResultEditor;
	private ContractEditor contractEditor;

	@FXML private void initialize()
	{
		LOGGER.debug("initialising");

		initialiseMenu();
		initialiseMainView();

		ActionOnEvent actionOnEvent = ActionOnEvent.instance();

		actionOnEvent.register(EditCandidatesEvent.class, e -> onEditCandidatesEvent());
		actionOnEvent.register(EditCustomersEvent.class, e -> onEditCustomersEvent());
		actionOnEvent.register(EditProjectsEvent.class, e -> onEditProjectsEvent());
		actionOnEvent.register(EditRequestersEvent.class, e -> onEditRequestersEvent());
		actionOnEvent.register(EditSuppliersEvent.class, e -> onEditSuppliersEvent());
		actionOnEvent.register(EditBackupEvent.class, e -> onEditBackupEvent());
		actionOnEvent.register(EditRestoreEvent.class, e -> onEditRestoreEvent());
		actionOnEvent.register(ReportSearchRequestsEvent.class, e -> onReportSearchRequestsEvent());
		actionOnEvent.register(DisplayFilterEditorRequest.class, e -> onDisplayFilterEditorRequest());
		actionOnEvent.register(de.jmda.app.xstaffr.client.fx.main.search.request.SearchRequestEditorService.DisplaySearchResultsRequest.class, e -> onDisplaySearchResultsRequest());
		actionOnEvent.register(de.jmda.app.xstaffr.client.fx.main.contract.ContractEditorService.DisplaySearchResultsRequest.class, e -> onDisplaySearchResultsRequest());
		actionOnEvent.register(DisplaySearchRequestsRequest.class, e -> onDisplaySearchRequestsRequest());
		actionOnEvent.register(DisplayContractsRequest.class, e -> onDisplayContractsRequest());

		eventServiceAvailable.fire(new ViewServiceAvailable(this, this));
	}

	private void initialiseMenu()
	{
		Menu menu = CDI.current().select(Menu.class).get();
		Parent menuRoot = menu.getLocalRoot();
		hbxTop.getChildren().add(menuRoot);
	}

	private void initialiseMainView()
	{
		searchRequestEditor = CDI.current().select(SearchRequestEditor.class).get();
		searchResultEditor = CDI.current().select(SearchResultEditor.class).get();
		contractEditor = CDI.current().select(ContractEditor.class).get();

		Parent searchRequestEditorRoot = searchRequestEditor.getLocalRoot();
		Parent searchResultEditorRoot = searchResultEditor.getLocalRoot();
		Parent contractEditorRoot = contractEditor.getLocalRoot();

		nchrPnSearchRequests.getChildren().add(searchRequestEditorRoot);
		nchrPnSearchResults.getChildren().add(searchResultEditorRoot);
		nchrPnContracts.getChildren().add(contractEditorRoot);

		HBox.setHgrow(stckPn, Priority.SOMETIMES);
		VBox.setVgrow(stckPn, Priority.SOMETIMES);
		HBox.setHgrow(searchRequestEditorRoot, Priority.SOMETIMES);
		VBox.setVgrow(searchRequestEditorRoot, Priority.SOMETIMES);
		HBox.setHgrow(searchResultEditorRoot, Priority.SOMETIMES);
		VBox.setVgrow(searchResultEditorRoot, Priority.SOMETIMES);
		HBox.setHgrow(contractEditorRoot, Priority.SOMETIMES);
		VBox.setVgrow(contractEditorRoot, Priority.SOMETIMES);

//		HBox.setHgrow(stckPn, Priority.ALWAYS);
//		VBox.setVgrow(stckPn, Priority.ALWAYS);
//		HBox.setHgrow(searchRequestEditorRoot, Priority.ALWAYS);
//		VBox.setVgrow(searchRequestEditorRoot, Priority.ALWAYS);
//		HBox.setHgrow(searchResultEditorRoot, Priority.ALWAYS);
//		VBox.setVgrow(searchResultEditorRoot, Priority.ALWAYS);
//		HBox.setHgrow(contractEditorRoot, Priority.ALWAYS);
//		VBox.setVgrow(contractEditorRoot, Priority.ALWAYS);

//		nchrPnSearchRequests.minHeightProperty().bind(stckPn.heightProperty());
//		nchrPnSearchRequests.minWidthProperty().bind(stckPn.widthProperty());
//		nchrPnSearchResults.minHeightProperty().bind(stckPn.heightProperty());
//		nchrPnSearchResults.minWidthProperty().bind(stckPn.widthProperty());
//		nchrPnContracts.minHeightProperty().bind(stckPn.heightProperty());
//		nchrPnContracts.minWidthProperty().bind(stckPn.widthProperty());

//		nchrPnSearchRequests.prefHeight(Double.MAX_VALUE);
//		nchrPnSearchRequests.prefWidth(Double.MAX_VALUE);
//		nchrPnSearchResults.prefHeight(Double.MAX_VALUE);
//		nchrPnSearchResults.prefWidth(Double.MAX_VALUE);
//		nchrPnContracts.prefHeight(Double.MAX_VALUE);
//		nchrPnContracts.prefWidth(Double.MAX_VALUE);

//		nchrPnSearchRequests.toFront();
		onDisplaySearchRequestsRequest();

//		AnchorPane.setTopAnchor(searchRequestEditorRoot, 0.0);
//		AnchorPane.setRightAnchor(searchRequestEditorRoot, 0.0);
//		AnchorPane.setBottomAnchor(searchResultEditorRoot, 0.0);
//		AnchorPane.setLeftAnchor(searchRequestEditorRoot, 0.0);
//
//		AnchorPane.setTopAnchor(searchResultEditorRoot, 0.0);
//		AnchorPane.setRightAnchor(searchResultEditorRoot, 0.0);
//		AnchorPane.setBottomAnchor(searchResultEditorRoot, 0.0);
//		AnchorPane.setLeftAnchor(searchResultEditorRoot, 0.0);
//
//		AnchorPane.setTopAnchor(contractEditorRoot, 0.0);
//		AnchorPane.setRightAnchor(contractEditorRoot, 0.0);
//		AnchorPane.setBottomAnchor(contractEditorRoot, 0.0);
//		AnchorPane.setLeftAnchor(contractEditorRoot, 0.0);
	}

	private FilterEditor filterEditor;

	private FilterEditor getFilterEditor()
	{
		if (filterEditor == null)
		{
			filterEditor = CDI.current().select(FilterEditor.class).get();
		}
		return filterEditor;
	}

	private void onEditCandidatesEvent() { CandidateEditor.show(XStaffrServiceProviderJPASE.instance().provide()); }
	private void onEditCustomersEvent() { CustomerEditor.show(XStaffrServiceProviderJPASE.instance().provide()); }
	private void onEditProjectsEvent() { ProjectEditor.show(XStaffrServiceProviderJPASE.instance().provide()); }
	private void onEditRequestersEvent() { RequesterEditor.show(XStaffrServiceProviderJPASE.instance().provide()); }
	private void onEditSuppliersEvent() { SupplierEditor.show(XStaffrServiceProviderJPASE.instance().provide()); }
	private void onDisplayFilterEditorRequest() { getFilterEditor().show(); }
	private void onDisplaySearchRequestsRequest()
	{
//		nchrPnSearchRequests.toFront();
		nchrPnSearchRequests.setVisible(true);;
		nchrPnSearchResults.setVisible(false);
		nchrPnContracts.setVisible(false);
	}
	private void onDisplaySearchResultsRequest()
	{
//		nchrPnSearchResults.toFront();
		nchrPnSearchResults.setVisible(true);
		nchrPnSearchRequests.setVisible(false);
		nchrPnContracts.setVisible(false);
	}
	private void onDisplayContractsRequest()
	{
//		nchrPnContracts.toFront();
		nchrPnContracts.setVisible(true);
		nchrPnSearchRequests.setVisible(false);
		nchrPnSearchResults.setVisible(false);
	}

	private DBUtils getDBUtils()
	{
		DBUtils result;

		try
		{
			result = new DBUtils(ClientDriver.class, "jdbc:derby://localhost:1527/db/derby/xstaffr", "xstaffr", "xstaffr");
		}
		catch (Exception e)
		{
			String msg = "failure initialising db utilities";
			LOGGER.error(msg, e);
			throw new IllegalStateException(msg, e);
		}

		return result;
	}

	private void onEditBackupEvent( )
	{
		try
		{
			getDBUtils().backup();
		}
		catch (Exception e)
		{
			String msg = "failure backing up database";
			LOGGER.error(msg, e);
			throw new IllegalStateException(msg, e);
		}
	}

	private void onEditRestoreEvent()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("restore database");
		alert.setContentText("Restoring the database will replace the current content of the database. Are you sure you want to restore?");

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.OK))
		{
			try
			{
				getDBUtils().restore();
			}
			catch (Exception e)
			{
				String msg = "failure restoring database";
				LOGGER.error(msg, e);
				alert = new Alert(AlertType.ERROR);
				alert.setContentText(msg + "\n" + e);
				alert.showAndWait();
			}
		}
	}

	private void onReportSearchRequestsEvent()
	{
		de.jmda.app.xstaffr.common.service.XStaffrService xStaffrService = XStaffrServiceProviderJPASE.instance().provide();
		SearchRequestReporter.runAndShow(xStaffrService.searchRequests());
	}

	@Override public void reloadSearchRequests()
	{
		searchRequestEditor.getService().reload();
	}
}