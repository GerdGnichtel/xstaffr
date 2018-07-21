package de.jmda.app.xstaffr.client.fx.edit.requester;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface RequesterEditorService
{
	void reload();

	public class RequestersChanged extends AbstractEvent<Set<Requester>>
	{
		public RequestersChanged(Object source, Set<Requester> data) { super(source, data); }
		public Set<Requester> getRequesters() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link RequesterEditorService} instance is available.
	 *
	 * @see RequesterEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<RequesterEditorService>
	{
		public ViewServiceAvailable(Object source, RequesterEditorService service) { super(source, service); }
		public RequesterEditorService getViewService() { return getData().get(); }
	}
}