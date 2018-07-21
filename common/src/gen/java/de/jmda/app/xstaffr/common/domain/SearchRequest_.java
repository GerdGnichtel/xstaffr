package de.jmda.app.xstaffr.common.domain;

import de.jmda.app.xstaffr.common.domain.SearchRequest.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-04-22T09:13:40.591+0200")
@StaticMetamodel(SearchRequest.class)
public class SearchRequest_ {
	public static volatile SingularAttribute<SearchRequest, Long> id;
	public static volatile SingularAttribute<SearchRequest, Project> project;
	public static volatile SingularAttribute<SearchRequest, Requester> requester;
	public static volatile SetAttribute<SearchRequest, SearchResult> searchResults;
	public static volatile SingularAttribute<SearchRequest, LocalDate> receipt;
	public static volatile SingularAttribute<SearchRequest, LocalDate> inception;
	public static volatile SingularAttribute<SearchRequest, LocalDate> expiration;
	public static volatile SingularAttribute<SearchRequest, String> rolename;
	public static volatile SingularAttribute<SearchRequest, String> text;
	public static volatile SingularAttribute<SearchRequest, BigDecimal> hourlyRate;
	public static volatile SingularAttribute<SearchRequest, State> state;
}
