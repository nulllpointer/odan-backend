package com.odan.inventory.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class CreateSale extends Command implements ICommand {
	public CreateSale(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/sales/create";
		//this.validate();
	}
}