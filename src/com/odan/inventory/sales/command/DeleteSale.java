package com.odan.inventory.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class DeleteSale extends Command implements ICommand {
	public DeleteSale(HashMap<String, Object> data) {
		super(data);
	}
}