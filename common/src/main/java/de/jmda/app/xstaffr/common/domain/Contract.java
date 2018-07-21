package de.jmda.app.xstaffr.common.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries
({
	@NamedQuery(name=Contract__.QUERY_ALL_NAME, query=Contract__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=Contract__.QUERY_COUNT_NAME, query=Contract__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=Contract__.QUERY_UPDATE_DELETE_ALL_NAME, query=Contract__.QUERY_UPDATE_DELETE_ALL_EXPRESSIOM),
})
public class Contract implements Serializable, Comparable<Contract>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne//(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="idSearchResult", nullable=false)
	private SearchResult searchResult;

	@Column(nullable=false)
	private LocalDate inception; // Vertragsbeginn

	@Column(nullable=false)
	private LocalDate expiration; // Vertragsende

	@Column(nullable=false)
	private BigDecimal hourlyRate;

	@Column(length=512) @Lob
	private String text;

	public Long getId() { return id; }
	public SearchResult getSearchResult() { return searchResult; }
	public void setSearchResult(SearchResult searchResult) { this.searchResult = searchResult; }
	public LocalDate getInception() { return inception; }
	public void setInception(LocalDate inception) { this.inception = inception; }
	public LocalDate getExpiration() { return expiration; }
	public void setExpiration(LocalDate expiration) { this.expiration = expiration; }
	public BigDecimal getHourlyRate() { return hourlyRate; }
	public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	/** make JPA happy */
	protected Contract() { }

	public Contract(SearchResult searchResult, LocalDate inception, LocalDate expiration, BigDecimal hourlyRate)
	{
		if (searchResult == null) throw new IllegalArgumentException("disallowed null parameter for searchResult");
		if (inception == null) throw new IllegalArgumentException("disallowed null parameter for inception");
		if (expiration == null) throw new IllegalArgumentException("disallowed null parameter for end");
		if (hourlyRate == null) throw new IllegalArgumentException("disallowed null parameter for hourlyRate");
		this.searchResult = searchResult;
		this.inception = inception;
		this.expiration = expiration;
		this.hourlyRate = hourlyRate;
	}

	@Override public int compareTo(Contract other)
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