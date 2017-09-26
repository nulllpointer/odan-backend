package com.odan.billing.invoice.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateSalesInvoice extends Command implements ICommand {
	public CreateSalesInvoice(HashMap<String, Object> data) {
		super(data);
		this.validate();
	}
	
	public CreateSalesInvoice(HashMap<String, Object> data, Transaction trx) {
		this(data);
		this.trx = trx;
		this.validate();
	}
	
	public CreateSalesInvoice(Object sales, Transaction trx) {
		super(null, trx);
		this.set("sales", sales);
		this.validate();
	}
	
	public CreateSalesInvoice(Object sales) {
		super();
		this.set("sales", sales);
		this.validate();
	}
}