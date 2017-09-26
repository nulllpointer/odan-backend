package com.odan.billing.invoice;

import com.odan.billing.invoice.event.InvoiceAccepted;
import com.odan.billing.invoice.event.InvoiceCreated;
import com.odan.common.cqrs.EventStore;
import com.odan.common.cqrs.IEvent;
import com.odan.common.cqrs.IEventHandler;

public class InvoiceEventHandler implements IEventHandler {

	public static void registerEvents() {
		EventStore.getInstance().registerEventHandler(InvoiceCreated.class, InvoiceEventHandler.class);
		EventStore.getInstance().registerEventHandler(InvoiceAccepted.class, InvoiceEventHandler.class);
	}

	public void handle(IEvent e) {
		if (e instanceof InvoiceCreated) {
			handle((InvoiceCreated) e);
		} else if (e instanceof InvoiceAccepted) {
			handle((InvoiceAccepted) e);
		} 
	}
	
	public void handle(InvoiceCreated e) {
		System.out.println("--INVOICE CREATED EVENT TRIGGERED--");
	}
	
	public void handle(InvoiceAccepted e) {
		System.out.println("--INVOICE ACCEPTED EVENT TRIGGERED--");
	}
	
}
