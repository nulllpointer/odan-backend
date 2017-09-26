package com.odan.billing.invoice.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class DeleteInvoiceAttachment extends Command implements ICommand {
	public DeleteInvoiceAttachment(String attachmentId) {
		super();
		this.set("id", attachmentId);
	}
}