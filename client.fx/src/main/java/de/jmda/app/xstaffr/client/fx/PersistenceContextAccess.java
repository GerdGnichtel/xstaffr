package de.jmda.app.xstaffr.client.fx;

import de.jmda.jpa.jse.AbstractPersistenceContextAccess;

public class PersistenceContextAccess extends AbstractPersistenceContextAccess
{
	@Override public String getPersistenceUnitName() { return "xstaffr.jpa.se"; }
}