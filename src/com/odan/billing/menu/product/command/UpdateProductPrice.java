package com.odan.billing.menu.product.command;

import com.odan.common.cqrs.ICommand;

import java.util.HashMap;

public class UpdateProductPrice extends CreateOrUpdateProductAbstract implements ICommand {
	public UpdateProductPrice(HashMap<String, Object> data) {
		super(data);
		//this.validationSchema ="billing/menu/product/update";
		//this.validate();
	}
}