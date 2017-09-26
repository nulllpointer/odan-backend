package com.odan.billing.invoice.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateCustomerInvoice extends Command implements ICommand {
	public CreateCustomerInvoice(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/invoice/create_internal";
		this.validate();
	}
	
	public CreateCustomerInvoice(HashMap<String, Object> data, Transaction trx) {
		this(data);
		this.trx = trx;
	}
}