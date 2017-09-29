package com.odan.billing.menu.category.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class UpdateCategory extends CreateOrUpdateCategoryAbstract implements ICommand {
	public UpdateCategory(HashMap<String, Object> data) {
		super(data);
		this.validationSchema ="billing/menu/category/update";
		this.validate();
	}
}