package de.jmda.app.xstaffr.client.fx.edit.candidate;

import java.util.Set;

import de.jmda.app.xstaffr.common.domain.Candidate;
import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

public interface CandidateEditorService
{
	void reload();

	public class CandidatesChanged extends AbstractEvent<Set<Candidate>>
	{
		public CandidatesChanged(Object source, Set<Candidate> data) { super(source, data); }
		public Set<Candidate> getCandidates() { return getData().get(); }
	}

	/**
	 * Used in CDI events to indicate that {@link FilterEditorService} instance is available.
	 *
	 * @see FilterEditorController#initialize()
	 */
	public class ViewServiceAvailable extends AbstractEvent<CandidateEditorService>
	{
		public ViewServiceAvailable(Object source, CandidateEditorService service) { super(source, service); }
		public CandidateEditorService getViewService() { return getData().get(); }
	}
}