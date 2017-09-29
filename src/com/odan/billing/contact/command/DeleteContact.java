package com.odan.billing.contact.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class DeleteContact extends Command implements ICommand {
	public DeleteContact(HashMap<String, Object> data) {
		super(data);
	}
}