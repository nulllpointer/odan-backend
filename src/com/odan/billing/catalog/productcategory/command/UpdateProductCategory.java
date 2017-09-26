package com.odan.billing.catalog.productcategory.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class UpdateProductCategory extends CreateOrUpdateProductCategoryAbstract implements ICommand {
	public UpdateProductCategory(HashMap<String, Object> data) {
		super(data);
		this.validationSchema ="billing/catalog/category/update";
		this.validate();
	}
}