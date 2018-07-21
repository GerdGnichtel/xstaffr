package de.jmda.jpa.jse;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.jpa.Operation;
import de.jmda.jpa.jse.TransactionRunner;

public class TransactionRunner
{
	private final static Logger LOGGER = LogManager.getLogger(TransactionRunner.class);

	public static void runInTransaction(EntityManager entityManager, Operation operation)
	{
		EntityTransaction transaction = entityManager.getTransaction();
		boolean startedTransaction = false;

		try
		{
			if (transaction.isActive())
			{
				LOGGER.debug("resuming active transaction " + transaction);
			}
			else
			{
				LOGGER.debug("starting transaction " + transaction);
				transaction.begin();
				startedTransaction = true;
			}
			LOGGER.debug("calling operation in transaction " + transaction);
			operation.execute();
		}
		catch (Throwable t)
		{
			LOGGER.error("failure executing operation in transaction " + transaction, t);
			throw t;
		}
		finally
		{
			if (startedTransaction)
			{
				if (transaction.isActive())
				{
					LOGGER.debug("committing transaction, " + transaction);
					transaction.commit();
					LOGGER.debug("commit succeeded, " + transaction);
				}
				else
				{
					LOGGER.warn("can't commit inactive transaction " + transaction);
				}
			}
		}
	}

	private EntityManager entityManager;

	public TransactionRunner(EntityManager entityManager)
	{
		super();
		this.entityManager = entityManager;
	}

	public void runInTransaction(Operation operation)
	{
		runInTransaction(entityManager, operation);
	}

	public <T> T persistInTransaction(T entity)
	{
		runInTransaction(() -> entityManager.persist(entity));
		return entity;
	}

	public <T> void removeInTransaction(T entity)
	{
		runInTransaction(() -> entityManager.remove(entity));
	}
}