package de.jmda.jpa.jse;

public interface PersistenceUnitNameProvider
{
	/** @return the persistent unit name that is in charge for META-INF/persistence.xml */
	String getPersistenceUnitName();
}