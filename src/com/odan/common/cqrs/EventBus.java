package com.odan.common.cqrs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EventBus {
	private Queue<IEvent> eventQueue;
	private static EventBus instance = null;
	private int activeProcessCount = 0;

	public EventBus() {
		eventQueue = new LinkedList<IEvent>();
	}

	public static EventBus getInstance() {
		if (EventBus.instance == null) {
			EventBus.instance = new EventBus();
		}
		return EventBus.instance;
	}

	public void enqueue(IEvent event) {
		this.eventQueue.add(event);
	}

	public IEvent dequeue() {
		return this.eventQueue.poll();
	}

	public boolean process(IEvent event) {
		boolean isProcessed = false;
		this.activeProcessCount++;

		List<Class<?>> eventHandlerList = EventStore.getInstance().getEventHandler(event);

		if (eventHandlerList != null) {
			try {
				for (Class<?> eventHandlerClass : eventHandlerList) {
					IEventHandler eventHandler = (IEventHandler) eventHandlerClass.getConstructor().newInstance();

					if (eventHandler != null) {
						eventHandler.handle(event);
					} else {
						System.out.println("..EventHandler Not Defined");
					}
				}
				isProcessed = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("..EventHandler Registry Not Defined for " + event.getClass().getName());
		}
		this.activeProcessCount--;

		return isProcessed;
	}

	public int getActiveProcessCount() {
		return activeProcessCount;
	}

}
