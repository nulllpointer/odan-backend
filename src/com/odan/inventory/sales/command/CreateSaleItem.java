/*
package com.odan.inventory.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import org.hibernate.Transaction;

import java.util.HashMap;

public class CreateSaleItem extends Command implements ICommand {
	public CreateSaleItem(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/sales/create";
		this.validate();
	}
	public CreateSaleItem(HashMap<String, Object> data, Transaction trx) {
		super(data, trx);
	}
}*/
