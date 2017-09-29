package com.odan.inventory.purchase;

import com.odan.inventory.purchase.event.PurchaseCreated;
import com.odan.common.cqrs.EventStore;
import com.odan.common.cqrs.IEvent;
import com.odan.common.cqrs.IEventHandler;

public class PurchaseEventHandler implements IEventHandler {

	public static void registerEvents() {
		EventStore.getInstance().registerEventHandler(PurchaseCreated.class, PurchaseEventHandler.class);
	}

	public void handle(IEvent e) {
		if (e instanceof PurchaseCreated) {
			handle((PurchaseCreated) e);
		}
	}
	
	public void handle(PurchaseCreated e) {
		System.out.println("--SALES CREATED EVENT TRIGGERED--");
	}
}
