package de.jmda.app.xstaffr.client.fx.settings.filter;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link FilterEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class FilterEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(FilterEditorApp.class, args); }
}