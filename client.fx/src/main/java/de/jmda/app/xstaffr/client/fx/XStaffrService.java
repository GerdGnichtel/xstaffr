package de.jmda.app.xstaffr.client.fx;

import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface XStaffrService
{
	/**
	 * Used in CDI events to indicate that {@link XStaffrService} instance is available.
	 *
	 * @see XStaffrController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<XStaffrService>
	{
		public ViewServiceAvailable(Object source, XStaffrService service) { super(source, service); }
		public XStaffrService getViewService() { return getData().get(); }
	}

	public void reloadSearchRequests();
}