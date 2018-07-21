package de.jmda.app.xstaffr.common.domain;

public class SearchRequest__ extends SearchRequest_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "SearchRequest";

	public final static String FIELD_PROJECT = "project";
	public final static String FIELD_REQUESTER = "requester";
	public final static String FIELD_SEARCH_RESULTS = "searchResults";
	public final static String FIELD_ROLENAME = "rolename";
	public final static String FIELD_HOURLY_RATE = "hourlyRate";
	public final static String FIELD_DATE_OF_REQUEST_RECEIPT = "dateOfRequestReceipt";
	public final static String FIELD_DATE_OF_PERIOD_START = "dateOfPeriodStart";
	public final static String FIELD_DATE_OF_PERIOD_END = "dateOfPeriodEnd";
	public final static String FIELD_TEXT = "text";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select r from " + MODEL_TYPE_SIMPLE_NAME + " r";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(r) from " + MODEL_TYPE_SIMPLE_NAME + " r";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSIOM = "delete from " + MODEL_TYPE_SIMPLE_NAME + " r";
}