package de.jmda.app.xstaffr.common.domain;

public class Contract__ extends Contract_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "Contract";

	public final static String FIELD_START = "start";
	public final static String FIELD_END = "end";
	public final static String FIELD_HOURLY_RATE = "hourlyRate";
	public final static String FIELD_TEXT = "text";
	public final static String FIELD_SEARCH_RESULT = "searchResult";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select c from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(c) from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSIOM = "delete from " + MODEL_TYPE_SIMPLE_NAME + " c";
}