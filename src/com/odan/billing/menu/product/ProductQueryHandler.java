package com.odan.billing.menu.product;

import java.util.HashMap;
import java.util.List;

import com.odan.billing.menu.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.Parser;

public class ProductQueryHandler implements IQueryHandler {

    @Override
    public Object getById(Long id) throws CommandException {
        Product rd = (Product) HibernateUtils.get(Product.class, id);
        return rd;

    }

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

     /*   if (q.has("categoryId")) {
            whereSQL += " AND categoryId = :categoryId ";
            sqlParams.put("categoryId", q.get("categoryId"));
        }
*/

        if (q.has("category")) {
            whereSQL += " AND category_id = :categoryId ";
            sqlParams.put("categoryId", q.get("categoryId"));
        }


        if (q.has("purchaseSide")) {
            whereSQL += " AND productType = :param1 OR productType=:param2 ";
            sqlParams.put("param1", Flags.ProductType.PURCHASE);
            sqlParams.put("param2", Flags.ProductType.BOTH);
        }
        if (q.has("saleSide")) {
            whereSQL += " AND productType = :param1 OR productType=:param2 ";
            sqlParams.put("param1", Flags.ProductType.SALE  );
            sqlParams.put("param2", Flags.ProductType.BOTH);
        }


        List<Object> products = HibernateUtils.select("FROM Product " + whereSQL, sqlParams);
      //  products.stream().map(c->(Product)c).map(p->=p.getCategory().getId()).

        return products;
    }

}
