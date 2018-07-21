package de.jmda.app.xstaffr.client.fx.edit.customer;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface CustomerEditorService
{
	void reload();

	public class CustomersChanged extends AbstractEvent<Set<Customer>>
	{
		public CustomersChanged(Object source, Set<Customer> data) { super(source, data); }
		public Set<Customer> getCustomers() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link CustomerEditorService} instance is available.
	 *
	 * @see CustomerEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<CustomerEditorService>
	{
		public ViewServiceAvailable(Object source, CustomerEditorService service) { super(source, service); }
		public CustomerEditorService getViewService() { return getData().get(); }
	}
}