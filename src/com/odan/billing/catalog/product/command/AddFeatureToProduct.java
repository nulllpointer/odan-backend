package com.odan.billing.catalog.product.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class AddFeatureToProduct extends Command implements ICommand {
	public AddFeatureToProduct(HashMap<String, Object> data) {
		super(data);
	}
	
	public AddFeatureToProduct(Long productId, Long featureId, Transaction trx) {
		super();
		this.data.put("productId", productId);
		this.data.put("featureId", featureId);
		this.trx = trx;
	}
	
	public AddFeatureToProduct(Long productId, Long featureId) {
		this(productId, featureId, null);
	}
}