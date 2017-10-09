package com.odan.billing.menu.product;

import com.odan.billing.menu.category.model.Category;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPriceQueryHandler implements IQueryHandler {

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
            queryParams.put("status", q.get("status"));
        }
        if (q.has("productId")) {
            whereSQL += " AND product_id = :productId ";
            queryParams.put("productId", Parser.convertObjectToLong(q.get("productId")));
        }


        if (q.has("type")) {
            whereSQL += " AND type = :type ";
            queryParams.put("type", Flags.ProductType.valueOf("type"));
        }
        List<Object> products = HibernateUtils.select("FROM ProductPrice " + whereSQL, queryParams);
        return products;
    }

    public ProductPrice getLatestPrice(Long productId) {
        return null;
    }

  /*  public Integer getSalesCountById(Query q) {
        Long productId = Parser.convertObjectToLong(q.get("productId"));
        String sql = "SELECT COUNT(s.id) AS count FROM sales s WHERE s.product_id = :productId";
        HashMap<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("productId", productId);

        List<Map<String, Object>> queryResult = (List<Map<String, Object>>) HibernateUtils.selectSQL(sql, sqlParams);
        Integer count = Parser.convertObjectToInteger(queryResult.get(0).get("count"));
        return count;
    }
*/

}
