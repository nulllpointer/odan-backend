package com.odan.billing.invoice.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateInvoiceWriteoff extends Command implements ICommand {
	public CreateInvoiceWriteoff(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "invoice/writeoff";
		this.validate();		
	}
}