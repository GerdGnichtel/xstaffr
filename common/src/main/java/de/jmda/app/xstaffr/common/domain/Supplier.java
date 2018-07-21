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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/** company or person that supplied {@link Candidate} in {@link SearchResult} */
@Entity
@NamedQueries
({
	@NamedQuery(name=Supplier__.QUERY_ALL_NAME, query=Supplier__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=Supplier__.QUERY_COUNT_NAME, query=Supplier__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=Supplier__.QUERY_UPDATE_DELETE_ALL_NAME, query=Supplier__.QUERY_UPDATE_DELETE_ALL_EXPRESSIOM),
	@NamedQuery(name=Supplier__.QUERY_BY_NAME_NAME, query=Supplier__.QUERY_BY_NAME_EXPRESSION)
})
public class Supplier implements Serializable, Comparable<Supplier>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy="supplier"/*, cascade = {CascadeType.ALL}*/)
	private Set<SearchResult> results = new HashSet<>();

	@Column(unique=true, nullable=false)
	private String name;

	@Lob
	private String text;

	/** make jpa happy */
	protected Supplier() { }

	public Supplier(String name)
	{
		if (name == null) throw new IllegalArgumentException("disallowed null parameter for name");
		this.name = name;
	}

	public Long getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	public Set<SearchResult> getSearchResults() { return results; }
	public void setResults(Set<SearchResult> results) { this.results = results; }

	public String asString()
	{
		return getClass().getSimpleName() + " [" + asStringSimpleFields() + "]";
	}

	public String asStringDepthOne()
	{
		return "Customer [[" + asStringSimpleFields() + "], [" + asStringNonSimpleFields() + "]]";
	}

	@Override public int compareTo(Supplier other)
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

		getSearchResults().forEach(item -> result.add(item.asString()));

		return result;
	}
}