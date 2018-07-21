package de.jmda.app.xstaffr.common.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-04-14T16:47:12.270+0200")
@StaticMetamodel(SearchResult.class)
public class SearchResult_ {
	public static volatile SingularAttribute<SearchResult, Long> id;
	public static volatile SingularAttribute<SearchResult, SearchRequest> searchRequest;
	public static volatile SingularAttribute<SearchResult, Supplier> supplier;
	public static volatile SingularAttribute<SearchResult, Candidate> candidate;
	public static volatile SetAttribute<SearchResult, Contract> contracts;
	public static volatile SingularAttribute<SearchResult, LocalDate> receipt;
	public static volatile SingularAttribute<SearchResult, BigDecimal> hourlyRate;
	public static volatile SingularAttribute<SearchResult, String> ratingInternal;
	public static volatile SingularAttribute<SearchResult, String> ratingByCustomer;
	public static volatile SingularAttribute<SearchResult, String> text;
	public static volatile SingularAttribute<SearchResult, LocalDate> forwardToRequester;
	public static volatile SingularAttribute<SearchResult, LocalDate> forwardToCustomer;
	public static volatile SingularAttribute<SearchResult, LocalDate> feedbackFromCustomer;
	public static volatile SingularAttribute<SearchResult, LocalDate> feedbackToSupplier;
}
