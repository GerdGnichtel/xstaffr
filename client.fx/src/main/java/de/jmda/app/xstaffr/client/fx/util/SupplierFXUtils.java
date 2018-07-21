package de.jmda.app.xstaffr.client.fx.util;

import java.util.Collection;
import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Supplier;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class SupplierFXUtils
{
	public static class CellFactory implements Callback<ListView<Supplier>, ListCell<Supplier>>
	{
		@Override public ListCell<Supplier> call(ListView<Supplier> param)
		{
			return new ListCell<Supplier>()
			{
				@Override protected void updateItem(Supplier item, boolean empty)
				{
					super.updateItem(item, empty);
					if (item == null || empty) { setGraphic(null); }
					else { setText(item.getName()); }
				}
			};
		}
	}

	public static interface StringConverterTest { boolean test(Supplier supplier, String string); }

	public static class StringConverterTestSupplierName implements StringConverterTest
	{
		@Override public boolean test(Supplier supplier, String name)
		{
			return supplier.getName().equals(name);
		}
	}

	public static class StringConverter extends javafx.util.StringConverter<Supplier>
	{
		private Collection<Supplier> suppliers;
		private StringConverterTest stringConverterTest;

		public StringConverter(final Collection<Supplier> suppliers, StringConverterTest stringConverterTest)
		{
			this.suppliers = suppliers;
			this.stringConverterTest = stringConverterTest;
		}

		@Override public String toString(Supplier supplier)
		{
			if (supplier != null) return supplier.getName();
			return "";
		}

		@Override public Supplier fromString(String string)
		{
			for (Supplier supplier : suppliers)
			{
				if (stringConverterTest.test(supplier, string)) return supplier;
			}
			return null;
		}

		public void repopulate(Set<Supplier> suppliers)
		{
			this.suppliers = suppliers;
		}
	}
}