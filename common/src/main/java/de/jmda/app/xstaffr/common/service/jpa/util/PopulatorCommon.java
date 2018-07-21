package de.jmda.app.xstaffr.common.service.jpa.util;

import static de.jmda.core.util.CollectionsUtil.asArray;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.service.XStaffrService;

/** This class will not perform any transaction management! */
public class PopulatorCommon implements Populator
{
//	private final static Logger LOGGER = LogManager.getLogger(PopulatorCommon.class);

	private XStaffrService xStaffrService;

	private Set<Candidate> candidates = new HashSet<>();
	private Set<Customer> customers = new HashSet<>();
	private Set<Requester> requesters = new HashSet<>();
	private Set<Supplier> suppliers = new HashSet<>();
	private Set<Project> projects = new HashSet<>();
	private Set<SearchRequest> searchRequests = new HashSet<>();
	private Set<SearchResult> searchResults = new HashSet<>();
	private Set<Contract> contracts = new HashSet<>();

	public PopulatorCommon(XStaffrService xStaffrService) { super(); this.xStaffrService = xStaffrService; }

	@Override public Set<Candidate> getCandidates() { return candidates; }
	@Override public Set<Customer> getCustomers() { return customers; }
	@Override public Set<Requester> getRequesters() { return requesters; }
	@Override public Set<Supplier> getSuppliers() { return suppliers; }
	@Override public Set<Project> getProjects() { return projects; }
	@Override public Set<SearchRequest> getSearchRequests() { return searchRequests; }
	@Override public Set<SearchResult> getSearchResults() { return searchResults; }
	@Override public Set<Contract> getContracts() { return contracts; }

	@Override public int unpopulate() { return xStaffrService.unpopulate(); }

	@Override public int populate()
	{
		int result = 0;
		result += populateCandidates();
		result += populateCustomers();
		result += populateRequesters();
		result += populateSuppliers();
		result += populateProjects();
		result += populateSearchRequests();
		result += populateSearchResults();
		result += populateContracts();
		return result;
	}

	private int populateCandidates()
	{
		Candidate candidate;

		candidate = new Candidate("Ndregjoni", "Fatjon", "Ndregjoni, Fatjon");
		candidate.setText("FATJON NDREGJONI B.Sc.\nBUNSENSTRASSE 19\n40215 DÜSSELDORF\n0176 65525815\nFATIJON@GMAIL.COM");
		candidates.add(candidate);
		xStaffrService.persistCandidate(candidate);

		candidate = new Candidate("Kamrowski", "Jürgen", "Kamrowski, Jürgen");
		candidate.setText("Jürgen Kamrowski\nRheinstrasse 13\n47198 Duisburg\n+49 171 4446485\njuergen.kamrowski@gmail.com");
		candidates.add(candidate);
		xStaffrService.persistCandidate(candidate);

		candidate = new Candidate("äöü", "ÄÖÜ", "ß€, äöü ÄÖÜ");
		candidate.setText("äöüÄÖÜß€");
		candidates.add(candidate);
		xStaffrService.persistCandidate(candidate);

		return candidates.size();
	}

	private int populateCustomers()
	{
		Customer customer;

		customer = new Customer("TKSE");
		customers.add(customer);
		xStaffrService.persistCustomer(customer);

		customer = new Customer("innogy");
		customers.add(customer);
		xStaffrService.persistCustomer(customer);

		return customers.size();
	}

	private int populateRequesters()
	{
		Requester requester;

		requester = new Requester("Karin Schmidt");
		requesters.add(requester);
		xStaffrService.persistRequester(requester);

		requester = new Requester("Christof Kauwetter");
		requesters.add(requester);
		xStaffrService.persistRequester(requester);

		return requesters.size();
	}

	private int populateSuppliers()
	{
		Supplier supplier;

		supplier = new Supplier("advantaxx");
		suppliers.add(supplier);
		xStaffrService.persistSupplier(supplier);

		supplier = new Supplier("excite");
		suppliers.add(supplier);
		xStaffrService.persistSupplier(supplier);

		return suppliers.size();
	}

	private int populateProjects()
	{
		Customer[] customerArray = asArray(Customer.class, customers);

		Project project;

		project = new Project(customerArray[0], "www");
		projects.add(project);
		xStaffrService.persistProject(project);

		project = new Project(customerArray[1], "new icon");
		projects.add(project);
		xStaffrService.persistProject(project);

		return projects.size();
	}

