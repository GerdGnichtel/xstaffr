package de.jmda.app.xstaffr.client.fx.util;

import java.util.Collection;

import de.jmda.app.xstaffr.common.domain.Candidate;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class CandidateFXUtils
{
	public static class CellFactory implements Callback<ListView<Candidate>, ListCell<Candidate>>
	{
		@Override public ListCell<Candidate> call(ListView<Candidate> param)
		{
			return new ListCell<Candidate>()
			{
				@Override protected void updateItem(Candidate item, boolean empty)
				{
					super.updateItem(item, empty);
					if (item == null || empty) { setGraphic(null); }
					else { setText(item.getUniqueName()); }
				}
			};
		}
	}

	public static interface StringConverterTest { boolean test(Candidate candidate, String string); }

	public static class StringConverterTestCandidateUniqueName implements StringConverterTest
	{
		@Override public boolean test(Candidate candidate, String uniqueName)
		{
			return candidate.getUniqueName().equals(uniqueName);
		}
	}

	public static class StringConverter extends javafx.util.StringConverter<Candidate>
	{
		private Collection<Candidate> candidates;
		private StringConverterTest stringConverterTest;

		public StringConverter(final Collection<Candidate> candidates, StringConverterTest stringConverterTest)
		{
			this.candidates = candidates;
			this.stringConverterTest = stringConverterTest;
		}

		@Override public String toString(Candidate candidate)
		{
			if (candidate != null) return candidate.getUniqueName();
			return "";
		}

		@Override public Candidate fromString(String string)
		{
			for (Candidate candidate : candidates)
			{
				if (stringConverterTest.test(candidate, string)) return candidate;
			}
			return null;
		}

		public void repopulate(Collection<Candidate> candidates)
		{
			this.candidates = candidates;
		}
	}
}
