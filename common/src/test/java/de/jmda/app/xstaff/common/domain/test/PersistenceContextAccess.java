package de.jmda.app.xstaff.common.domain.test;

import de.jmda.jpa.jse.AbstractPersistenceContextAccess;

public class PersistenceContextAccess extends AbstractPersistenceContextAccess
{
	@Override public String getPersistenceUnitName() { return "xstaffr.jpa.se"; }
}