package com.odan.billing.menu.product.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class DeleteProductPrice  extends Command implements ICommand {
	public DeleteProductPrice(HashMap<String, Object> data) {
		super(data);
		//this.validationSchema ="billing/menu/product/update";
		//this.validate();
	}
}