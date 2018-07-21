package de.jmda.app.xstaffr.client.fx.edit.supplier;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link MainEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class SupplierEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(SupplierEditorApp.class, args); }
}