package com.odan.billing.sales.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateSales extends Command implements ICommand {
	public CreateSales(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/sales/create";
		this.validate();
	}
}