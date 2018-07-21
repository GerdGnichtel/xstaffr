package de.jmda.core.cdi.event;

import java.util.List;

public class EventDrivenProcess
{
	private List<EventDrivenProcessStep> steps;

	public EventDrivenProcess(List<EventDrivenProcessStep> steps)
	{
		this.steps = steps;
	}

	public void execute()
	{
		try
		{
			registerResponseEventsAndTheirConsumers();
			steps.forEach(edps -> ActionOnEvent.fire(edps.getEventRequest()));
		}
		finally
		{
			unregisterResponseEventsAndTheirConsumers();
		}
	}

	/** register all event response types together with their consumer */
	private void registerResponseEventsAndTheirConsumers()
	{
		steps.forEach(edps -> ActionOnEvent.instance().register(edps.getEventResponseType(), edps.getEventResponseConsumer()));
	}

	/** unregister all event response types together with their consumer */
	private void unregisterResponseEventsAndTheirConsumers()
	{
		steps.forEach(edps -> ActionOnEvent.instance().unregister(edps.getEventResponseType(), edps.getEventResponseConsumer()));
	}
}