package de.jmda.fx.cdi.viewtemplate;

import javax.enterprise.inject.spi.CDI;

import de.jmda.fx.cdi.FXApp;
import de.jmda.fx.cdi.FXView;

public class ViewApp extends FXApp
{
	private FXView fxView;

	@Override protected FXView getFXView()
	{
		if (fxView == null)
		{
			fxView = CDI.current().select(View.class).get();
		}

		return fxView;
	}
}