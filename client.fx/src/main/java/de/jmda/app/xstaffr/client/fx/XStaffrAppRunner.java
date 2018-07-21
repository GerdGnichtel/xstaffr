package de.jmda.app.xstaffr.client.fx;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link XStaffrApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class XStaffrAppRunner
{
	public static void main(String[] args) { FXAppRunner.run(XStaffrApp.class, args); }
}