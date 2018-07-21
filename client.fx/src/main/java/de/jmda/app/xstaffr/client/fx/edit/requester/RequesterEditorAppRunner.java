package de.jmda.app.xstaffr.client.fx.edit.requester;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link RequesterEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class RequesterEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(RequesterEditorApp.class, args); }
}