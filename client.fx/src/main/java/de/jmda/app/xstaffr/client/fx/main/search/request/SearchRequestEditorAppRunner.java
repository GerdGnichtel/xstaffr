package de.jmda.app.xstaffr.client.fx.main.search.request;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link MainEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class SearchRequestEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(SearchRequestEditorApp.class, args); }
}