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
		String whereSQL = " WHERE 1=1 ";



		// Apply Filter Params
		if (q.has("status")) {
			whereSQL += " AND status = :status ";
		}



		if (q.has("title")) {
			whereSQL += " AND lower(title) = :title ";
			queryParams.put("title", ((String) q.get("title")).toLowerCase());
		}

		if (q.has("description")) {
			whereSQL += " AND lower(description) = :description ";
			queryParams.put("title", ((String) q.get("description")).toLowerCase());
		}

		if (q.has("id")) {
			whereSQL += " AND id = :id ";
			queryParams.put("id", q.get("id"));
		}

		if (q.has("parentId")) {
			whereSQL += " AND parent_id = :parentId ";
			queryParams.put("parentId", q.get("parentId"));
		}



		List<Object> categories = HibernateUtils.select("FROM Category " + whereSQL, queryParams);
		return categories;

	}

}
