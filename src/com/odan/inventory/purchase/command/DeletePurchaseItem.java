package com.odan.inventory.purchase.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class DeletePurchaseItem extends Command implements ICommand {
	public DeletePurchaseItem(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/sales/create";
		this.validate();
	}
}