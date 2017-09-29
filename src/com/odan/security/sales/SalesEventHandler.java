package com.odan.security.sales;

import com.odan.common.cqrs.EventStore;
import com.odan.common.cqrs.IEvent;
import com.odan.common.cqrs.IEventHandler;
import com.odan.security.sales.event.SalesCreated;

public class SalesEventHandler implements IEventHandler {

	public static void registerEvents() {
		EventStore.getInstance().registerEventHandler(SalesCreated.class, SalesEventHandler.class);
	}

	public void handle(IEvent e) {
		if (e instanceof SalesCreated) {
			handle((SalesCreated) e);
		}
	}
	
	public void handle(SalesCreated e) {
		System.out.println("--SALES CREATED EVENT TRIGGERED--");
	}
}
