package com.odan.inventory.purchase.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class DeletePurchase extends Command implements ICommand {
	public DeletePurchase(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/purchase/delete";
		this.validate();
	}
}