package com.odan.billing.menu.product;

import com.odan.billing.menu.category.model.Category;
import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.Parser;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPriceQueryHandler implements IQueryHandler {

    @Override
    public Object getById(Long id) throws CommandException {
        ProductPrice pp = (ProductPrice) HibernateUtils.get(ProductPrice.class, id);
        return pp;

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

        List<Object> products = HibernateUtils.select("FROM ProductPrice " + whereSQL, queryParams);
        return products;
    }

    public ProductPrice getProductPrice(Long productId, Timestamp txnDate) throws ParseException {


        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("productId", productId);

        List<Object> products = get(new Query(map));
        ProductPrice pp = null;

        for (Object o : products) {
            pp = (ProductPrice) o;
            System.out.println(pp.getStartDate() + "after" + "  " + "  " + txnDate +Parser.convertObjectToTimestamp(pp.getStartDate()) .after(txnDate));


            if (Parser.convertObjectToTimestamp(pp.getStartDate()).before(txnDate) && Parser.convertObjectToTimestamp(pp.getEndDate()).after(txnDate)) {
                return pp;
            }


        }
        return pp;

    }
}
