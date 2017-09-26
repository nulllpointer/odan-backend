package com.odan.billing.invoice.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateRecurringInvoice extends Command implements ICommand {
	public CreateRecurringInvoice(HashMap<String, Object> data) {
		super(data);	
	}
	
	public CreateRecurringInvoice(HashMap<String, Object> data, Transaction trx) {
		this(data);
		this.trx = trx;
	}
	
	public CreateRecurringInvoice(Object invoice, Transaction trx) {
		super(null, trx);
		this.set("invoice", invoice);
		this.validate();
	}
	
	public CreateRecurringInvoice(Object invoice) {
		super();
		this.set("invoice", invoice);
		this.validate();
	}
}