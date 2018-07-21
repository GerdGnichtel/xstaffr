package de.jmda.app.xstaff.common.domain.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class JUT_JPA_SE_Environment
{
	private PersistenceContextAccess persistenceContextAccess;
	
	@Before public void before() { persistenceContextAccess = new PersistenceContextAccess(); }

	@Test public void persistenceUnitNameIsNotNull()
	{
		assertThat(persistenceContextAccess.getPersistenceUnitName(), is(notNullValue()));
	}
	@Test public void persistenceUnitNameHasCorrectValue()
	{
		assertThat(persistenceContextAccess.getPersistenceUnitName(), is(equalTo("xstaffr.jpa.se")));
	}
	@Test public void entityManagerFactoryIsNotNull()
	{
		assertThat(persistenceContextAccess.getEntityManagerFactory(), is(notNullValue()));
	}
	@Test public void entityManagerFactoryIsOpen()
	{
		assertThat(persistenceContextAccess.getEntityManagerFactory().isOpen(), is(true));
	}
	@Test public void entityManagerIsNotNull()
	{
		assertThat(persistenceContextAccess.getEntityManager(), is(notNullValue()));
	}
	@Test public void entityManagerIsOpen()
	{
		assertThat(persistenceContextAccess.getEntityManager().isOpen(), is(true));
	}
	@Test public void transactionRunnerIsNotNull()
	{
		assertThat(persistenceContextAccess.getTransactionRunner(), is(notNullValue()));
	}
}