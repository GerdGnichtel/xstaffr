package de.jmda.app.xstaffr.client.fx.edit.supplier;

import de.jmda.app.xstaffr.common.domain.Supplier;
import de.jmda.app.xstaffr.common.domain.Supplier_;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public final class SupplierTableWrapper
{
	private Supplier supplier;
	private StringProperty name;

	public SupplierTableWrapper(Supplier supplier)
	{
		this.supplier = supplier;
		try
		{
			name =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(supplier)
							.name(Supplier_.name.getName())
							.build();
		}
		catch (NoSuchMethodException e)
		{
			throw new ExceptionInInitializerError("failure creating " + getClass().getName() + " instance\n" + e);
		}
	}

	public Supplier getSupplier() { return supplier; }
	public void setSupplier(Supplier supplier) { this.supplier = supplier; }
	public String getName() { return name.get(); }
	public void setName(String name) { this.name.set(name); }
}