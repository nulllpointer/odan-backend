package com.odan.billing.user.command;

import java.util.HashMap;
import java.util.List;

import com.odan.billing.user.UserQueryHandler;
import com.odan.billing.user.model.User;
import com.odan.common.application.Authentication;
import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;

public abstract class CreateOrUpdateUserAbstract extends Command implements ICommand {
	public CreateOrUpdateUserAbstract(HashMap<String, Object> data) {
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
			if (this.getClass().getName() == CreateUser.class.getName()) {
				if (this._isDuplicateEmail((String) this.get("email"), null)) {
					isRequestValid = false;
					APILogger.add(APILogType.ERROR, "User not created. Duplicate user email.");
				}
				else if (this._isDuplicateUserId((String) this.get("userId"), null)) {
					isRequestValid = false;
					APILogger.add(APILogType.ERROR, "User not created. Duplicate user id.");
				}
			} else if (this.getClass().getName() == UpdateUser.class.getName()) {
				if (this._isDuplicateEmail((String) this.get("email"), id)) {
					isRequestValid = false;
					APILogger.add(APILogType.ERROR, "User not updated. Duplicate user email.");
				} else if (this._isDuplicateUserId((String) this.get("userId"), id)) {
					isRequestValid = false;
					APILogger.add(APILogType.ERROR, "User not updated. Duplicate user id.");
				}
			}
		}
		this.isValid = isRequestValid;
		return isRequestValid;

	}

	// Check if user email is duplicate or not.
	private boolean _isDuplicateEmail(String email, Long id) {
		boolean duplicate = false;
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("email", email);
		Query q = new Query(queryParams);
		IQueryHandler qHandle = new UserQueryHandler();
		List<Object> uList = qHandle.get(q);
		for (Object uObj : uList) {
			User u = (User) uObj;
			if ((u.getEmail() != null && u.getEmail().equals(email)) && !u.getId().equals(id)) {
				duplicate = true;
				break;
			}
		}
		return duplicate;
	}

	// Check if user id exists or not.
	private boolean _isDuplicateUserId(String userId, Long id) {
		boolean duplicate = false;
		if (userId != null) {
			HashMap<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("userId", userId);
			Query q = new Query(queryParams);
			IQueryHandler qHandle = new UserQueryHandler();
			List<Object> uList = qHandle.get(q);
			for (Object uObj : uList) {
				User u = (User) uObj;
				if ((u.getExternalId() != null && u.getExternalId().equals(userId)) && !u.getId().equals(id)) {
					duplicate = true;
					break;
				}
			}
		}
		return duplicate;
	}

}