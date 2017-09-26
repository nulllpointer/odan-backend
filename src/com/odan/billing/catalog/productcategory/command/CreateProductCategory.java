package com.odan.billing.catalog.productcategory.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class CreateProductCategory extends CreateOrUpdateProductCategoryAbstract implements ICommand {
	public CreateProductCategory(HashMap<String, Object> data) {
		super(data);
		this.validationSchema = "billing/catalog/category/create";
		this.validate();
	}
}