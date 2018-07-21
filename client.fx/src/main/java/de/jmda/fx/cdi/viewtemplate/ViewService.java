package de.jmda.fx.cdi.viewtemplate;

import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface ViewService
{
	/**
	 * Used in CDI events to indicate that {@link ViewService} instance is available.
	 *
	 * @see ViewController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<ViewService>
	{
		public ViewServiceAvailable(Object source, ViewService service) { super(source, service); }
		public ViewService getViewService() { return getData().get(); }
	}
}