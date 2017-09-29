package com.odan.billing.menu.category;

import com.odan.billing.menu.category.model.Category;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.utils.Parser;

import java.util.HashMap;
import java.util.List;

public class CategoryQueryHandler implements IQueryHandler {

	@Override
	public Object getById(Long id) throws CommandException {
		Category category = (Category) HibernateUtils.get(Category.class, id);
		return category;

	}

	@Override
	public List<Object> get(Query q) {

		HashMap<String, Object> queryParams = new HashMap<>();
		String where = " WHERE 1=1 ";

		int page = 1;
		int limit = 100;
		int offset = 0;

		if (q.has("page")) {
			page = Parser.convertObjectToInteger(q.get("page"));
		}

		if (q.has("limit")) {
			limit = Parser.convertObjectToInteger(q.get("limit"));
		}

		// Apply Filter Params
		if (q.has("status")) {
			where += " AND status = :status ";
		}

		if (q.has("ownerId")) {
			where += " AND ownerId = :ownerId ";
			queryParams.put("ownerId", q.get("ownerId"));
		}

		if (q.has("title")) {
			where += " AND lower(title) = :title ";
			queryParams.put("title", ((String) q.get("title")).toLowerCase());
		}

		if (q.has("id")) {
			where += " AND id = :id ";
			queryParams.put("id", q.get("id"));
		}

		offset = (limit * (page - 1));

		List<Object> categories = HibernateUtils.select("FROM ProductCategory " + where, queryParams, limit, offset);
		return categories;

	}

}
