package com.odan.security.user.command;

import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class LoginUser extends CreateOrUpdateUserAbstract implements ICommand {
	public LoginUser(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "security/user/login";
		//this.validate();
	}
}