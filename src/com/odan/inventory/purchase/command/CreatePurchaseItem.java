package com.odan.inventory.purchase.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import org.hibernate.Transaction;

import java.util.HashMap;

public class CreatePurchaseItem extends Command implements ICommand {
	public CreatePurchaseItem(HashMap<String, Object> data,Transaction trx) {
		super(data,trx);
		this.validationSchema = "billing/sales/create";
		//this.validate();
	}
}