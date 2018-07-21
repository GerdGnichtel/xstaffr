package de.jmda.app.xstaffr.client.fx.menu;

import de.jmda.core.cdi.event.ActionOnEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuController implements MenuService
{
	@FXML private MenuBar menubar;

	@FXML private javafx.scene.control.Menu mnEdit;
	@FXML private MenuItem mnItmEditCandidates;
	@FXML private MenuItem mnItmEditCustomers;
	@FXML private MenuItem mnItmEditRequesters;
	@FXML private MenuItem mnItmEditSuppliers;
	@FXML private MenuItem mnItmEditProjects;
	@FXML private MenuItem mnItmEditBackup;
	@FXML private MenuItem mnItmEditRestore;
	@FXML private MenuItem mnItmExit;

	@FXML private javafx.scene.control.Menu mnReport;
	@FXML private MenuItem mnItemReportSearchRequests;

	@FXML private void initialize()
	{
		mnItmEditCandidates.setOnAction(e -> onEditCandidates());
		mnItmEditCustomers.setOnAction(e -> onEditCustomers());
		mnItmEditProjects.setOnAction(e -> onEditProjects());
		mnItmEditRequesters.setOnAction(e -> onEditRequesters());
		mnItmEditSuppliers.setOnAction(e -> onEditSuppliers());
		mnItmEditBackup.setOnAction(e -> onEditBackup());
		mnItmEditRestore.setOnAction(e -> onEditRestore());
		mnItemReportSearchRequests.setOnAction(e -> onReportSearchRequests());
		mnItmExit.setOnAction(e -> onExit());
	}

	private void onEditCandidates() { ActionOnEvent.fire(new EditCandidatesEvent(mnItmEditCandidates)); }
	private void onEditCustomers() { ActionOnEvent.fire(new EditCustomersEvent(mnItmEditCustomers)); }
	private void onEditProjects() { ActionOnEvent.fire(new EditProjectsEvent(mnItmEditRequesters)); }
	private void onEditRequesters() { ActionOnEvent.fire(new EditRequestersEvent(mnItmEditRequesters)); }
	private void onEditSuppliers() { ActionOnEvent.fire(new EditSuppliersEvent(mnItmEditSuppliers)); }
	private void onEditBackup() { ActionOnEvent.fire(new EditBackupEvent(mnItmEditBackup)); }
	private void onEditRestore() { ActionOnEvent.fire(new EditRestoreEvent(mnItmEditRestore)); }

	private void onReportSearchRequests() { ActionOnEvent.fire(new ReportSearchRequestsEvent(mnItemReportSearchRequests)); }

	private void onExit() { ActionOnEvent.fire(new ExitEvent(mnItmExit)); }
}