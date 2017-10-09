package com.odan.inventory.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class UpdateSale extends Command implements ICommand {
	public UpdateSale(HashMap<String, Object> data) {
		super(data);
		//this.validate();
	}
}