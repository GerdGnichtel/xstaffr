package de.jmda.app.xstaffr.common.domain;

public class Customer__ extends Customer_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "Customer";

	public final static String FIELD_NAME = "name";
	public final static String FIELD_PROJECTS = "projects";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select c from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(c) from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSIOM = "delete from " + MODEL_TYPE_SIMPLE_NAME + " c";

	public final static String QUERY_BY_NAME_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.by.name";
	public final static String QUERY_BY_NAME_EXPRESSION = "select c from " + MODEL_TYPE_SIMPLE_NAME + " c where c." + FIELD_NAME + " = :" + FIELD_NAME;

	//	public final static String GRAPH_PROJECTS_NAME = MODEL_TYPE_SIMPLE_NAME + ".graph." + Project__.MODEL_TYPE_SIMPLE_NAME;
	public final static String GRAPH_PROJECTS_NAME = MODEL_TYPE_SIMPLE_NAME + ".graph.Project";
}