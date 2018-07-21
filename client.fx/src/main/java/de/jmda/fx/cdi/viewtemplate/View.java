package de.jmda.fx.cdi.viewtemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.fx.cdi.FXView;

public class View extends FXView
{
	private final static Logger LOGGER = LogManager.getLogger(View.class);

	public View()
	{
		LOGGER.debug("view constructor: " + getClass().getName());
	}

	@Override public ViewController getController()
	{
		return (ViewController) super.getController();
	}

	public ViewService getService() { return getController(); }
}