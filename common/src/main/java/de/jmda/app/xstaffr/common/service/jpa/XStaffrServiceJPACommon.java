package de.jmda.app.xstaffr.common.service.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Candidate__;
import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.app.xstaffr.common.domain.Contract__;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Customer_;
import de.jmda.app.xstaffr.common.domain.Customer__;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Project__;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.Requester__;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchRequest__;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.SearchResult__;
import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.domain.Supplier__;
import de.jmda.app.xstaffr.common.service.XStaffrService;

/**
 * This class implements <code>XStaffrService</code> operations on the {@link #entityManager} instance. It will <b>
 * not</b> perform any transaction management so that it can be used in container transaction managed EJBs with JTA
 * transaction management as well as in non JTA environments ({@link XStaffrServiceJPASE}).
 */
public class XStaffrServiceJPACommon implements XStaffrService
{
	public enum GraphType
	{
		FETCH("javax.persistence.fetchgraph"),
		LOAD("javax.persistence.loadgraph");

		private String name;
		private GraphType(String name) { this.name = name; }
		public String getName() { return name; }
	}

	private final static Logger LOGGER = LogManager.getLogger(XStaffrServiceJPACommon.class);

	/** constructor injection */
	private EntityManager entityManager;

	public XStaffrServiceJPACommon(EntityManager entityManager)
	{
		super();
		if (entityManager == null) throw new IllegalArgumentException("entity manager must not be null");
		this.entityManager = entityManager;
	}

