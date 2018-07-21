package de.jmda.app.xstaff.common.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.jmda.app.xstaff.common.domain.test.JPASETestTools;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.TestDataFactory;

public class JUT_JPA_SE_Project
{
//	private final static Logger LOGGER = LogManager.getLogger(JUT_JPA_SE_Project.class);

	private TestDataFactory testDataFactory;
	private JPASETestTools jpaTestTools;

	@BeforeClass public static void beforeClass() { JPASETestTools.beforeClass(); }
	@AfterClass public static void afterClass() { JPASETestTools.afterClass(); }

	@Before public void before()
	{
		testDataFactory = new TestDataFactory();
		jpaTestTools = new JPASETestTools();
		jpaTestTools.before();
	}

	@After public void after() { jpaTestTools.after(); }

	@Test public void persistProjectWithCustomer()
	{
		Project project = testDataFactory.getProject1();

		// persist
		jpaTestTools.getXStaffrService().persistCustomer(project.getCustomer());
		jpaTestTools.getXStaffrService().persistProject(project);

		// retrieve
		Project foundProject = jpaTestTools.getXStaffrService().findProjectById(project.getId());

		// assert
		assertThat("unexpected null value from find", foundProject, not(nullValue()));
		assertThat("unexpected null id from find", foundProject.getId(), not(nullValue()));
		assertThat("unexpected name from find", foundProject.getName(), equalTo(project.getName()));
		assertThat("unexpected customer from find", foundProject.getCustomer(), equalTo(project.getCustomer()));
	}
}