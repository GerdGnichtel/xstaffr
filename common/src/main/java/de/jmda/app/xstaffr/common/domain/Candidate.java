package de.jmda.app.xstaffr.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries
({
	@NamedQuery(name=Candidate__.QUERY_ALL_NAME, query=Candidate__.QUERY_ALL_EXPRESSION),
	@NamedQuery(name=Candidate__.QUERY_COUNT_NAME, query=Candidate__.QUERY_COUNT_EXPRESSION),
	@NamedQuery(name=Candidate__.QUERY_UPDATE_DELETE_ALL_NAME, query=Candidate__.QUERY_UPDATE_DELETE_ALL_EXPRESSION),
	@NamedQuery(name=Candidate__.QUERY_BY_LAST_NAME_NAME, query=Candidate__.QUERY_BY_LAST_NAME_EXPRESSION)
})
public class Candidate implements Serializable, Comparable<Candidate>//, RemoveOperationProvider
{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable=false)
	private String lastName;

	@Column(nullable=false)
	private String firstName;

	@Column(nullable=false, unique=true)
	private String uniqueName;

	@Column(length=1024) @Lob
	private String text;

	/** make jpa happy */
	protected Candidate() { }

	public Candidate(String lastName, String firstName, String uniqueName)
	{
		if (lastName == null) throw new IllegalArgumentException("disallowed null parameter for lastName");
		if (firstName == null) throw new IllegalArgumentException("disallowed null parameter for firstName");
		if (uniqueName == null) throw new IllegalArgumentException("disallowed null parameter for uniqueName");
		this.lastName = lastName;
		this.firstName = firstName;
		this.uniqueName = uniqueName;
	}

	public Long getId() { return id; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getText() { return text; }
	public void setText(String notes) { this.text = notes; }
	public String getUniqueName() { return uniqueName; }
	public void setUniqueName(String uniqueName) { this.uniqueName = uniqueName; }

	public String asString()
	{
		return getClass().getSimpleName() + " [" + asStringSimpleFields() + "]";
	}

	public String asStringDepthOne()
	{
		return "Customer [[" + asStringSimpleFields() + "], [" + asStringNonSimpleFields() + "]]";
	}

	@Override public int compareTo(Candidate other)
	{
		return getLastName().compareTo(other.getLastName());
	}

	private String asStringSimpleFields()
	{
		return
				  getClass().getSimpleName()
				+ " ["
				+      "id=" + id
				+    ", lastName=" + lastName
				+    ", text=" + text
				+  "]";
	}

	private String asStringNonSimpleFields()
	{
		return "roles [" + String.join(", ", resultsAsStrings()) + "]";
	}

	private Iterable<String> resultsAsStrings()
	{
		Collection<String> result = new ArrayList<>();

//		getProjects().forEach(item -> result.add(item.asString()));

		return result;
	}
}