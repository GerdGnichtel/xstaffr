package de.jmda.app.xstaffr.client.fx.settings.filter;

import java.util.Optional;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.Requester;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchRequest.State;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import de.jmda.app.xstaffr.common.domain.Supplier;

public class FilterManager
{
//	private final static Logger LOGGER = LogManager.getLogger(FilterManager.class);

	private Optional<Candidate> filterByCandidate = Optional.ofNullable(null);
	private Optional<Customer>  filterByCustomer = Optional.ofNullable(null);
	private Optional<Project>   filterByProject = Optional.ofNullable(null);
	private Optional<Requester> filterByRequester = Optional.ofNullable(null);
	private Optional<Supplier>  filterBySupplier = Optional.ofNullable(null);
	private Optional<State>     filterByState = Optional.ofNullable(null);

	public void filterByCandidate(Candidate candidate) { filterByCandidate = Optional.ofNullable(candidate); }
	public void filterByCustomer( Customer  customer ) { filterByCustomer  = Optional.ofNullable(customer ); }
	public void filterByProject(  Project   project  ) { filterByProject   = Optional.ofNullable(project  ); }
	public void filterByRequester(Requester requester) { filterByRequester = Optional.ofNullable(requester); }
	public void filterBySupplier( Supplier  supplier ) { filterBySupplier  = Optional.ofNullable(supplier ); }
	public void filterByState(    State     state    ) { filterByState     = Optional.ofNullable(state    ); }

	public boolean getPredicateFor(SearchRequest searchRequest)
	{
		// if there are no criteria present, the search request matches
		if (anyCriteriaPresent() == false) return true;

		// at least one criterion is present, so result must be set to non-null value by first check of a present criterion
		Boolean result = null;

		if (filterByCandidate.isPresent())
		{
			// this is the FIRST criteria check
			// set result to false to indicate that subsequent criteria checks have to apply a logical operation with result
			if (result == null) result = false;

			Long idOfCandidateCriterion = filterByCandidate.get().getId();

			// look for a search result whichs candidate matches candidate criterion
			for (SearchResult searchResult : searchRequest.getSearchResults())
			{
				Long idOfCandidateInSearchResult = searchResult.getCandidate().getId();

				result = idOfCandidateCriterion.equals(idOfCandidateInSearchResult);

				if (result) break;
			}
		}

		// subsequent logical operations applied with result can not be true if result is false here 
		if ((result != null) && (result == false)) return result;

		if (filterByCustomer.isPresent())
		{
			// this is NOT THE FIRST criteria check
			// set result to true to indicate that subsequent criteria checks have to apply a logical operation with result
			if (result == null) result = true;

			Long idOfCustomerCriterion = filterByCustomer.get().getId();
			Long idOfCustemerInSearchResult = searchRequest.getProject().getCustomer().getId();

			result = (result && idOfCustomerCriterion.equals(idOfCustemerInSearchResult));
		}

		// subsequent logical and operations applied with result can not be true if result is false here 
		if ((result != null) && (result == false)) return result;

		if (filterByProject.isPresent())
		{
			// this is NOT THE FIRST criteria check
			// set result to true to indicate that subsequent criteria checks have to apply a logical operation with result
			if (result == null) result = true;

			Long idOfProjectCriterion = filterByProject.get().getId();
			Long idOfProjectInSearchResult = searchRequest.getProject().getId();

			result = (result && idOfProjectCriterion.equals(idOfProjectInSearchResult));
		}

		// subsequent logical and operations applied with result can not be true if result is false here 
		if ((result != null) && (result == false)) return result;

		if (filterByRequester.isPresent())
		{
			// this is NOT THE FIRST criteria check
			// set result to true to indicate that subsequent criteria checks have to apply a logical operation with result
			if (result == null) result = true;

			Long idOfRequesterCriterion = filterByRequester.get().getId();
			Long idOfRequesterInSearchResult = searchRequest.getRequester().getId();

			result = (result && idOfRequesterCriterion.equals(idOfRequesterInSearchResult));
		}

		// subsequent logical and operations applied with result can not be true if result is false here 
		if ((result != null) && (result == false)) return result;

		if (filterBySupplier.isPresent())
		{
			// this is NOT THE FIRST criteria check
			// set result to false to indicate that subsequent criteria checks have to apply a logical operation with result
			if (result == null) result = false;

			Long idOfSupplierCriterion = filterBySupplier.get().getId();

			// look for a search result whichs supplier matches supplier criterion
			for (SearchResult searchResult : searchRequest.getSearchResults())
			{
				Long idOfSupplierInSearchResult = searchResult.getSupplier().getId();

				result = idOfSupplierCriterion.equals(idOfSupplierInSearchResult);

				if (result) break;
			}
		}

		// subsequent logical and operations applied with result can not be true if result is false here 
		if ((result != null) && (result == false)) return result;

		if (filterByState.isPresent())
		{
			// set result to false to indicate, that subsequent criteria checks have to apply a logical and
			// operation with result
			if (result == null) result = true;

			String stateCriterionAsString = filterByState.get().toString();
			String stateInSearchResultAsString = searchRequest.getState().toString();

			result = (result && stateCriterionAsString.equals(stateInSearchResultAsString));
		}

		return result;
	}

	private boolean anyCriteriaPresent()
	{
		return
				   filterByCandidate.isPresent()
				|| filterByCustomer .isPresent()
				|| filterByProject  .isPresent()
				|| filterByRequester.isPresent()
				|| filterBySupplier .isPresent()
				|| filterByState    .isPresent();
	}
}