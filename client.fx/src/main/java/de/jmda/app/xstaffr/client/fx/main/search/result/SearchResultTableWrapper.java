package de.jmda.app.xstaffr.client.fx.main.search.result;

import java.math.BigDecimal;
import java.time.LocalDate;

import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.Supplier;

public class SearchResultTableWrapper
{
	private SearchResult searchResult;

	public SearchResultTableWrapper(SearchResult searchRequest)
	{
		this.searchResult = searchRequest;
	}

	public SearchResult getSearchResult() { return searchResult; }
	public SearchRequest getSearchRequest() { return searchResult.getSearchRequest(); }
	public Supplier getSupplier() { return searchResult.getSupplier(); }
	public String getDate()
	{
		LocalDate date = searchResult.getReceipt();
		return date == null ? "" : date.toString();
	}
	public String getCandidateUniqueName() { return searchResult.getCandidate().getUniqueName(); }
	public String getHourlyRate()
	{
		BigDecimal hourlyRate = searchResult.getHourlyRate();
		return hourlyRate == null ? "" : hourlyRate.toString();
	}
	public String getRatingInternal() { return searchResult.getRatingInternal(); }
	public String getRatingByCustomer() { return searchResult.getRatingByCustomer(); }
	public String getForwardToRequester()
	{
		LocalDate date = searchResult.getForwardToRequester();
		return date == null ? "" : date.toString();
	}
	public String getForwardToCustomer()
	{
		LocalDate date = searchResult.getForwardToCustomer();
		return date == null ? "" : date.toString();
	}
	public String getFeedbackFromCusomer()
	{
		LocalDate date = searchResult.getFeedbackFromCustomer();
		return date == null ? "" : date.toString();
	}
	public String getFeedbackToSupplier()
	{
		LocalDate date = searchResult.getFeedbackToSupplier();
		return date == null ? "" : date.toString();
	}
}