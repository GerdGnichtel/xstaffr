package de.jmda.app.xstaffr.client.fx;

import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.app.xstaffr.common.service.jpa.XStaffrServiceJPASE;

/**
 * Provider for {@link XStaffrService} instances in a non-JTA, JPA SE (Standard Edition) environment.
 */
public class XStaffrServiceProviderJPASE implements XStaffrServiceProvider
{
	private final static XStaffrServiceProvider INSTANCE = new XStaffrServiceProviderJPASE();

	private XStaffrServiceProviderJPASE() { }

	public final static XStaffrServiceProvider instance() { return INSTANCE; }

	private XStaffrService xStaffrService;

	@Override public XStaffrService provide()
	{
		if ((xStaffrService == null) ||
		    (false == ((XStaffrServiceJPASE) xStaffrService).getEntityManager().isOpen()))
		{
			xStaffrService = new XStaffrServiceJPASE(new PersistenceContextAccess());
		}
		return xStaffrService;
	}
}