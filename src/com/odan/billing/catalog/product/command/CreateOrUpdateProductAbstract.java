package com.odan.billing.catalog.product.command;

import java.util.HashMap;
import java.util.List;

import com.odan.billing.catalog.product.ProductQueryHandler;
import com.odan.billing.catalog.product.model.Product;
import com.odan.common.application.Authentication;
import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;

public abstract class CreateOrUpdateProductAbstract extends Command implements ICommand {
	public CreateOrUpdateProductAbstract(HashMap<String, Object> data) {
		super(data);
	}

	@Override
	public boolean validate() {
		boolean isRequestValid = super.validate();
		Long id = Parser.convertObjectToLong(this.get("id"));
		Long ownerId = Parser.convertObjectToLong(this.get("ownerId"));
		if (ownerId == null) {
			ownerId = Authentication.getUserId();
		}

		// Validate that product is not duplicate
		if (isRequestValid) {
			if (this._isDuplicateTitle((String) this.get("title"), ownerId, id)) {
				isRequestValid = false;
				APILogger.add(APILogType.ERROR, "Product not created. Duplicate product title.");
			} 
		}
		this.isValid = isRequestValid;
		return isRequestValid;

	}

	// Check if product is duplicate or not.
	private boolean _isDuplicateTitle(String title, Long ownerId, Long productId) {
		boolean duplicate = false;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("title", title);
		queryParams.put("ownerId", ownerId);
		Query q = new Query(queryParams);
		IQueryHandler qHandle = new ProductQueryHandler();
		List<Object> pList = qHandle.get(q);
		for (Object pObj : pList) {
			Product p = (Product) pObj;
			if (p.getTitle().equals(title) && !p.getId().equals(productId)) {
				duplicate = true;
				break;
			}
		}
		return duplicate;
	}

}