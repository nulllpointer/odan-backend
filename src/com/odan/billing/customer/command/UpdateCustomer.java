package com.odan.billing.customer.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class UpdateCustomer extends CreateOrUpdateCustomerAbstract implements ICommand {
	public UpdateCustomer(HashMap<String, Object> data) {
		super(data);
		//this.validate();
	}
}