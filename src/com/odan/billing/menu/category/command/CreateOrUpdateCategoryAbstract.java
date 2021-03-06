package com.odan.billing.menu.category.command;

import java.util.HashMap;
import java.util.List;

import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.billing.menu.category.model.Category;
import com.odan.common.application.Authentication;
import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;

public abstract class CreateOrUpdateCategoryAbstract extends Command implements ICommand {
	public CreateOrUpdateCategoryAbstract(HashMap<String, Object> data) {
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

		// Validate that product category is not duplicate
		if (isRequestValid) {
			if (this._isDuplicateTitle((String) this.get("title"), ownerId, id)) {
				isRequestValid = false;
				APILogger.add(APILogType.ERROR, "Product category not created. Duplicate category title.");
			}
		}
		this.isValid = isRequestValid;
		return isRequestValid;

	}

	// Check if category is duplicate or not.
	private boolean _isDuplicateTitle(String title, Long ownerId, Long categoryId) {
		boolean duplicate = false;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("title", title);
		queryParams.put("ownerId", ownerId);
		Query q = new Query(queryParams);
		IQueryHandler qHandle = new CategoryQueryHandler();
		List<Object> pcList = qHandle.get(q);
		for (Object pcObj : pcList) {
			Category pc = (Category) pcObj;
			if (pc.getTitle().equals(title) && !pc.getId().equals(categoryId)) {
				duplicate = true;
				break;
			}
		}
		return duplicate;
	}

}