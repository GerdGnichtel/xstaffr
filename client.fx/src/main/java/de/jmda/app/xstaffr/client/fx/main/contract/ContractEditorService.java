package de.jmda.app.xstaffr.client.fx.main.contract;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface ContractEditorService
{
	void reload();

	public class ContractsChanged extends AbstractEvent<Set<Contract>>
	{
		public ContractsChanged(Object source, Set<Contract> data) { super(source, data); }
	}

	public class DisplaySearchResultsRequest extends AbstractEvent<Contract>
	{
		public DisplaySearchResultsRequest(Object source, Contract data) { super(source, data); }
		public Contract getContract() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link ContractEditorService} instance is available.
	 *
	 * @see ContractEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<ContractEditorService>
	{
		public ViewServiceAvailable(Object source, ContractEditorService service) { super(source, service); }
		public ContractEditorService getViewService() { return getData().get(); }
	}
}