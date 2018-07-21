package de.jmda.app.xstaffr.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries
({
	@NamedQuery(name=Project__.QUERY_ALL_NAME, query=Project__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=Project__.QUERY_COUNT_NAME, query=Project__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=Project__.QUERY_UPDATE_DELETE_ALL_NAME, query=Project__.QUERY_UPDATE_DELETE_ALL_EXPRESSIOM),
})
public class Project implements Serializable, Comparable<Project>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(/*optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}*/)
	@JoinColumn(name="idCustomer", nullable=false)
	private Customer customer;

	@OneToMany(mappedBy = "project"/*, cascade = {CascadeType.ALL}*/)
	private Set<SearchRequest> searchRequests = new HashSet<>();

	@Column(unique=true, nullable=false)
	private String name;

	/** make jpa happy */
	protected Project() { }

	public Project(Customer customer, String name)
	{
		if (customer == null) throw new IllegalArgumentException("disallowed null parameter for customer");
		if (name == null) throw new IllegalArgumentException("disallowed null parameter for name");
		this.customer = customer;
		this.name = name;
	}

	public Long getId() { return id; }
	public Customer getCustomer() { return customer; }
//	public void setCustomer(Customer customer) { this.customer = customer; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Set<SearchRequest> getSearchRequests() { return searchRequests; }
//	public void setSearchRequests(Set<SearchRequest> searchRequests) { this.searchRequests = searchRequests; }

	public String asString()
	{
		return getClass().getSimpleName() + " [" + asStringSimpleFields() + "]";
	}

	public String asStringDepthOne()
	{
		return "Customer [[" + asStringSimpleFields() + "], [" + asStringNonSimpleFields() + "]]";
	}

	@Override public int compareTo(Project other)
	{
		return getName().compareTo(other.getName());
	}

	private String asStringSimpleFields()
	{
		return
				  getClass().getSimpleName()
				+ " ["
				+      "id=" + id
				+    ", name=" + name
				+  "]";
	}

	private String asStringNonSimpleFields()
	{
		return "roles [" + String.join(", ", resultsAsStrings()) + "]";
	}

	private Iterable<String> resultsAsStrings()
	{
		Collection<String> result = new ArrayList<>();

		getSearchRequests().forEach(item -> result.add(item.asString()));

		return result;
	}
}