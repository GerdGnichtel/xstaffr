package de.jmda.app.xstaffr.client.fx.util;

import java.util.Collection;
import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Project;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class ProjectFXUtils
{
	public static class CellFactory implements Callback<ListView<Project>, ListCell<Project>>
	{
		@Override public ListCell<Project> call(ListView<Project> param)
		{
			return new ListCell<Project>()
			{
				@Override protected void updateItem(Project item, boolean empty)
				{
					super.updateItem(item, empty);
					if (item == null || empty) { setGraphic(null); }
					else { setText(item.getName()); }
				}
			};
		}
	}

	public static interface StringConverterTest { boolean test(Project project, String string); }

	public static class StringConverterTestProjectName implements StringConverterTest
	{
		@Override public boolean test(Project project, String name)
		{
			return project.getName().equals(name);
		}
	}

	public static class StringConverter extends javafx.util.StringConverter<Project>
	{
		private Collection<Project> projects;
		private StringConverterTest stringConverterTest;

		public StringConverter(final Collection<Project> projects, StringConverterTest stringConverterTest)
		{
			this.projects = projects;
			this.stringConverterTest = stringConverterTest;
		}

		@Override public String toString(Project project)
		{
			if (project != null) return project.getName();
			return "";
		}

		@Override public Project fromString(String string)
		{
			for (Project project : projects)
			{
				if (stringConverterTest.test(project, string)) return project;
			}
			return null;
		}

		public void repopulate(Set<Project> projects)
		{
			this.projects = projects;
		}
	}
}
