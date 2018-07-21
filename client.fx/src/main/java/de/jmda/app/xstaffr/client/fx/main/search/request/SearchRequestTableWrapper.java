package de.jmda.app.xstaffr.client.fx.main.search.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import de.jmda.app.xstaffr.common.domain.SearchRequest;

public final class SearchRequestTableWrapper
{
	private SearchRequest searchRequest;

	public SearchRequestTableWrapper(SearchRequest searchRequest)
	{
		this.searchRequest = searchRequest;
	}

	public SearchRequest getSearchRequest() { return searchRequest; }
	public String getDateOfRequestReceipt()
	{
		LocalDate date = searchRequest.getReceipt();
		return date == null ? "" : date.toString();
	}
	public String getDateOfOfPeriodStart()
	{
		LocalDate date = searchRequest.getInception();
		return date == null ? "" : date.toString();
	}
	public String getDateOfOfPeriodEnd()
	{
		LocalDate date = searchRequest.getExpiration();
		return date == null ? "" : date.toString();
	}
	public String getRoleName() { return searchRequest.getRolename(); }
	public String getHourlyRate()
	{
		BigDecimal hourlyRate = searchRequest.getHourlyRate();
		return hourlyRate == null ? "" : hourlyRate.toString();
	}
}