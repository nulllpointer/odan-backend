package com.odan.billing.invoice.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateInvoiceAttachment extends Command implements ICommand {
	public CreateInvoiceAttachment(String entityId, String filename) {
		super();
		this.set("entityId", entityId);
		this.set("filename", filename);
	}
}