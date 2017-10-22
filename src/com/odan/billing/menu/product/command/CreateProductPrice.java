package com.odan.billing.menu.product.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class CreateProductPrice extends Command implements ICommand {
	public CreateProductPrice(HashMap<String, Object> data) {
		super(data);
		//this.validationSchema = "billing/menu/product/create";
		//this.validate();
	}
}