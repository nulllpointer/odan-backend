package com.odan.billing.invoice.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class UpdateInvoiceDueStatus extends Command implements ICommand {
	public UpdateInvoiceDueStatus(HashMap<String, Object> data) {
		super(data);	
		this.validate();
	}
	
	public UpdateInvoiceDueStatus(HashMap<String, Object> data, Transaction trx) {
		this(data);
		this.trx = trx;
		this.validate();
	}
	
	public UpdateInvoiceDueStatus(Object invoice, Transaction trx) {
		super(null, trx);
		this.set("invoice", invoice);
		this.validate();
	}
	
	public UpdateInvoiceDueStatus(Object invoice) {
		super();
		this.set("invoice", invoice);
		this.validate();
	}
}