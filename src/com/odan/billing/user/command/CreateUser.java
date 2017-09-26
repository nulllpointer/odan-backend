package com.odan.billing.user.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class CreateUser extends CreateOrUpdateUserAbstract implements ICommand {
	public CreateUser(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/user/create";
		this.validate();
	}
}