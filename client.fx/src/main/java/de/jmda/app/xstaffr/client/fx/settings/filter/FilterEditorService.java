package de.jmda.app.xstaffr.client.fx.settings.filter;

import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface FilterEditorService
{
	void reload();

	public class FilterChanged extends AbstractEvent<FilterManager>
	{
		public FilterChanged(Object source, FilterManager data) { super(source, data); }
		public FilterManager getFilterManager() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link FilterEditorService} instance is available.
	 *
	 * @see FilterEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<FilterEditorService>
	{
		public ViewServiceAvailable(Object source, FilterEditorService service) { super(source, service); }
		public FilterEditorService getViewService() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that the publication of a {@link FilterManager} is requested by
	 * <code>source</code>. Publication is expected to occur via firing a {@link
	 * PublishFilterManagerResponse} event.
	 */
	public class PublishFilterManagerRequest extends AbstractEvent<Object>
	{
		public PublishFilterManagerRequest(Object source) { super(source); }
	}

	/** Used in CDI events to publish a {@code FilterManager}. */
	public class PublishFilterManagerResponse extends AbstractEvent<FilterManager>
	{
		public PublishFilterManagerResponse(Object source, FilterManager filterManager) { super(source, filterManager); }
		public FilterManager getFilterManager() { return getData().get(); }
	}
}