package com.odan.inventory.purchase.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class UpdatePurchase extends Command implements ICommand {
	public UpdatePurchase(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/purchase/update";
		this.validate();
	}
}