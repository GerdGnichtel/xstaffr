package de.jmda.app.xstaffr.common.service.jpa;

import java.util.Set;

import javax.persistence.EntityManager;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.service.XStaffrService;
import de.jmda.core.util.Container;
import de.jmda.jpa.jse.AbstractPersistenceContextAccess;
import de.jmda.jpa.jse.TransactionRunner;

/**
 * This class implements <code>XStaffrService</code> operations in a non transaction managed JPA SE (Standard
 * Edition) environment. For each operation a transaction is explicitly started before it delegates to a
 * corresponding {@link XStaffrServiceJPACommon} operation. A transaction commit will be issued after the
 * call to the delegate returned.
 */
public class XStaffrServiceJPASE implements XStaffrService
{
	/** Constructor initialisation. */
	private EntityManager entityManager;

	/** Constructor initialisation. */
	private TransactionRunner transactionRunner;

	/** Constructor initialisation. */
	private XStaffrServiceJPACommon delegate;

	public XStaffrServiceJPASE(AbstractPersistenceContextAccess persistenceContextAccess)
	{
		entityManager = persistenceContextAccess.getEntityManager();
		transactionRunner = new TransactionRunner(entityManager);
		delegate = new XStaffrServiceJPACommon(entityManager);
	}

	@Override public String ping(String in)
	{
		Container<String> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.ping(in));
		return container.value;
	}

	@Override public void persistCandidate(Candidate entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistCandidate(entity)); }
	@Override public void persistCustomer(Customer entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistCustomer(entity)); }
	@Override public void persistContract(Contract entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistContract(entity)); }
	@Override public void persistRequester(Requester entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistRequester(entity)); }
	@Override public void persistSupplier(Supplier entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistSupplier(entity)); }
	@Override public void persistProject(Project entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistProject(entity)); }
	@Override public void persistSearchRequest(SearchRequest entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistSearchRequest(entity)); }
	@Override public void persistSearchResult(SearchResult entity)
	{ transactionRunner.runInTransaction(() -> delegate.persistSearchResult(entity)); }

	@Override public Candidate updateCandidate(Candidate entity)
	{
		Container<Candidate> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateCandidate(entity));
		return container.value;
	}

	@Override public Customer updateCustomer(Customer entity)
	{
		Container<Customer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateCustomer(entity));
		return container.value;
	}

	@Override public Contract updateContract(Contract entity)
	{
		Container<Contract> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateContract(entity));
		return container.value;
	}

	@Override public Requester updateRequester(Requester entity)
	{
		Container<Requester> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateRequester(entity));
		return container.value;
	}

	@Override public Supplier updateSupplier(Supplier entity)
	{
		Container<Supplier> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateSupplier(entity));
		return container.value;
	}

	@Override public Project updateProject(Project entity)
	{
		Container<Project> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateProject(entity));
		return container.value;
	}

	@Override public SearchRequest updateSearchRequest(SearchRequest entity)
	{
		Container<SearchRequest> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateSearchRequest(entity));
		return container.value;
	}

	@Override public SearchResult updateSearchResult(SearchResult entity)
	{
		Container<SearchResult> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.updateSearchResult(entity));
		return container.value;
	}

	@Override public Candidate findCandidateById(Long id)
	{
		Container<Candidate> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findCandidateById(id));
		return container.value;
	}

	@Override public Customer findCustomerById(Long id)
	{
		Container<Customer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findCustomerById(id));
		return container.value;
	}

	@Override public Contract findContractById(Long id)
	{
		Container<Contract> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findContractById(id));
		return container.value;
	}

	@Override public Requester findRequesterById(Long id)
	{
		Container<Requester> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findRequesterById(id));
		return container.value;
	}

	@Override public Supplier findSupplierById(Long id)
	{
		Container<Supplier> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findSupplierById(id));
		return container.value;
	}

	@Override public Project findProjectById(Long id)
	{
		Container<Project> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findProjectById(id));
		return container.value;
	}

	@Override public SearchRequest findSearchRequestById(Long id)
	{
		Container<SearchRequest> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findSearchRequestById(id));
		return container.value;
	}

	@Override public SearchResult findSearchResultById(Long id)
	{
		Container<SearchResult> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findSearchResultById(id));
		return container.value;
	}

	@Override public void deleteCandidate(Candidate entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteCandidate(entity)); }
	@Override public void deleteCustomer(Customer entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteCustomer(entity)); }
	@Override public void deleteContract(Contract entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteContract(entity)); }
	@Override public void deleteRequester(Requester entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteRequester(entity)); }
	@Override public void deleteSupplier(Supplier entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteSupplier(entity)); }
	@Override public void deleteProject(Project entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteProject(entity)); }
	@Override public void deleteSearchRequest(SearchRequest entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteSearchRequest(entity)); }
	@Override public void deleteSearchResult(SearchResult entity)
	{ transactionRunner.runInTransaction(() -> delegate.deleteSearchResult(entity)); }

	@Override public int deleteCandidates()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteCandidates());
		return container.value;
	}

	@Override public int deleteCustomers()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteCustomers());
		return container.value;
	}

	@Override public int deleteContracts()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteContracts());
		return container.value;
	}

	@Override public int deleteRequesters()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteRequesters());
		return container.value;
	}

	@Override public int deleteSuppliers()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteSuppliers());
		return container.value;
	}

	@Override public int deleteProjects()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteProjects());
		return container.value;
	}

	@Override public int deleteSearchRequests()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteSearchRequests());
		return container.value;
	}

	@Override public int deleteSearchResults()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.deleteSearchResults());
		return container.value;
	}

	@Override public Long numberOfCandidates()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfCandidates());
		return container.value;
	}

	@Override public Long numberOfCustomers()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfCustomers());
		return container.value;
	}

	@Override public Long numberOfContracts()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfContracts());
		return container.value;
	}

	@Override public Long numberOfRequesters()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfRequesters());
		return container.value;
	}

	@Override public Long numberOfSuppliers()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfSuppliers());
		return container.value;
	}

	@Override public Long numberOfProjects()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfProjects());
		return container.value;
	}

	@Override public Long numberOfSearchRequests()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfSearchRequests());
		return container.value;
	}

	@Override public Long numberOfSearchResults()
	{
		Container<Long> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.numberOfSearchResults());
		return container.value;
	}

	@Override public Set<Candidate> candidates()
	{
		Container<Set<Candidate>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.candidates());
		return container.value;
	}

	@Override public Set<Customer> customers()
	{
		Container<Set<Customer>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.customers());
		return container.value;
	}

	@Override public Set<Contract> contracts()
	{
		Container<Set<Contract>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.contracts());
		return container.value;
	}

	@Override public Set<Requester> requesters()
	{
		Container<Set<Requester>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.requesters());
		return container.value;
	}

	@Override public Set<Supplier> suppliers()
	{
		Container<Set<Supplier>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.suppliers());
		return container.value;
	}

	@Override public Set<Project> projects()
	{
		Container<Set<Project>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.projects());
		return container.value;
	}

	@Override public Set<SearchResult> searchResults()
	{
		Container<Set<SearchResult>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.searchResults());
		return container.value;
	}

	@Override public Set<SearchResult> searchResultsWithContracts()
	{
		Container<Set<SearchResult>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.searchResultsWithContracts());
		return container.value;
	}

	@Override public Customer findCustomerByName(String name)
	{
		Container<Customer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findCustomerByName(name));
		return container.value;
	}

	@Override public Customer findCustomerByNameWithProjects(String name)
	{
		Container<Customer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.findCustomerByNameWithProjects(name));
		return container.value;
	}

	@Override public Set<SearchRequest> searchRequests()
	{
		Container<Set<SearchRequest>> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.searchRequests());
		return container.value;
	}

	@Override public int unpopulate()
	{
		Container<Integer> container = new Container<>();
		transactionRunner.runInTransaction(() -> container.value = delegate.unpopulate());
		return container.value;
	}

	public EntityManager getEntityManager() { return entityManager; }
}