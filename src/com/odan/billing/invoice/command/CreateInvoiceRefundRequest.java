package com.odan.billing.invoice.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateInvoiceRefundRequest extends Command implements ICommand {
	public CreateInvoiceRefundRequest(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/invoice/refund_request";
		this.validate();
	}
}