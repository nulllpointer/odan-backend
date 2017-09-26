package com.odan.billing.catalog.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odan.billing.catalog.productcategory.model.ProductCategory;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.Parser;

public class ProductQueryHandler implements IQueryHandler {

	@Override
	public Object getById(Long id) throws CommandException {
		ProductCategory category = (ProductCategory) HibernateUtils.get(ProductCategory.class, id);
		return category;

	}

	@Override
	public List<Object> get(Query q) {

		HashMap<String, Object> queryParams = new HashMap<>();
		String where = " WHERE 1=1 ";

		int page = 1;
		int limit = 100;
		int offset = 0;

		// Apply Filter Params
		if (q.has("status")) {
			where += " AND status = :status ";
			queryParams.put("status", Flags.convertInputToStatus(q.get("status")));
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
		if (q.has("accountingCodeId")) {
			where += " AND accountingCodeId = :accountingCodeId ";
			queryParams.put("accountingCodeId", q.get("accountingCodeId"));
		}

		offset = (limit * (page - 1));

		List<Object> products = HibernateUtils.select("FROM Product " + where, queryParams, limit, offset);
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

	// public static HashMap<Long, HashMap<String, Object>>
	// getProductDetailList() {
	// String sql = "SELECT p.id AS product_id, p.title AS product_title,
	// p.status AS product_status, "
	// + "pc.id AS category_id, pc.title AS category_title, "
	// + "pf.id AS feature_id, pf.title AS feature_title, pf.code AS
	// feature_code, "
	// + "prp.id AS RatePlanId, prp.title AS rateplan_title, "
	// + "prpc.id AS rateplan_charge_id, prpc.title AS rateplan_charge_title
	// FROM product p "
	// + "LEFT JOIN product_category_link pcl ON p.id = pcl.product_id "
	// + "LEFT JOIN product_category pc ON pcl.product_category_id = pc.id "
	// + "LEFT JOIN product_feature_link pfl ON p.id = pfl.product_id "
	// + "LEFT JOIN product_feature pf ON pfl.product_feature_id = pf.id "
	// + "LEFT JOIN product_rate_plan_link prpl ON p.id = prpl.product_id "
	// + "LEFT JOIN product_rate_plan prp ON prp.id = prpl.rate_plan_id "
	// + "LEFT JOIN product_rate_plan_charge prpc ON prpc.product_rate_plan_id =
	// prp.id";
	//
	// List<Map<String, Object>> queryResult = (List<Map<String, Object>>)
	// HibernateUtils.selectSQL(sql);
	//
	// HashMap<Long, HashMap<String, Object>> productList = new HashMap<Long,
	// HashMap<String, Object>>();
	//
	// for (Map<String, Object> qRow : queryResult) {
	// Long productId = Parser.convertObjectToLong(qRow.get("product_id"));
	//
	// // ADD PRODUCT
	// HashMap<String, Object> product = null;
	// if (!productList.containsKey(productId)) {
	// product = new HashMap<String, Object>();
	// product.put("id", productId);
	// product.put("title", qRow.get("product_title"));
	// product.put("status", qRow.get("product_status"));
	// product.put("features", new ArrayList<Object>());
	// product.put("categories", new ArrayList<Object>());
	// product.put("rateplans", new ArrayList<Object>());
	// productList.put(productId, product);
	// } else {
	// product = (HashMap<String, Object>) productList.get(productId);
	// }
	//
	// ArrayList<Object> features = (ArrayList<Object>) product.get("features");
	//
	// if (qRow.get("feature_id") != null) {
	// HashMap<String, Object> f = new HashMap<String, Object>();
	// f.put("id", qRow.get("feature_id"));
	// f.put("title", qRow.get("feature_title"));
	// f.put("code", qRow.get("feature_code"));
	//
	// if (!features.contains(f)) {
	// features.add(f);
	// }
	// }
	//
	// ArrayList<Object> categories = (ArrayList<Object>)
	// product.get("categories");
	// if (qRow.get("category_id") != null) {
	// HashMap<String, Object> c = new HashMap<String, Object>();
	// c.put("id", qRow.get("category_id"));
	// c.put("title", qRow.get("category_title"));
	// if (!categories.contains(c)) {
	// categories.add(c);
	// }
	// }
	//
	// ArrayList<Object> rateplans = (ArrayList<Object>)
	// product.get("rateplans");
	// if (qRow.get("rateplan_id") != null) {
	// HashMap<String, Object> rp = new HashMap<String, Object>();
	// rp.put("id", qRow.get("rateplan_id"));
	// rp.put("title", qRow.get("rateplan_title"));
	// if (!rateplans.contains(rp)) {
	// rateplans.add(rp);
	// }
	// }
	//
	// product.put("features", features);
	// product.put("categories", categories);
	// product.put("rateplans", rateplans);
	//
	// productList.put(productId, product);
	//
	// }
	//
	// return productList;
	//
	// }

	// public static List<Map<String, Object>> getProductRatePlanList(Long
	// productId) {
	// Product p = (Product) HibernateUtils.get(Product.class, productId);
	// if (p == null) {
	// APILogger.add(APILogType.ERROR, "Product (" + productId + ") doesn't
	// exists.");
	// return null;
	// }
	//
	// return p.getRatePlans();
	// }
}
