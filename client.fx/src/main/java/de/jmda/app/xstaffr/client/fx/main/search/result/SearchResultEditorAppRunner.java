package de.jmda.app.xstaffr.client.fx.main.search.result;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link MainEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class SearchResultEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(SearchResultEditorApp.class, args); }
}