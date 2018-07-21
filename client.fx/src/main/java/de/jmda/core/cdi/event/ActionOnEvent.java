package de.jmda.core.cdi.event;

import static de.jmda.core.util.StringUtil.sb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@code ActionOnEvent} is a singleton that primarily provides a solution to the following shortcoming of CDI events:
 * <p>
 * In CDI, if an instance method observes an event (see annotation {@link Observes}), CDI may <b>create a new instance
 * </b> for the respective (observing) class even though there are already CDI managed instances for that class.
 * <p>
 * {@link ActionOnEvent#addInstanceListener(Object, Class)} allows to register {@link Consumer}s together with an event
 * type ({@code Class<?>}). For registered consumers {@code ActionOnEvent} will invoke {@link Consumer#accept(Object)}
 * as soon as an instance of event type becomes available for {@link ActionOnEvent#observe(AbstractEvent)}.
 *
 * @author roger@jmda.de
 */
@ApplicationScoped
public class ActionOnEvent
{
	private final static Logger LOGGER = LogManager.getLogger(ActionOnEvent.class);

	/**
	 * Base class for generic event classes. Makes sure {@link #getSource()} never returns {@code null}.
	 *
	 * @param <T>
	 *
	 * @author roger@jmda.de
	 */
	public abstract static class AbstractEvent<T>
	{
		private Object source;
		private Optional<T> data;
	
		public AbstractEvent(Object source, T data)
		{
			if (source == null) throw new IllegalArgumentException("source must not be null");
			this.source = source;
			this.data = Optional.ofNullable(data);
		}
	
		public AbstractEvent(Object source) { this(source, null); }
	
		public Object getSource() { return source; }
		public Optional<T> getData() { return data; }
	}

	/**
	 * Base class for generic consumers that accept {@code AbstractEvent}s.
	 *
	 * @param <E>
	 *
	 * @author roger@jmda.de
	 */
	public abstract class AbstractAction<E extends AbstractEvent<?>> implements Consumer<E>
	{
		@Override public void accept(E event)
		{
			if (event == null) throw new IllegalArgumentException("event must not be null");
		}
	}

	public static class EventEmitter
	{
		@Inject private Event<AbstractEvent<?>> event;	
		public Event<AbstractEvent<?>> getEvent() { return event; }
	}

	public static EventEmitter getEventEmitter() { return CDI.current().select(EventEmitter.class).get(); }
	public static void fire(AbstractEvent<?> event) { getEventEmitter().getEvent().fire(event); }

	/**
	 * Stores all data necessary to invoke {@link Consumer#accept(Object)} method when events become available.
	 *
	 * @author roger@jmda.de
	 */
	private static class ConsumerInvocationData
	{
		private Consumer<?> consumer;
		private Method acceptMethod;
		private ConsumerInvocationData(Consumer<?> consumer)
		{
			this.consumer = consumer;
			try
			{
				acceptMethod = consumer.getClass().getDeclaredMethod("accept", Object.class);
				acceptMethod.setAccessible(true);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				throw new IllegalStateException("unable to find method accept(Object)", e);
			}
		}
	}

	/** singleton instance */
//	private final static ActionOnEvent INSTANCE = new ActionOnEvent();
//	private final static ActionOnEvent INSTANCE = CDI.current().select(ActionOnEvent.class).get();
/*
	private final static ActionOnEvent INSTANCE;

	static
	{
		INSTANCE = CDI.current().select(ActionOnEvent.class).get();
	}
*/
	private static ActionOnEvent INSTANCE;

	/** Maps {@link AbstractEvent} types to {@link ConsumerInvocationData} instances. */
	private Map<Class<? extends ActionOnEvent.AbstractEvent<?>>, List<ConsumerInvocationData>> consumerInvocationDataByEventType;

	/** singleton constructor */
	private ActionOnEvent() { consumerInvocationDataByEventType = new HashMap<>(); }

	/** @return lazily initialised singleton instance */
	public static ActionOnEvent instance()
	{
		if (INSTANCE == null) INSTANCE =  CDI.current().select(ActionOnEvent.class).get();
		return INSTANCE;
	}

	/**
	 * Stores {@link ConsumerInvocationData} in {@link #consumerInvocationDataByEventType} so that {@link
	 * Consumer#accept(Object)} can be invoked as soon as a {@code eventType} becomes available for {@link
	 * #observe(AbstractEvent)}. If data is already available for {@code eventType} this method does nothing.
	 *
	 * @param eventType
	 * @param consumer
	 *
	 * TODO The following method signature is cleaner and avoids castings for consumer, but it does not work with e.g.
	 *      {@link EventDrivenProcess}
	 *      <code>public <T extends AbstractEvent<?>> void register(Class<T> eventType, Consumer<T> consumer)</code>
	 */
	public void register(Class<? extends AbstractEvent<?>> eventType, Consumer<? extends AbstractEvent<?>> consumer)
	{
		List<ConsumerInvocationData> consumerInvocationDataItems = consumerInvocationDataByEventType.get(eventType);
		if (consumerInvocationDataItems != null)
		{
			Optional<ConsumerInvocationData> optionalConsumerInvocationData =
					consumerInvocationDataItems
							.stream()
							.filter(cid -> cid.consumer == consumer)
							.findFirst();
			if (optionalConsumerInvocationData.isPresent())
			{
				LOGGER.debug("consumer already registered");
			}
			else
			{
				consumerInvocationDataItems.add(new ConsumerInvocationData(consumer));
			}
		}
		else
		{
			consumerInvocationDataItems = new ArrayList<>();
			consumerInvocationDataItems.add(new ConsumerInvocationData(consumer));
			consumerInvocationDataByEventType.put(eventType, consumerInvocationDataItems);
		}
	}

	/**
	 * Removes {@link ConsumerInvocationData} in {@link #consumerInvocationDataByEventType} so that {@link
	 * Consumer#accept(Object)} will no longer be invoked when a {@code eventType} becomes available for {@link
	 * #observe(AbstractEvent)}. If data is not available this method does nothing.
	 *
	 * @param eventType
	 * @param consumer
	 */
	public void unregister(Class<? extends ActionOnEvent.AbstractEvent<?>> eventType, Consumer<? extends ActionOnEvent.AbstractEvent<?>> consumer)
	{
		List<ConsumerInvocationData> consumerInvocationDataItems = consumerInvocationDataByEventType.get(eventType);
		if (consumerInvocationDataItems != null)
		{
			Optional<ConsumerInvocationData> optionalConsumerInvocationData =
					consumerInvocationDataItems
							.stream()
							.filter(cid -> cid.consumer == consumer)
							.findFirst();
			if (optionalConsumerInvocationData.isPresent())
			{
				consumerInvocationDataItems.remove(optionalConsumerInvocationData.get());
			}
			else
			{
				LOGGER.debug("consumer not registered");
			}
		}
	}

	/**
	 * Invokes {@link Consumer#accept(Object)} for all registered consumers.
	 * @param event
	 */
	@SuppressWarnings("unused") private void observe(@Observes AbstractEvent<?> event)
	{
		List<ConsumerInvocationData> consumerInvocationDataItems = consumerInvocationDataByEventType.get(event.getClass());
		if (consumerInvocationDataItems != null)
		{
			consumerInvocationDataItems
					.stream()
					.forEach
					(
							c ->
							{
									try
									{
										c.acceptMethod.invoke(c.consumer, event);
									}
									catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
									{
										LOGGER.error("failure invoking " + c.acceptMethod.getName() + " on " + c.consumer.getClass(), e);
									}
							}
					);
		}
	}

	public String traceInfo()
	{
		StringBuffer result = sb();

		Comparator<Class<?>> byName = (class1, class2) -> class1.getName().compareTo(class2.getName());
		Set<Class<?>> eventTypesByName = new TreeSet<>(byName);
		eventTypesByName.addAll(consumerInvocationDataByEventType.keySet());

		for (Class<?> eventType : eventTypesByName)
		{
			result.append("event type: " + eventType.getName());

			for (ConsumerInvocationData consumerInvocationData : consumerInvocationDataByEventType.get(eventType))
			{
				result.append("\n\tconsumer: " + consumerInvocationData.consumer.getClass());
//						+ "."
//						                 + consumerInvocationData.acceptMethod.toGenericString());
			}

			result.append("\n");
		}

		return result.toString();
	}
}