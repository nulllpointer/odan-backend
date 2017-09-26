package com.odan.billing.invoice.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateInvoiceRefundResponse extends Command implements ICommand {
	public CreateInvoiceRefundResponse(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/invoice/refund_approve";
		this.validate();
	}
}