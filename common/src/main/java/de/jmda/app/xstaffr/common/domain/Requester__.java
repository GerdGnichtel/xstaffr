package de.jmda.app.xstaffr.common.domain;

public class Requester__ extends Requester_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "Requester";

	public final static String FIELD_NAME = "name";
	public final static String FIELD_SEARCH_REQUESTS = "searchRequests";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select r from " + MODEL_TYPE_SIMPLE_NAME + " r";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(r) from " + MODEL_TYPE_SIMPLE_NAME + " r";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSIOM = "delete from " + MODEL_TYPE_SIMPLE_NAME + " r";

	public final static String QUERY_BY_NAME_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.by.name";
	public final static String QUERY_BY_NAME_EXPRESSION = "select r from " + MODEL_TYPE_SIMPLE_NAME + " r where r." + FIELD_NAME + " = :" + FIELD_NAME;
}