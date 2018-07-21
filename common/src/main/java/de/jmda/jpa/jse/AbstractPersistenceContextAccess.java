package de.jmda.jpa.jse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Provides access to {@link EntityManagerFactory}, {@link EntityManager} and {@link TransactionRunner}
 * instances based on {@link #getPersistenceUnitName()}. Override {@link #getPersistenceUnitName()} in
 * subclasses to specify the persistence unit (defined in persistence.xml) you want to use.
 */
public abstract class AbstractPersistenceContextAccess implements PersistenceUnitNameProvider
{
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private TransactionRunner transactionRunner;

	public EntityManagerFactory getEntityManagerFactory()
	{
		if (entityManagerFactory == null)
		{
			entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
		}
		return entityManagerFactory;
	}

	public EntityManager getEntityManager()
	{
		if (entityManager == null)
		{
			entityManager = getEntityManagerFactory().createEntityManager();
		}
		return entityManager;
	}

	public TransactionRunner getTransactionRunner()
	{
		if (transactionRunner == null)
		{
			transactionRunner = new TransactionRunner(getEntityManager());
		}
		return transactionRunner;
	}
	public String info()
	{
		return
				    "PERSISTENCE_UNIT_NAME : " +  getPersistenceUnitName()
				+ "\nENTITY_MANAGER_FACTORY: " + (getEntityManagerFactory() == null ? "absent" : "present")
				+ "\nENTITY_MANAGER        : " + (getEntityManager()        == null ? "absent" : "present")
				+ "\nTRANSACTION_RUNNER    : " + (getTransactionRunner()    == null ? "absent" : "present");
	}
}