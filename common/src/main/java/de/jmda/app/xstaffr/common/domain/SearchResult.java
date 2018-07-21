package de.jmda.app.xstaffr.common.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries
({
	@NamedQuery(name=SearchResult__.QUERY_ALL_NAME, query=SearchResult__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=SearchResult__.QUERY_COUNT_NAME, query=SearchResult__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=SearchResult__.QUERY_UPDATE_DELETE_ALL_NAME, query=SearchResult__.QUERY_UPDATE_DELETE_ALL_EXPRESSIOM),
})
@NamedEntityGraphs
({
	@NamedEntityGraph(name=SearchResult__.GRAPH_WITH_CONTRACTS, attributeNodes = { @NamedAttributeNode(value=SearchResult__.FIELD_CONTRACTS)})
})
public class SearchResult implements Serializable, Comparable<SearchResult>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne//(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="idSearchRequest", foreignKey=@ForeignKey(name="fk" + "SearchRequest"), nullable=false)
	private SearchRequest searchRequest;

	@ManyToOne//(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="idSupplier", foreignKey=@ForeignKey(name="fk" + "Supplier"), nullable=false)
	private Supplier supplier;

	@ManyToOne//(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="idCandidate", foreignKey=@ForeignKey(name="fk" + "candidate"), nullable=false)
	private Candidate candidate;

	@OneToMany(mappedBy = "searchResult")
	private Set<Contract> contracts = new HashSet<>();

	private LocalDate receipt;

	private BigDecimal hourlyRate;

	@Column(length=1024) @Lob
	private String ratingInternal;

	@Column(length=1024) @Lob
	private String ratingByCustomer;

	@Column(length=1024) @Lob
	private String text;

	private LocalDate forwardToRequester;

	private LocalDate forwardToCustomer;

	private LocalDate feedbackFromCustomer;

	private LocalDate feedbackToSupplier;

	public Long getId() { return id; }
	public SearchRequest getSearchRequest() { return searchRequest; }
	public void setSearchRequest(SearchRequest searchRequest) { this.searchRequest = searchRequest; }
	public Supplier getSupplier() { return supplier; }
	public void setSupplier(Supplier supplier) { this.supplier = supplier; }
	public LocalDate getReceipt() { return receipt; }
	public void setReceipt(LocalDate date) { this.receipt = date; }
	public BigDecimal getHourlyRate() { return hourlyRate; }
	public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
	public String getRatingInternal() { return ratingInternal; }
	public void setRatingInternal(String ratingInternal) { this.ratingInternal = ratingInternal; }
	public String getRatingByCustomer() { return ratingByCustomer; }
	public void setRatingByCustomer(String ratingByCustomer) { this.ratingByCustomer = ratingByCustomer; }
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	public LocalDate getForwardToRequester() { return forwardToRequester; }
	public void setForwardToRequester(LocalDate forwardToRequester) { this.forwardToRequester = forwardToRequester; }
	public LocalDate getForwardToCustomer() { return forwardToCustomer; }
	public void setForwardToCustomer(LocalDate forwardToCustomer) { this.forwardToCustomer = forwardToCustomer; }
	public LocalDate getFeedbackFromCustomer() { return feedbackFromCustomer; }
	public void setFeedbackFromCustomer(LocalDate feedbackFromCustomer) { this.feedbackFromCustomer = feedbackFromCustomer; }
	public LocalDate getFeedbackToSupplier() { return feedbackToSupplier; }
	public void setFeedbackToSupplier(LocalDate feedbackToSupplier) { this.feedbackToSupplier = feedbackToSupplier; }
	public Candidate getCandidate() { return candidate; }
	public void setCandidate(Candidate candidate) { this.candidate = candidate; }
	public Set<Contract> getContracts() { return contracts; }

	/** make JPA happy */
	protected SearchResult() { }

	public SearchResult(SearchRequest searchRequest, Candidate candidate, Supplier supplier, LocalDate date, BigDecimal hourlyRate)
	{
		if (searchRequest == null) throw new IllegalArgumentException("disallowed null parameter for searchRequest");
		if (candidate == null) throw new IllegalArgumentException("disallowed null parameter for candidate");
		if (supplier == null) throw new IllegalArgumentException("disallowed null parameter for supplier");
		if (date == null) throw new IllegalArgumentException("disallowed null parameter for receipt");
		if (hourlyRate == null) throw new IllegalArgumentException("disallowed null parameter for hourlyRate");
		this.searchRequest = searchRequest;
		this.candidate = candidate;
		this.supplier = supplier;
		this.receipt = date;
		this.hourlyRate = hourlyRate;
	}
	@Override public int compareTo(SearchResult other)
	{
		return getId().compareTo(other.getId());
	}

	public String asString()
	{
		return getClass().getSimpleName() + " [" + asStringSimpleFields() + "]";
	}

	private String asStringSimpleFields()
	{
		return
				getClass().getSimpleName() + " [" + "id=" + id + "]";
	}
}