package de.jmda.app.xstaffr.client.fx.main.contract;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link ContractEditorApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class ContractEditorAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(ContractEditorApp.class, args); }
}