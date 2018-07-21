package de.jmda.app.xstaffr.client.fx.menu;

import de.jmda.fx.cdi.FXView;

public class Menu extends FXView
{
	@Override public MenuController getController() { return (MenuController) super.getController(); }

	public MenuService getService() { return getController(); }
}