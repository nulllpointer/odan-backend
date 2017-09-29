package com.odan.security.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class CreateSales extends Command implements ICommand {
	public CreateSales(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/sales/create";
		this.validate();
	}
}