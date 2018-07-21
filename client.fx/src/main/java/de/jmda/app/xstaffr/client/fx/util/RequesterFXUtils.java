package de.jmda.app.xstaffr.client.fx.util;

import java.util.Collection;
import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Requester;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class RequesterFXUtils
{
	public static class CellFactory implements Callback<ListView<Requester>, ListCell<Requester>>
	{
		@Override public ListCell<Requester> call(ListView<Requester> param)
		{
			return new ListCell<Requester>()
			{
				@Override protected void updateItem(Requester item, boolean empty)
				{
					super.updateItem(item, empty);
					if (item == null || empty) { setGraphic(null); }
					else { setText(item.getName()); }
				}
			};
		}
	}

	public static interface StringConverterTest { boolean test(Requester requester, String string); }

	public static class StringConverterTestRequesterName implements StringConverterTest
	{
		@Override public boolean test(Requester requester, String name)
		{
			return requester.getName().equals(name);
		}
	}

	public static class StringConverter extends javafx.util.StringConverter<Requester>
	{
		private Collection<Requester> requesters;
		private StringConverterTest stringConverterTest;

		public StringConverter(final Collection<Requester> requesters, StringConverterTest stringConverterTest)
		{
			this.requesters = requesters;
			this.stringConverterTest = stringConverterTest;
		}

		@Override public String toString(Requester requester)
		{
			if (requester != null) return requester.getName();
			return "";
		}

		@Override public Requester fromString(String string)
		{
			for (Requester requester : requesters)
			{
				if (stringConverterTest.test(requester, string)) return requester;
			}
			return null;
		}

		public void repopulate(Set<Requester> requesters)
		{
			this.requesters = requesters;
		}
	}
}
