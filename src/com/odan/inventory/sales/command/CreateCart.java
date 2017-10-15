package com.odan.inventory.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class CreateCart extends Command implements ICommand {
	public CreateCart(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/sales/create";
		//this.validate();
	}
}