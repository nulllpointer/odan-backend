package com.odan.security.user.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class UpdateUser extends CreateOrUpdateUserAbstract implements ICommand {
	public UpdateUser(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "security/user/update";
		this.validate();
	}
}