	private int populateSearchRequests()
	{
		int result = 1;
		for (int i = 0; i < result; i++)
		{
			SearchRequest entity =
					new SearchRequest(
							asArray(Project.class, projects)[i],
							asArray(Requester.class, requesters)[i],
							LocalDate.now(),
							LocalDate.now(),
							LocalDate.now(),
							"rolename " + i);
			searchRequests.add(entity);
			xStaffrService.persistSearchRequest(entity);
		}
		return result;
	}

	private int populateSearchResults()
	{
		int result = 1;
		for (int i = 0; i < result; i++)
		{
			SearchResult entity =
					new SearchResult(
							asArray(SearchRequest.class, searchRequests)[i],
							asArray(Candidate.class, candidates)[i],
							asArray(Supplier.class, suppliers)[i],
							LocalDate.now(),
							new BigDecimal(i));
			searchResults.add(entity);
			xStaffrService.persistSearchResult(entity);
		}
		return result;
	}

	private int populateContracts()
	{
		int result = 1;
		for (int i = 0; i < result; i++)
		{
			Contract entity =
					new Contract(
							asArray(SearchResult.class, searchResults)[i],
							LocalDate.now(),
							LocalDate.now(),
							new BigDecimal(99));
			contracts.add(entity);
			xStaffrService.persistContract(entity);
		}
		return result;
	}

	/** @see de.jmda.app.xstaffr.common.service.jpa.util.Populator#statistics(java.lang.String) */
	@Override public String statistics(String msg)
	{
		StringBuffer result = new StringBuffer(msg);

		result.append(  "number of candidats: " + xStaffrService.numberOfCandidates());
		result.append("\nnumber of customers: " + xStaffrService.numberOfCustomers());
		result.append("\nnumber of requesters: " + xStaffrService.numberOfRequesters());
		result.append("\nnumber of suppliers: " + xStaffrService.numberOfSuppliers());
		result.append("\nnumber of projects: " + xStaffrService.numberOfProjects());
		result.append("\nnumber of searchRequests: " + xStaffrService.numberOfSearchRequests());
		result.append("\nnumber of searchResults: " + xStaffrService.numberOfSearchResults());
		result.append("\nnumber of contracts: " + xStaffrService.numberOfContracts());

		return result.toString();
	}

	/** @see de.jmda.app.xstaffr.common.service.jpa.util.Populator#report(java.lang.String) */
	@Override public String report(String msg)
	{
		StringBuffer result = new StringBuffer(msg);

		result.append(  "number of candidats: " + xStaffrService.numberOfCandidates());
		xStaffrService.candidates().forEach(item -> result.append("\n" + item.asString()));
		result.append("\nnumber of customers: " + xStaffrService.numberOfCustomers());
		xStaffrService.customers().forEach(item -> result.append("\n" + item.asString()));
		result.append("\nnumber of requesters: " + xStaffrService.numberOfRequesters());
		xStaffrService.requesters().forEach(item -> result.append("\n" + item.asString()));
		result.append("\nnumber of suppliers: " + xStaffrService.numberOfSuppliers());
		xStaffrService.suppliers().forEach(item -> result.append("\n" + item.asString()));
		result.append("\nnumber of projects: " + xStaffrService.numberOfProjects());
		xStaffrService.projects().forEach(item -> result.append("\n" + item.asString()));
		result.append("\nnumber of searchRequests: " + xStaffrService.numberOfSearchRequests());
		xStaffrService.searchRequests().forEach(item -> result.append("\n" + item.asString()));
		result.append("\nnumber of searchResults: " + xStaffrService.numberOfSearchResults());
		xStaffrService.searchResults().forEach(item -> result.append("\n" + item.asString()));

		return result.toString();
	}

//	public String reportDepthOne(String msg)
//	{
//		StringBuffer result = new StringBuffer(msg);
//
//		result.append("number of search requests: "
//		    + xStaffrService.createNamedQuery(SearchRequest.QUERY_COUNT).getSingleResult());
//
//		xStaffrService.createNamedQuery(SearchRequest.QUERY_ALL, SearchRequest.class).getResultList()
//		  .forEach(sr -> result.append("\n" + sr.asStringDepthOne()));
//
//		return result.toString();
//	}
}