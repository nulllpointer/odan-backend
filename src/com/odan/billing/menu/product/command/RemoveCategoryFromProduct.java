package com.odan.billing.menu.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class RemoveCategoryFromProduct extends Command implements ICommand {
	public RemoveCategoryFromProduct(HashMap<String, Object> data) {
		super(data);
	}

	public RemoveCategoryFromProduct(Long productId, Long categoryId, Transaction trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("categoryId", categoryId);
		this.trx = trx;
	}
	
	public RemoveCategoryFromProduct(Long productId, Transaction trx) {
		super();
		this.data.put("productId", productId);
		this.trx = trx;
	}
	
	public RemoveCategoryFromProduct(Long productId, Long categoryId) {
		this(productId, categoryId, null);
	}
	
	public RemoveCategoryFromProduct(Long productId) {
		this(productId, (Transaction) null);
	}
}