package com.odan.billing.customer.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class DeleteCustomer extends Command implements ICommand {
	public DeleteCustomer(HashMap<String, Object> data) {
		super(data);
	}
}