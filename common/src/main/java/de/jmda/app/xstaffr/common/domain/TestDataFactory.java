package de.jmda.app.xstaffr.common.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class TestDataFactory
{
	private Set<Candidate> candidates = new HashSet<>();
	private Set<Customer> customers = new HashSet<>();
	private Set<Project> projects = new HashSet<>();
	private Set<Requester> requesters = new HashSet<>();
	private Set<SearchRequest> searchRequests = new HashSet<>();
	private Set<SearchResult> searchResults = new HashSet<>();
	private Set<Supplier> suppliers = new HashSet<>();

	private Candidate candidate1 = new Candidate("candidate1lastname", "candidate1firstname", "candidate1uniquename");
	private Customer customer1 = new Customer("customer1");
	private Project project1 = new Project(customer1, "project1");
	private Requester requester1 = new Requester("requester1");
	private Supplier supplier1 = new Supplier("supplier1");
	private SearchRequest searchRequest1 = new SearchRequest(project1, requester1, LocalDate.now(), LocalDate.now(), LocalDate.now(), "rolename1");
	private SearchResult searchResult1 = new SearchResult(searchRequest1, candidate1, supplier1, LocalDate.now(), new BigDecimal(0));

	public Set<Candidate> getCandidates() { return candidates; }
	public Set<Customer> getCustomers() { return customers; }
	public Set<Project> getProjects() { return projects; }
	public Set<Requester> getRequesters() { return requesters; }
	public Set<SearchRequest> getSearchRequests() { return searchRequests; }
	public Set<SearchResult> getSearchResults() { return searchResults; }
	public Set<Supplier> getSuppliers() { return suppliers; }
	public Candidate getCandidate1() { return candidate1; }
	public Customer getCustomer1() { return customer1; }
	public Project getProject1() { return project1; }
	public Requester getRequester1() { return requester1; }
	public Supplier getSupplier1() { return supplier1; }
	public SearchRequest getSearchRequest1() { return searchRequest1; }
	public SearchResult getSearchResult1() { return searchResult1; }
}