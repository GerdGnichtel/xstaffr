package de.jmda.fx.cdi.viewtemplate;

import de.jmda.fx.cdi.FXAppRunner;

/**
 * Starts {@link ViewApp} by invoking {@link FXAppRunner#run(Class, String[])} from {@link #main(String[])}.
 *
 * @author ruu@jmda.de
 */
public class ViewAppRunner
{
	public static void main(String[] args)
	{
		FXAppRunner.run(ViewApp.class, args);
	}
}