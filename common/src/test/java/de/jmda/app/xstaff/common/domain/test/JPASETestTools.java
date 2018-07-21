package de.jmda.app.xstaff.common.domain.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.common.service.jpa.XStaffrServiceJPASE;
import de.jmda.app.xstaffr.common.service.jpa.util.Populator;
import de.jmda.app.xstaffr.common.service.jpa.util.PopulatorCommon;

public class JPASETestTools
{
	private final static Logger LOGGER = LogManager.getLogger(JPASETestTools.class);

	/** log.debugs statistics about db population */
	public static void beforeClass() { logStatistics("population before class\n"); }

	/** log.debugs statistics about db population */
	public static void afterClass() { logStatistics("population after class\n"); }

	/** log.debugs <code>message</code> plus statistics from {@link Populator} */
	private static void logStatistics(String message)
	{
		// work with local entity manager (PersistenceContextAccess) since entityManager (PersistenceContextAccess)
		// might be in a bad state after a test
		PersistenceContextAccess persistenceContextAccess = new PersistenceContextAccess();
		Populator populator = new PopulatorCommon(new XStaffrServiceJPASE(persistenceContextAccess));
		LOGGER.debug(populator.statistics(message));
		persistenceContextAccess.getEntityManager().close();
	}

	/** lazy */
	private PersistenceContextAccess persistenceContextAccess;

	/** lazy */
	private XStaffrServiceJPASE xStaffrService;

	public XStaffrServiceJPASE getXStaffrService()
	{
		if ((xStaffrService == null) || xStaffrService.getEntityManager().isOpen() == false)
		{
			xStaffrService = new XStaffrServiceJPASE(getPersistenceContextAccess());
		}
		return xStaffrService;
	}

	/** initialises environment and clears population */
	public void before()
	{
		logStatistics("population before test\n");
		Populator populator = new PopulatorCommon(getXStaffrService());
		LOGGER.debug("removed " + populator.unpopulate() + " objects from population before test");
	}

	public void after() { logStatistics("population after test\n"); }

	private PersistenceContextAccess getPersistenceContextAccess()
	{
		if (persistenceContextAccess == null)
		{
			persistenceContextAccess = new PersistenceContextAccess();
		}
		return persistenceContextAccess;
	}
}