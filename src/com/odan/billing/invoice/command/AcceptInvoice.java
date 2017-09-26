package com.odan.billing.invoice.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class AcceptInvoice extends Command implements ICommand {
	public AcceptInvoice(HashMap<String, Object> data) {
		super(data);
		this.validationSchema ="billing/invoice/accept_internal";
		this.validate();
	}
}