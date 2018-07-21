package de.jmda.app.xstaffr.client.fx.menu;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.fx.cdi.FXApp;
import de.jmda.fx.cdi.FXAppRunner;
import de.jmda.fx.cdi.FXView;

public class MenuAppRunner
{
	private final static Logger LOGGER = LogManager.getLogger(MenuAppRunner.class);

	public static void main(String[] args)
	{
		FXApp.setPostFXMLLoadedAction(getPostFXMLLoadedAction());
		FXAppRunner.run(MenuApp.class, args);
	}

	private static Optional<Runnable> getPostFXMLLoadedAction()
	{
		Runnable result =
				() ->
				{
					Optional<? extends FXView> mainViewOptional = MenuApp.getMainView();

					if (mainViewOptional.isPresent())
					{
						FXView fxView = mainViewOptional.get();

						if (fxView instanceof Menu)
						{
							Menu menu = (Menu) fxView;
							MenuService mainViewService = menu.getService();
							LOGGER.debug("main view service is null: " + (mainViewService == null));
						}
					}
				};
		return Optional.of(result);
	}
}