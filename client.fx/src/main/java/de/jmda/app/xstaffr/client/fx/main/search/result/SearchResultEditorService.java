package de.jmda.app.xstaffr.client.fx.main.search.result;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface SearchResultEditorService
{
	void reload();
//	void updateFrom(SearchRequest searchRequest);

	public class SearchResultChanged extends AbstractEvent<SearchResult>
	{
		public SearchResultChanged(Object source, SearchResult data) { super(source, data); }
	}

	public class SearchResultSelectedChanged extends AbstractEvent<SearchResult>
	{
		public SearchResultSelectedChanged(Object source, SearchResult data) { super(source, data); }
		public SearchResult getSearchResult() { return getData().get(); }
	}

	public class SearchResultsChanged extends AbstractEvent<Set<SearchResult>>
	{
		public SearchResultsChanged(Object source, Set<SearchResult> data) { super(source, data); }
	}

	public class DisplaySearchRequestsRequest extends AbstractEvent<SearchResult>
	{
		public DisplaySearchRequestsRequest(Object source, SearchResult data) { super(source, data); }
		public SearchResult getSearchResult() { return getData().get(); }
	}

	public class DisplayContractsRequest extends AbstractEvent<SearchResult>
	{
		public DisplayContractsRequest(Object source, SearchResult data) { super(source, data); }
		public SearchResult getSearchResult() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link MainEditorService} instance is available.
	 *
	 * @see MainEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<SearchResultEditorService>
	{
		public ViewServiceAvailable(Object source, SearchResultEditorService service) { super(source, service); }
		public SearchResultEditorService getViewService() { return getData().get(); }
	}
}