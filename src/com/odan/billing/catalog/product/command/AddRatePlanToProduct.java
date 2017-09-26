package com.odan.billing.catalog.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class AddRatePlanToProduct extends Command implements ICommand {
	public AddRatePlanToProduct(HashMap<String, Object> data) {
		super(data);
	}
	
	public AddRatePlanToProduct(Long productId, Long rateplanId, Transaction trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("rateplanId", rateplanId);
		this.trx = trx;
	}
	
	public AddRatePlanToProduct(Long productId, Long rateplanId) {
		this(productId, rateplanId, null);
	}
}