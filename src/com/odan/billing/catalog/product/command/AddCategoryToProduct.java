package com.odan.billing.catalog.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class AddCategoryToProduct extends Command implements ICommand {
	public AddCategoryToProduct(HashMap<String, Object> data) {
		super(data);
	}
	
	public AddCategoryToProduct(Long productId, Long categoryId, Transaction trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("categoryId", categoryId);
		this.trx = trx;
	}
	
	public AddCategoryToProduct(Long productId, Long categoryId) {
		this(productId, categoryId, null);
	}
}