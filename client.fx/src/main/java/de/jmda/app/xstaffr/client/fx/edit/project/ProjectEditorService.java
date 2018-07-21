package de.jmda.app.xstaffr.client.fx.edit.project;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface ProjectEditorService
{
	void reload();

	public class ProjectsChanged extends AbstractEvent<Set<Project>>
	{
		public ProjectsChanged(Object source, Set<Project> data) { super(source, data); }
		public Set<Project> getProjects() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link ProjectEditorService} instance is available.
	 *
	 * @see ProjectEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<ProjectEditorService>
	{
		public ViewServiceAvailable(Object source, ProjectEditorService service) { super(source, service); }
		public ProjectEditorService getViewService() { return getData().get(); }
	}
}