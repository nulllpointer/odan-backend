package com.odan.billing.menu.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odan.billing.menu.category.model.Category;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.Parser;

public class ProductQueryHandler implements IQueryHandler {

    @Override
    public Object getById(Long id) throws CommandException {
        Category category = (Category) HibernateUtils.get(Category.class, id);
        return category;

    }

    @Override
    public List<Object> get(Query q) {

        HashMap<String, Object> queryParams = new HashMap<>();
        String where = " WHERE 1=1 ";

        // Apply Filter Params
        if (q.has("status")) {
            where += " AND status = :status ";
            queryParams.put("status", q.get("status"));
        }
        if (q.has("title")) {
            where += " AND lower(title) = :title ";
            queryParams.put("title", ((String) q.get("title")).toLowerCase());
        }
        List<Object> products = HibernateUtils.select("FROM Product " + where, queryParams);
        return products;
    }

    public Integer getSalesCountById(Query q) {
        Long productId = Parser.convertObjectToLong(q.get("productId"));
        String sql = "SELECT COUNT(s.id) AS count FROM sales s WHERE s.product_id = :productId";
        HashMap<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("productId", productId);

        List<Map<String, Object>> queryResult = (List<Map<String, Object>>) HibernateUtils.selectSQL(sql, sqlParams);
        Integer count = Parser.convertObjectToInteger(queryResult.get(0).get("count"));
        return count;
    }


}
