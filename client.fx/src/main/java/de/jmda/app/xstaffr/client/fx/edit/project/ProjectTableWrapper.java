package de.jmda.app.xstaffr.client.fx.edit.project;

import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Project_;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public final class ProjectTableWrapper
{
	private Project project;
	private StringProperty name;

	public ProjectTableWrapper(Project project)
	{
		this.project = project;
		try
		{
			name =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(project)
							.name(Project_.name.getName())
							.build();
		}
		catch (NoSuchMethodException e)
		{
			throw new ExceptionInInitializerError("failure creating " + getClass().getName() + " instance\n" + e);
		}
	}

	public Project getProject() { return project; }
	public void setProject(Project project) { this.project = project; }
	public String getName() { return name.get(); }
	public void setName(String name) { this.name.set(name); }
}