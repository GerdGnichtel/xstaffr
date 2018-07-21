package de.jmda.app.xstaffr.client.fx.edit.customer;

import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Customer_;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public final class CustomerTableWrapper
{
	private Customer customer;
	private StringProperty name;

	public CustomerTableWrapper(Customer customer)
	{
		this.customer = customer;
		try
		{
			name =
					JavaBeanStringPropertyBuilder
							.create()
							.bean(customer)
							.name(Customer_.name.getName())
							.build();
		}
		catch (NoSuchMethodException e)
		{
			throw new ExceptionInInitializerError("failure creating " + getClass().getName() + " instance\n" + e);
		}
	}

	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	public String getName() { return name.get(); }
	public void setName(String name) { this.name.set(name); }
}