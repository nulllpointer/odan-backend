package com.odan.billing.catalog.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class RemoveFeatureFromProduct extends Command implements ICommand {
	public RemoveFeatureFromProduct(HashMap<String, Object> data) {
		super(data);
	}
	
	public RemoveFeatureFromProduct(Long productId, Long featureId, Transaction Trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("featureId", featureId);
		this.trx = trx;
	}

	
	public RemoveFeatureFromProduct(Long productId, Transaction Trx) {
		super();
		this.data.put("productId", productId);
		this.trx = trx;
	}
	
	public RemoveFeatureFromProduct(Long productId, Long featureId) {
		this(productId, featureId, null);
	}
	
	public RemoveFeatureFromProduct(Long productId) {
		this(productId, (Transaction) null);
	}
}