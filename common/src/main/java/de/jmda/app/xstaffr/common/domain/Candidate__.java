package de.jmda.app.xstaffr.common.domain;

public class Candidate__ extends Candidate_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "Candidate";

	public final static String FIELD_LAST_NAME = "lastName";
	public final static String FIELD_FIRST_NAME = "firstName";
	public final static String FIELD_UNIQUE_NAME = "uniqueName";
	public final static String FIELD_TEXT = "text";
	public final static String FIELD_SEARCH_RESULTS = "searchResults";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select c from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(c) from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSION = "delete from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_BY_LAST_NAME_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.by.lastName";
	public final static String QUERY_BY_LAST_NAME_EXPRESSION = "select c from " + MODEL_TYPE_SIMPLE_NAME + " c where c." + FIELD_LAST_NAME + " = :" + FIELD_LAST_NAME;
}