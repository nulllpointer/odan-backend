package com.odan.billing.customer.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class CreateCustomer extends CreateOrUpdateCustomerAbstract implements ICommand {
	public CreateCustomer(HashMap<String, Object> data) {
		super(data);
//		this.validate();
	}
}