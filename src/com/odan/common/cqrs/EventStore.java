package com.odan.common.cqrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventStore {
	private HashMap<Class<?>, List<Class<?>>> registry;
	private static EventStore instance = null;

	public EventStore() {
		registry = new HashMap<Class<?>, List<Class<?>>>();
	}

	public static EventStore getInstance() {
		if (EventStore.instance == null) {
			EventStore.instance = new EventStore();
		}
		return EventStore.instance;
	}

	public void registerEventHandler(Class<?> event, Class<?> handler) {
		List<Class<?>> list = (List<Class<?>>) this.registry.get(event);
		if (list == null) {
			list = new ArrayList<Class<?>>();
		}

		if (!list.contains(handler)) {
			list.add(handler);
		}

		this.registry.put(event, list);
	}

	public void unregisterEventHanlder(Class<?> event, Class<?> handler) {
		List<Class<?>> list = (List<Class<?>>) this.registry.get(event);
		if (list == null) {
			list = new ArrayList<Class<?>>();
		}

		list.remove(handler);

		this.registry.put(event, list);
	}

	public List<Class<?>> getEventHandler(IEvent event) {
		Class eventClass = event.getClass();
		List<Class<?>> eventHandlerClassList = null;

		if (registry.containsKey(eventClass)) {
			eventHandlerClassList = registry.get(eventClass);
		}

		return eventHandlerClassList;
	}

}
