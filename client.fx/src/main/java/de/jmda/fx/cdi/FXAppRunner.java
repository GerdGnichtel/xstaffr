package de.jmda.fx.cdi;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import javafx.application.Application;

public abstract class FXAppRunner
{
	/**
	 * If not already initialised, CDI will be initialised in this method and {@code appClass} will be
	 * launched by invoking {@link Application#launch(Class, String...)}.
	 *
	 * @param appClass
	 * @param args
	 */
	public static void run(Class<? extends Application> appClass, String[] args)
	{
//		CDIProvider cdiProvider = CDI.getCDIProvider();
//
//		if (false == cdiProvider.isInitialized())
//		{
//			try (CDI<Object> cdi = CDI.getCDIProvider().initialize())
//			{
//				Application.launch(appClass, args);
//			}
//			// cdi closes automatically via try with resources
//		}
//		else
//		{
//			Application.launch(appClass, args);
//		}

		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.initialize())
		{
			Application.launch(appClass, args);
		}
		// cdi closes automatically via try with resources
	}
}