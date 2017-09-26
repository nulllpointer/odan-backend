package com.odan.billing.catalog.product.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class DeleteProduct extends Command implements ICommand {
	public DeleteProduct(HashMap<String, Object> data) {
		
	}
}