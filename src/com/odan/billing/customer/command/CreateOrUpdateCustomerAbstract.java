package com.odan.billing.customer.command;

import java.util.HashMap;
import java.util.List;

import com.odan.billing.customer.CustomerQueryHandler;
import com.odan.billing.customer.model.Customer;
import com.odan.common.application.Authentication;
import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;

public abstract class CreateOrUpdateCustomerAbstract extends Command implements ICommand {
	public CreateOrUpdateCustomerAbstract(HashMap<String, Object> data) {
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

		// Validate that user is not duplicate
		if (isRequestValid) {
			if (this._isDuplicateEmail((String) this.get("email"), ownerId, id)) {
				isRequestValid = false;
				APILogger.add(APILogType.ERROR, "Customer not created. Duplicate customer email.");
			} else if (this.has("userId")
					&& this._isDuplicateUserId(Parser.convertObjectToLong(this.get("userId")), ownerId, id)) {
				isRequestValid = false;
				APILogger.add(APILogType.ERROR, "Customer not created. Duplicate user id.");
			}
		}
		this.isValid = isRequestValid;
		return isRequestValid;

	}

	// Check if user id is duplicate or not.
	private boolean _isDuplicateUserId(Long userId, Long ownerId, Long id) {
		boolean duplicate = false;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("ownerId", ownerId);
		queryParams.put("userId", userId);
		Query q = new Query(queryParams);
		IQueryHandler qHandle = new CustomerQueryHandler();
		List<Object> cList = qHandle.get(q);
		for (Object cObj : cList) {
			Customer c = (Customer) cObj;
			if ((c.getUserId() != null && c.getUserId().equals(userId)) && !c.getId().equals(id)) {
				duplicate = true;
				break;
			}
		}
		return duplicate;
	}

	// Check if user email is duplicate or not.
	private boolean _isDuplicateEmail(String email, Long ownerId, Long id) {
		boolean duplicate = false;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("ownerId", ownerId);
		queryParams.put("email", email);
		Query q = new Query(queryParams);
		IQueryHandler qHandle = new CustomerQueryHandler();
		List<Object> cList = qHandle.get(q);
		for (Object cObj : cList) {
			Customer c = (Customer) cObj;
			if ((c.getEmail() != null && c.getEmail().equals(email)) && !c.getId().equals(id)) {
				duplicate = true;
				break;
			}
		}
		return duplicate;
	}

}