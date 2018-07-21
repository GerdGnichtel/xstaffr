package de.jmda.app.xstaffr.client.fx.main.search.request;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface SearchRequestEditorService
{
	void reload();

	public class SearchRequestsChanged extends AbstractEvent<Set<SearchRequest>>
	{
		public SearchRequestsChanged(Object source, Set<SearchRequest> data) { super(source, data); }
		public Set<SearchRequest> getSearchRequest() { return getData().get(); }
	}

	public class SearchRequestSelectedChanged extends AbstractEvent<SearchRequest>
	{
		public SearchRequestSelectedChanged(Object source, SearchRequest data) { super(source, data); }
		public SearchRequest getSearchRequest() { return getData().get(); }
	}

	public class DisplaySearchResultsRequest extends AbstractEvent<SearchRequest>
	{
		public DisplaySearchResultsRequest(Object source, SearchRequest data) { super(source, data); }
		public SearchRequest getSearchRequest() { return getData().get(); }
	}

	public class DisplayFilterEditorRequest extends AbstractEvent<Object>
	{
		public DisplayFilterEditorRequest(Object source) { super(source); }
	}

	public class ViewServiceAvailable extends AbstractEvent<SearchRequestEditorService>
	{
		public ViewServiceAvailable(Object source, SearchRequestEditorService service) { super(source, service); }
		public SearchRequestEditorService getViewService() { return getData().get(); }
	}
}