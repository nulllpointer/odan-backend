package com.odan.inventory.purchase.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class CreatePurchase extends Command implements ICommand {
	public CreatePurchase(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/purchase/create";
		//this.validate();
	}
}