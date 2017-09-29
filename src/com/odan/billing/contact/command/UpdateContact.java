package com.odan.billing.contact.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class UpdateContact extends CreateOrUpdateContactAbstract implements ICommand {
	public UpdateContact(HashMap<String, Object> data) {
		super(data);
		//this.validate();
	}
}