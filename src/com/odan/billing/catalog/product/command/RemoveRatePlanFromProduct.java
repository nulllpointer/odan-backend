package com.odan.billing.catalog.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class RemoveRatePlanFromProduct extends Command implements ICommand {
	public RemoveRatePlanFromProduct(HashMap<String, Object> data) {
		super(data);
	}
	
	public RemoveRatePlanFromProduct(Long productId, Long rateplanId, Transaction Trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("rateplanId", rateplanId);
		this.trx = trx;
	}
	
	public RemoveRatePlanFromProduct(Long productId, Transaction Trx) {
		super();
		this.data.put("productId", productId);
		this.trx = trx;
	}
	
	public RemoveRatePlanFromProduct(Long productId, Long rateplanId) {
		this(productId, rateplanId, null);
	}
	
	public RemoveRatePlanFromProduct(Long productId) {
		this(productId, (Transaction) null);
	}
}