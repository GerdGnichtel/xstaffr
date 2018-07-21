package de.jmda.app.xstaffr.client.fx.menu;

import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface MenuService
{
	public class EditCandidatesEvent extends AbstractEvent<Boolean> { public EditCandidatesEvent(Object source) { super(source); } }
	public class EditCustomersEvent extends AbstractEvent<Object> { public EditCustomersEvent(Object source) { super(source); } }
	public class EditProjectsEvent extends AbstractEvent<Object> { public EditProjectsEvent(Object source) { super(source); } }
	public class EditRequestersEvent extends AbstractEvent<Object> { public EditRequestersEvent(Object source) { super(source); } }
	public class EditSuppliersEvent extends AbstractEvent<Object> { public EditSuppliersEvent(Object source) { super(source); } }
	public class EditBackupEvent extends AbstractEvent<Object> { public EditBackupEvent(Object source) { super(source); } }
	public class EditRestoreEvent extends AbstractEvent<Object> { public EditRestoreEvent(Object source) { super(source); } }
	public class ReportSearchRequestsEvent extends AbstractEvent<Object> { public ReportSearchRequestsEvent(Object source) { super(source); } }
	public class ExitEvent extends AbstractEvent<Object> { public ExitEvent(Object source) { super(source); } }
}