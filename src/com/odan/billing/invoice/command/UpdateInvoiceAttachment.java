package com.odan.billing.invoice.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class UpdateInvoiceAttachment extends Command implements ICommand {
	public UpdateInvoiceAttachment(HashMap<String, Object> data) {
		super(data);
	}
}