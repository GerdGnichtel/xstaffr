package de.jmda.app.xstaffr.client.fx.edit.candidate;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Candidate_;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public final class CandidateTableWrapper
{
	private Candidate candidate;
	private StringProperty lastName;
	private StringProperty firstName;
	private StringProperty uniqueName;
	private StringProperty notes;

	public CandidateTableWrapper(Candidate candidate)
	{
		this.candidate = candidate;
		try
		{
			lastName =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(candidate)
							.name(Candidate_.lastName.getName())
							.build();
			firstName =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(candidate)
							.name(Candidate_.firstName.getName())
							.build();
			uniqueName =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(candidate)
							.name(Candidate_.uniqueName.getName())
							.build();
			notes =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(candidate)
							.name(Candidate_.text.getName())
							.build();
		}
		catch (NoSuchMethodException e)
		{
			throw new ExceptionInInitializerError("failure creating " + getClass().getName() + " instance\n" + e);
		}
	}

	public Candidate getCandidate() { return candidate; }
	public void setCandidate(Candidate candidate) { this.candidate = candidate; }
	public String getLastName() { return lastName.get(); }
	public String getFirstName() { return firstName.get(); }
	public void setFirstName(String firstName) { this.firstName.set(firstName); }
	public String getUniqueName() { return uniqueName.get(); }
	public void setUniqueName(StringProperty uniqueName) { this.uniqueName = uniqueName; }
	public String getNotes() { return notes.get(); }
	public void setNotes(String notes) { this.notes.set(notes); }
}