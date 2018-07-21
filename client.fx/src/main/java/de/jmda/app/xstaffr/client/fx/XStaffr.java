package de.jmda.app.xstaffr.client.fx;

import de.jmda.fx.cdi.FXView;

public class XStaffr extends FXView
{
	@Override public XStaffrController getController() { return (XStaffrController) super.getController(); }

	public XStaffrService getService() { return getController(); }
}