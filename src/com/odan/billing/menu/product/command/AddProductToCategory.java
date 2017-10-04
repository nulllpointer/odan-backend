package com.odan.billing.menu.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class AddProductToCategory extends Command implements ICommand {
	public AddProductToCategory(HashMap<String, Object> data) {
		super(data);
	}
	
	public AddProductToCategory(Long productId, Long categoryId, Transaction trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("categoryId", categoryId);
		this.trx = trx;
	}
	
	public AddProductToCategory(Long productId, Long categoryId) {
		this(productId, categoryId, null);
	}
}