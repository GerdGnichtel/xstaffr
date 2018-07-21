package de.jmda.app.xstaffr.client.fx.util;

import java.util.Collection;
import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Customer;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class CustomerFXUtils
{
	public static class CellFactory implements Callback<ListView<Customer>, ListCell<Customer>>
	{
		@Override public ListCell<Customer> call(ListView<Customer> param)
		{
			return new ListCell<Customer>()
			{
				@Override protected void updateItem(Customer item, boolean empty)
				{
					super.updateItem(item, empty);
					if (item == null || empty) { setGraphic(null); }
					else { setText(item.getName()); }
				}
			};
		}
	}

	public static interface StringConverterTest { boolean test(Customer customer, String string); }

	public static class StringConverterTestCustomerName implements StringConverterTest
	{
		@Override public boolean test(Customer customer, String name)
		{
			return customer.getName().equals(name);
		}
	}

	public static class StringConverter extends javafx.util.StringConverter<Customer>
	{
		private Collection<Customer> customers;
		private StringConverterTest stringConverterTest;

		public StringConverter(final Collection<Customer> customers, StringConverterTest stringConverterTest)
		{
			this.customers = customers;
			this.stringConverterTest = stringConverterTest;
		}

		@Override public String toString(Customer customer)
		{
			if (customer != null) return customer.getName();
			return "";
		}

		@Override public Customer fromString(String string)
		{
			for (Customer customer : customers)
			{
				if (stringConverterTest.test(customer, string)) return customer;
			}
			return null;
		}

		public void repopulate(Set<Customer> customers)
		{
			this.customers = customers;
		}
	}
}
