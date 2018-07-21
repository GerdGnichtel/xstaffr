package de.jmda.app.xstaffr.common.domain;

public class Project__ extends Project_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "Project";

	public final static String FIELD_NAME = "name";
	public final static String FIELD_CUSTOMER = "customer";
	public final static String FIELD_SEARCH_REQUESTS = "searchRequests";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select p from " + MODEL_TYPE_SIMPLE_NAME + " p";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(p) from " + MODEL_TYPE_SIMPLE_NAME + " p";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSIOM = "delete from " + MODEL_TYPE_SIMPLE_NAME + " p";
}