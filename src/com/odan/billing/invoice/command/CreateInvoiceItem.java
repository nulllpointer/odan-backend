package com.odan.billing.invoice.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateInvoiceItem extends Command implements ICommand {
	public CreateInvoiceItem(HashMap<String, Object> data) {
		super(data);
	}

	public CreateInvoiceItem(HashMap<String, Object> data, Transaction trx) {
		super(data, trx);
	}
}
