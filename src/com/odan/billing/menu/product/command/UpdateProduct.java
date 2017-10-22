package com.odan.billing.menu.product.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class UpdateProduct extends CreateOrUpdateProductAbstract implements ICommand {
	public UpdateProduct(HashMap<String, Object> data) {
		super(data);
		this.validationSchema ="billing/menu/product/update";
		//this.validate();
	}
}