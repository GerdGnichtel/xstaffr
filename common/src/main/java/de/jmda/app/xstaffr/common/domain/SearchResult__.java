package de.jmda.app.xstaffr.common.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import de.jmda.app.xstaffr.common.service.jpa.XStaffrServiceJPACommon.GraphType;

public class SearchResult__ extends SearchResult_
{
	public final static String MODEL_TYPE_SIMPLE_NAME = "SearchResult";

	public final static String FIELD_SEARCH_REQUEST = "searchRequest";
	public final static String FIELD_SUPPLIER = "supplier";
	public final static String FIELD_CANDIDATES = "candidates";
	public final static String FIELD_CONTRACTS = "contracts";
	public final static String FIELD_DATE = "date";
	public final static String FIELD_HOURLY_RATE = "hourlyRate";
	public final static String FIELD_RATING_INTERNAL = "ratingInternal";
	public final static String FIELD_RATING_BY_CUSTOMER = "ratingByCustomer";
	public final static String FIELD_TEXT = "text";
	public final static String FIELD_FORWARD_TO_REQUESTER = "forwardToRequester";
	public final static String FIELD_FORWARD_TO_CUSTOMER = "forwardToCustomer";
	public final static String FIELD_FEEDBACK_FROM_CUSTOMER = "feedbackFromCustomer";
	public final static String FIELD_FEEDBACK_TO_SUPPLIER = "feedbackToSupplier";

	public final static String QUERY_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.all";
	public final static String QUERY_ALL_EXPRESSION = "select s from " + MODEL_TYPE_SIMPLE_NAME + " s";

	public final static String QUERY_COUNT_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.count";
	public final static String QUERY_COUNT_EXPRESSION = "select count(s) from " + MODEL_TYPE_SIMPLE_NAME + " s";

	public final static String QUERY_UPDATE_DELETE_ALL_NAME = MODEL_TYPE_SIMPLE_NAME + ".query.update.delete.all";
	public final static String QUERY_UPDATE_DELETE_ALL_EXPRESSIOM = "delete from " + MODEL_TYPE_SIMPLE_NAME + " s";

	public final static String GRAPH_WITH_CONTRACTS = MODEL_TYPE_SIMPLE_NAME + ".graph.with.contracts";
	public final static Map<String, Object> getHintWithContracts(EntityManager entityManager, GraphType graphType)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(graphType.toString(), entityManager.getEntityGraph(GRAPH_WITH_CONTRACTS));
		return result;
	}
}