package com.odan.security.user.command;

import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class CreateUser extends CreateOrUpdateUserAbstract implements ICommand {
	public CreateUser(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/user/create";
		this.validate();
	}
}