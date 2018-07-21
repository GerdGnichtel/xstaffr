package de.jmda.app.xstaffr.common.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries
({
	@NamedQuery(name=SearchRequest__.QUERY_ALL_NAME, query=SearchRequest__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=SearchRequest__.QUERY_COUNT_NAME, query=SearchRequest__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=SearchRequest__.QUERY_UPDATE_DELETE_ALL_NAME, query=SearchRequest__.QUERY_UPDATE_DELETE_ALL_EXPRESSIOM),
})
public class SearchRequest implements Serializable, Comparable<SearchRequest>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	public enum State
	{
		OPEN,
		CLOSED_SOLVED,
		CLOSED_UNSOLVED,
	}

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(/*cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}*/)
	@JoinColumn(name="idProject", nullable=false)
	private Project project;

	@ManyToOne(/*cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}*/)
	@JoinColumn(name="idRequester", nullable=false)
	private Requester requester;

	@OneToMany(mappedBy = "searchRequest"/*, cascade = {CascadeType.ALL}*/)
	private Set<SearchResult> searchResults = new HashSet<>();

	@Column(nullable=false)
	private LocalDate receipt;

	@Column(nullable=false)
	private LocalDate inception;

	@Column(nullable=false)
	private LocalDate expiration;

	/** descriptive name of the requested candidate's role in the {@link #customer}'s project (#projectname) */
	@Column(nullable=false)
	private String rolename;

	@Column(length=2048) @Lob
	private String text;

	private BigDecimal hourlyRate;

	@Enumerated
	private State state;

	/** make jpa happy */
	protected SearchRequest() { }

	public SearchRequest(
			Project project,
			Requester requester,
			LocalDate dateOfRequestReceipt,
			LocalDate dateOfPeriodStart,
			LocalDate dateOfPeriodEnd,
			String rolename)
	{
		if (project == null) throw new IllegalArgumentException("disallowed null parameter for project");
		if (requester == null) throw new IllegalArgumentException("disallowed null parameter for requester");
		if (dateOfRequestReceipt == null) throw new IllegalArgumentException("disallowed null parameter for receipt");
		if (dateOfPeriodStart == null) throw new IllegalArgumentException("disallowed null parameter for inception");
		if (dateOfPeriodEnd == null) throw new IllegalArgumentException("disallowed null parameter for expiration");
		if (rolename == null) throw new IllegalArgumentException("disallowed null parameter for rolename");
		this.project = project;
		this.requester = requester;
		this.receipt = dateOfRequestReceipt;
		this.inception = dateOfPeriodStart;
		this.expiration = dateOfPeriodEnd;
		this.rolename = rolename;
		state = State.OPEN;
	}

	public Long getId() { return id; }
	public Project getProject() { return project; }
	public void setProject(Project project) { this.project = project; }
	public Requester getRequester() { return requester; }
	public void setRequester(Requester requester) { this.requester = requester; }
	public LocalDate getReceipt() { return receipt; }
	public void setReceipt(LocalDate dateOfRequestReceipt) { this.receipt = dateOfRequestReceipt; }
	public LocalDate getInception() { return inception; }
	public void setInception(LocalDate dateOfPeriodStart) { this.inception = dateOfPeriodStart; }
	public LocalDate getExpiration() { return expiration; }
	public void setExpiration(LocalDate dateOfPeriodEnd) { this.expiration = dateOfPeriodEnd; }
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	public String getRolename() { return rolename; }
	public void setRolename(String rolename) { this.rolename = rolename; }
	public BigDecimal getHourlyRate() { return hourlyRate; }
	public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
	public Set<SearchResult> getSearchResults() { return searchResults; }
	public void setSearchResults(Set<SearchResult> searchResults) { this.searchResults = searchResults; }
	public State getState() { return state; }
	public void setState(State state) { this.state = state; }

	public String asString()
	{
		return getClass().getSimpleName() + " [" + asStringSimpleFields() + "]";
	}

	public String asStringDepthOne()
	{
		return "Request [[" + asStringSimpleFields() + "], [" + asStringNonSimpleFields() + "]]";
	}

	@Override public int compareTo(SearchRequest other)
	{
		return getReceipt().compareTo(other.getReceipt());
	}

	private String asStringSimpleFields()
	{
		return
				  getClass().getSimpleName()
				+ " ["
				+      "id=" + id
				+    ", receipt=" + receipt
				+    ", rolename=" + rolename
				+    ", hourlyRate =" + hourlyRate
				+  "]";
	}

	private String asStringNonSimpleFields()
	{
		return "roles [" + String.join(", ", resultsAsStrings()) + "]";
	}

	private Iterable<String> resultsAsStrings()
	{
		Collection<String> result = new ArrayList<>();

		getSearchResults().forEach(searchResult -> result.add(searchResult.asString()));

		return result;
	}
}