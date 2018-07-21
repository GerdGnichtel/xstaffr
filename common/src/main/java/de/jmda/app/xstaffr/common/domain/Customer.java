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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries
({
	@NamedQuery(name=Customer__.QUERY_ALL_NAME, query=Customer__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=Customer__.QUERY_COUNT_NAME, query=Customer__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=Customer__.QUERY_UPDATE_DELETE_ALL_NAME, query=Customer__.QUERY_UPDATE_DELETE_ALL_EXPRESSIOM),
	@NamedQuery(name=Customer__.QUERY_BY_NAME_NAME, query=Customer__.QUERY_BY_NAME_EXPRESSION),
})
public class Customer implements Serializable, Comparable<Customer>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy = "customer"/*, cascade = {CascadeType.ALL}*/)
	private Set<Project> projects = new HashSet<>();

	@Column(unique=true, nullable=false)
	private String name;

	/** make jpa happy */
	protected Customer() { }

	public Customer(String name)
	{
		if (name == null) throw new IllegalArgumentException("disallowed null parameter for name");
		this.name = name;
	}

	public Long getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Set<Project> getProjects() { return projects; }
	public void setProjects(Set<Project> projects) { this.projects = projects; }

	public String asString()
	{
		return getClass().getSimpleName() + " [" + asStringSimpleFields() + "]";
	}

	public String asStringDepthOne()
	{
		return "Customer [[" + asStringSimpleFields() + "], [" + asStringNonSimpleFields() + "]]";
	}

	@Override public int compareTo(Customer other)
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

		getProjects().forEach(item -> result.add(item.asString()));

		return result;
	}
}