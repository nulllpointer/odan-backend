package com.odan.security.user;

import java.util.HashMap;
import java.util.List;

import com.odan.security.user.model.User;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.utils.Parser;

public class UserQueryHandler implements IQueryHandler {

	@Override
	public Object getById(Long id) throws CommandException {
		User rd = (User) HibernateUtils.get(User.class, id);
		return rd;

	}

	/*public User getByExternalId(String externalId) {
		Query q = new Query();
		q.set("externalId", externalId);
		List<Object> userList = this.get(q);
		User c = null;
		if (userList.size() > 0) {
			c = (User) userList.get(0);
		}
		return c;

	}*/

	@Override
	public List<Object> get(Query q) {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		String whereSQL = " WHERE 1=1 ";



		if (q.has("id")) {
			whereSQL += " AND id = :id ";
			sqlParams.put("id", Parser.convertObjectToLong(q.get("id")));
		}

		if (q.has("firstName")) {
			whereSQL += " AND lower(firstName) = :firstName ";
			sqlParams.put("firstName", ((String) q.get("firstName")).toLowerCase());
		}

		if (q.has("lastName")) {
			whereSQL += " AND lower(lastName) = :lastName ";
			sqlParams.put("lastName", ((String) q.get("lastName")).toLowerCase());
		}

		if (q.has("email")) {
			whereSQL += " AND lower(email) = :email ";
			sqlParams.put("email", ((String) q.get("email")).toLowerCase());
		}

		if (q.has("city")) {
			whereSQL += " AND lower(city) = :city ";
			sqlParams.put("city", ((String) q.get("city")).toLowerCase());
		}

		if (q.has("state")) {
			whereSQL += " AND lower(state) = :state ";
			sqlParams.put("state", ((String) q.get("state")).toLowerCase());
		}

		if (q.has("country")) {
			whereSQL += " AND lower(country) = :country ";
			sqlParams.put("country", ((String) q.get("country")).toLowerCase());
		}

		if (q.has("externalId")) {
			whereSQL += " AND lower(externalId) = :externalId ";
			sqlParams.put("externalId", ((String) q.get("externalId")).toLowerCase());
		}


		List<Object> users = HibernateUtils.select("FROM User " + whereSQL, sqlParams);
		return users;
	}

}
