package com.odan.billing.menu.category.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class CreateCategory extends CreateOrUpdateCategoryAbstract implements ICommand {
	public CreateCategory(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/menu/category/create";
		//this.validate();
	}
}