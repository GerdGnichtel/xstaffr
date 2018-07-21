package de.jmda.core.cdi.event;

import java.util.function.Consumer;

import de.jmda.core.cdi.event.ActionOnEvent.AbstractEvent;

/**
 * Data used to define a step in a {@link EventDrivenProcess}.
 *
 * @author roger@jmda.de
 */
public class EventDrivenProcessStep
{
	private AbstractEvent<?> eventRequest;
	private Class<? extends AbstractEvent<?>> eventResponseType;
	private Consumer<? extends AbstractEvent<?>> eventResponseConsumer;

	/**
	 * @param eventRequest will be fired to start the process step, a consumer for the event request is expected to fire another
	 *                     event of type {@code eventResponseType}
	 * @param eventResponseType will be registered together with {@code eventResponseConsumer} in
	 *                          {@link ActionOnEvent#register(Class, Consumer)} to establish a consumer when an event of the type
	 *                          will be fired
	 * @param eventResponseConsumer will be registered together with {@code eventResponseType} in
	 *                              {@link ActionOnEvent#register(Class, Consumer)} to establish a consumer when an event of the
	 *                              type will be fired
	 */
	public EventDrivenProcessStep(
			AbstractEvent<?> eventRequest,
			Class<? extends AbstractEvent<?>> eventResponseType,
			Consumer<? extends AbstractEvent<?>> eventResponseConsumer)
	{
		this.eventRequest = eventRequest;
		this.eventResponseType = eventResponseType;
		this.eventResponseConsumer = eventResponseConsumer;
	}

	public AbstractEvent<?> getEventRequest() { return eventRequest; }
	public Class<? extends AbstractEvent<?>> getEventResponseType() { return eventResponseType; }
	public Consumer<? extends AbstractEvent<?>> getEventResponseConsumer() { return eventResponseConsumer; }
}