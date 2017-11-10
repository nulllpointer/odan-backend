package com.odan.security.user.command;

import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class DeleteUser extends CreateOrUpdateUserAbstract implements ICommand {
	public DeleteUser(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "security/user/update";
		//this.validate();
	}
}