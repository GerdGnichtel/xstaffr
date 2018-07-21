package de.jmda.app.xstaffr.common.service.jpa.util;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.Supplier;

public interface Populator
{
	Set<Candidate> getCandidates();
	Set<Customer> getCustomers();
	Set<Requester> getRequesters();
	Set<Supplier> getSuppliers();
	Set<Project> getProjects();
	Set<SearchRequest> getSearchRequests();
	Set<SearchResult> getSearchResults();
	Set<Contract> getContracts();

	int unpopulate();
	int populate();

	String statistics(String msg);
	String report(String msg);
}