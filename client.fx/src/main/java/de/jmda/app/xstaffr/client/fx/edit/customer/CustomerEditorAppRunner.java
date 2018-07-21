package de.jmda.app.xstaffr.client.fx.edit.customer;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link CustomerEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class CustomerEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(CustomerEditorApp.class, args); }
}