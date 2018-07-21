package de.jmda.app.xstaffr.client.fx.edit.project;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link ProjectEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class ProjectEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(ProjectEditorApp.class, args); }
}