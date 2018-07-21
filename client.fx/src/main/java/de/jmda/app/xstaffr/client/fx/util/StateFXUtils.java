package de.jmda.app.xstaffr.client.fx.util;

import java.util.Collection;

import de.jmda.app.xstaffr.common.domain.SearchRequest.State;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class StateFXUtils
{
	public static class CellFactory implements Callback<ListView<State>, ListCell<State>>
	{
		@Override public ListCell<State> call(ListView<State> param)
		{
			return new ListCell<State>()
			{
				@Override protected void updateItem(State item, boolean empty)
				{
					super.updateItem(item, empty);
					if (item == null || empty) { setGraphic(null); }
					else { setText(item.toString()); }
				}
			};
		}
	}

	public static interface StringConverterTest { boolean test(State state, String string); }

	public static class StringConverterTestState implements StringConverterTest
	{
		@Override public boolean test(State state, String name)
		{
			return state.toString().equals(name);
		}
	}

	public static class StringConverter extends javafx.util.StringConverter<State>
	{
		private Collection<State> states;
		private StringConverterTest stringConverterTest;

		public StringConverter(final Collection<State> states, StringConverterTest stringConverterTest)
		{
			this.states = states;
			this.stringConverterTest = stringConverterTest;
		}

		@Override public String toString(State state)
		{
			if (state != null) return state.toString();
			return "";
		}

		@Override public State fromString(String string)
		{
			for (State state : states)
			{
				if (stringConverterTest.test(state, string)) return state;
			}
			return null;
		}
	}
}