	@Override public String ping(String in)
	{
		return
				in + "pinged at " +
				(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS")).format(new Date()) +
				(entityManager == null ? ", em == null" : ", em initialised");
	}

	@Override public void persistCandidate(Candidate entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		entityManager.persist(entity);
	}

	@Override public void persistCustomer(Customer entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		entityManager.persist(entity);
	}

	@Override public void persistRequester(Requester entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		entityManager.persist(entity);
	}

	@Override public void persistSupplier(Supplier entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		entityManager.persist(entity);
	}

	@Override public void persistProject(Project entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		if (entity.getCustomer() == null) throw new IllegalArgumentException("entity.customer must not be null");
		if (entity.getCustomer().getId() == null) throw new PersistenceException("entity.customer is not persistent");
		entityManager.persist(entity);
		entity.getCustomer().getProjects().add(entity);
	}

	@Override public void persistSearchRequest(SearchRequest entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		if (entity.getRequester() == null) throw new IllegalArgumentException("entity.requester must not be null");
		if (entity.getProject() == null) throw new IllegalArgumentException("entity.project must not be null");
		if (entity.getRequester().getId() == null) throw new PersistenceException("entity.requester is not persistent");
		if (entity.getProject().getId() == null) throw new PersistenceException("entity.project is not persistent");
		entityManager.persist(entity);
		entity.getProject().getSearchRequests().add(entity);
		entity.getRequester().getSearchRequests().add(entity);
	}

	@Override public void persistSearchResult(SearchResult entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		if (entity.getSearchRequest() == null) throw new IllegalArgumentException("entity.searchRequest must not be null");
		if (entity.getSupplier() == null) throw new IllegalArgumentException("entity.supplier must not be null");
		if (entity.getSearchRequest().getId() == null) throw new PersistenceException("entity.searchRequest is not persistent");
		if (entity.getSupplier().getId() == null) throw new PersistenceException("entity.supplier is not persistent");
		entityManager.persist(entity);
		entity.getSearchRequest().getSearchResults().add(entity);
		entity.getSupplier().getSearchResults().add(entity);
	}

	@Override public void persistContract(Contract entity)
	{
		if (entity == null) throw new IllegalArgumentException("entity must not be null");
		if (entity.getSearchResult() == null) throw new IllegalArgumentException("entity.searchResult must not be null");
		if (entity.getInception() == null) throw new IllegalArgumentException("entity.start must not be null");
		if (entity.getExpiration() == null) throw new IllegalArgumentException("entity.end must not be null");
		if (entity.getHourlyRate() == null) throw new IllegalArgumentException("entity.hourlyRate must not be null");
		if (entity.getSearchResult().getId() == null) throw new PersistenceException("entity.searchResult is not persistent");
		entityManager.persist(entity);
		entity.getSearchResult().getContracts().add(entity);
	}

	@Override public SearchRequest updateSearchRequest(SearchRequest entity) { return entityManager.merge(entity); }
	@Override public Candidate updateCandidate(Candidate entity) { return entityManager.merge(entity); }
	@Override public Contract updateContract(Contract entity) { return entityManager.merge(entity); }
	@Override public Customer updateCustomer(Customer entity) { return entityManager.merge(entity); }
	@Override public Requester updateRequester(Requester entity)  { return entityManager.merge(entity); }
	@Override public Supplier updateSupplier(Supplier entity) { return entityManager.merge(entity); }
	@Override public Project updateProject(Project entity) { return entityManager.merge(entity); }
	@Override public SearchResult updateSearchResult(SearchResult entity) { return entityManager.merge(entity); }

	@Override public Candidate findCandidateById(Long id) { return entityManager.find(Candidate.class, id); }
	@Override public Contract findContractById(Long id) { return entityManager.find(Contract.class, id); }
	@Override public Customer findCustomerById(Long id) { return entityManager.find(Customer.class, id); }
	@Override public Requester findRequesterById(Long id) { return entityManager.find(Requester.class, id); }
	@Override public Supplier findSupplierById(Long id) { return entityManager.find(Supplier.class, id); }
	@Override public Project findProjectById(Long id) { return entityManager.find(Project.class, id); }
	@Override public SearchRequest findSearchRequestById(Long id) { return entityManager.find(SearchRequest.class, id); }
	@Override public SearchResult findSearchResultById(Long id) { return entityManager.find(SearchResult.class, id); }

	@Override public int unpopulate()
	{
		return
				  deleteCandidates()
				+ deleteContracts()
				+ deleteSearchResults()
				+ deleteSearchRequests()
				+ deleteProjects()
				+ deleteCustomers()
				+ deleteSuppliers()
				+ deleteRequesters()
				;
	}

	@Override public void deleteCandidate(Candidate entity) { entityManager.remove(entity); }
	@Override public void deleteCustomer(Customer entity) { entityManager.remove(entity); }
	@Override public void deleteContract(Contract entity) { entityManager.remove(entity); }
	@Override public void deleteRequester(Requester entity) { entityManager.remove(entity); }
	@Override public void deleteSupplier(Supplier entity) { entityManager.remove(entity); }
	@Override public void deleteProject(Project entity) { entityManager.remove(entity); }
	@Override public void deleteSearchRequest(SearchRequest entity) { entityManager.remove(entity); }
	@Override public void deleteSearchResult(SearchResult entity) { entityManager.remove(entity); }

	@Override public int deleteCandidates()
	{
		int result = entityManager.createNamedQuery(Candidate__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + Candidate.class + " objects");
		return result;
	}

	@Override public int deleteCustomers()
	{
		int result = entityManager.createNamedQuery(Customer__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + Customer.class + " objects");
		return result;
	}

	@Override public int deleteContracts()
	{
		int result = entityManager.createNamedQuery(Contract__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + Contract.class + " objects");
		return result;
	}

	@Override public int deleteRequesters()
	{
		int result = entityManager.createNamedQuery(Requester__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + Requester.class + " objects");
		return result;
	}

	@Override public int deleteSuppliers()
	{
		int result = entityManager.createNamedQuery(Supplier__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + Supplier.class + " objects");
		return result;
	}

	@Override public int deleteProjects()
	{
		int result = entityManager.createNamedQuery(Project__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + Project.class + " objects");
		return result;
	}

	@Override public int deleteSearchRequests()
	{
		int result = entityManager.createNamedQuery(SearchRequest__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + SearchRequest.class + " objects");
		return result;
	}

	@Override public int deleteSearchResults()
	{
		int result = entityManager.createNamedQuery(SearchResult__.QUERY_UPDATE_DELETE_ALL_NAME).executeUpdate();
		LOGGER.debug("deleted " + result + " " + SearchResult.class + " objects");
		return result;
	}

	@Override public Long numberOfCandidates() { return entityManager.createNamedQuery(Candidate__.QUERY_COUNT_NAME, Long.class).getSingleResult(); }
	@Override public Long numberOfCustomers() { return entityManager.createNamedQuery(Customer__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}
	@Override public Long numberOfContracts() { return entityManager.createNamedQuery(Contract__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}
	@Override public Long numberOfRequesters() { return entityManager.createNamedQuery(Requester__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}
	@Override public Long numberOfSuppliers() { return entityManager.createNamedQuery(Supplier__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}
	@Override public Long numberOfProjects() { return entityManager.createNamedQuery(Project__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}
	@Override public Long numberOfSearchRequests() { return entityManager.createNamedQuery(SearchRequest__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}
	@Override public Long numberOfSearchResults() { return entityManager.createNamedQuery(SearchResult__.QUERY_COUNT_NAME, Long.class).getSingleResult();	}

	@Override public Set<Candidate> candidates() { return new HashSet<Candidate>(entityManager.createNamedQuery(Candidate__.QUERY_ALL_NAME, Candidate.class).getResultList()); }
	@Override public Set<Customer> customers() { return new HashSet<Customer>(entityManager.createNamedQuery(Customer__.QUERY_ALL_NAME, Customer.class).getResultList()); }
	@Override public Set<Contract> contracts() { return new HashSet<Contract>(entityManager.createNamedQuery(Contract__.QUERY_ALL_NAME, Contract.class).getResultList()); }
	@Override public Set<Requester> requesters() { return new HashSet<Requester>(entityManager.createNamedQuery(Requester__.QUERY_ALL_NAME, Requester.class).getResultList()); }
	@Override public Set<Supplier> suppliers() { return new HashSet<Supplier>(entityManager.createNamedQuery(Supplier__.QUERY_ALL_NAME, Supplier.class).getResultList()); }
	@Override public Set<Project> projects() { return new HashSet<Project>(entityManager.createNamedQuery(Project__.QUERY_ALL_NAME, Project.class).getResultList()); }
	@Override public Set<SearchRequest> searchRequests() { return new HashSet<SearchRequest>(entityManager.createNamedQuery(SearchRequest__.QUERY_ALL_NAME, SearchRequest.class).getResultList()); }
	@Override public Set<SearchResult> searchResults() { return new HashSet<SearchResult>(entityManager.createNamedQuery(SearchResult__.QUERY_ALL_NAME, SearchResult.class).getResultList()); }


	@Override public Customer findCustomerByName(String name)
	{
		if (name == null || name.isEmpty()) throw new IllegalArgumentException("name must not be null or empty");
		return
				entityManager.createNamedQuery(Customer__.QUERY_BY_NAME_NAME, Customer.class)
				             .setParameter("name", name).getSingleResult();
	}

	private EntityGraph<Customer> entityGraphCustomerWithProjects;

	private EntityGraph<Customer> getEntityGraphCustomerWithProjects()
	{
		if (entityGraphCustomerWithProjects == null)
		{
			entityGraphCustomerWithProjects = entityManager.createEntityGraph(Customer.class);
			entityGraphCustomerWithProjects.addAttributeNodes(Customer_.projects);
		}
		return entityGraphCustomerWithProjects;
	}

	@Override public Customer findCustomerByNameWithProjects(String name)
	{
		return
				entityManager.createNamedQuery(Customer__.QUERY_BY_NAME_NAME, Customer.class)
				             .setParameter("name", name)
//				             .setHint(GraphType.LOAD.name, entityManager.getEntityGraph(Customer__.GRAPH_PROJECTS_NAME))
				             .setHint(GraphType.LOAD.name, getEntityGraphCustomerWithProjects())
				             .getSingleResult();
	}

	@Override public Set<SearchResult> searchResultsWithContracts()
	{
		return
				new HashSet<>(
						entityManager
								.createNamedQuery(SearchResult__.QUERY_ALL_NAME, SearchResult.class)
								.setHint(GraphType.LOAD.name, SearchResult__.getHintWithContracts(entityManager, GraphType.LOAD))
								.getResultList());
	}
}