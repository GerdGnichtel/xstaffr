package de.jmda.app.xstaffr.client.fx.edit.supplier;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface SupplierEditorService
{
	void reload();

	public class SuppliersChanged extends AbstractEvent<Set<Supplier>>
	{
		public SuppliersChanged(Object source, Set<Supplier> data) { super(source, data); }
		public Set<Supplier> getSuppliers() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link SupplierEditorService} instance is available.
	 *
	 * @see MainEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<SupplierEditorService>
	{
		public ViewServiceAvailable(Object source, SupplierEditorService service) { super(source, service); }
		public SupplierEditorService getViewService() { return getData().get(); }
	}
}