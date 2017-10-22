package com.odan.inventory.sales;

import com.odan.common.application.CommandException;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.inventory.sales.model.Cart;
import com.odan.inventory.sales.model.CartItem;

import java.util.HashMap;
import java.util.List;

public class CartItemQueryHandler implements IQueryHandler {

	@Override
	public Object getById(Long id) throws CommandException {
		CartItem s = (CartItem) HibernateUtils.get(CartItem.class, id);
		return s;
	}

	@Override
	public List<Object> get(Query q) {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		String whereSQL = " WHERE 1=1 ";


		// Apply Filter Params


		if (q.has("cartId")) {
			whereSQL += " AND cart_id = :cartId ";
			sqlParams.put("cartId", q.get("cartId"));
		}
		if (q.has("customerId")) {
			whereSQL += " AND customerId = :customerId ";
			sqlParams.put("customerId", q.get("customerId"));
		}
		if (q.has("is_invoiced")) {
			whereSQL += " AND is_invoiced = :is_invoiced ";
			sqlParams.put("is_invoiced", q.get("is_invoiced"));
		}
		if (q.has("status")) {
			whereSQL += " AND status = :status ";
			sqlParams.put("status", q.get("status"));
		}

		// sales start date
		if (q.has("salesStartDateStart") ^ q.has("salesStartDateEnd")) {
			APILogger.add(APILogType.WARNING, "Start Sales start or end date missing.");
		} else if (q.has("salesStartDateStart")) {
			whereSQL += " AND DATE(startDate) BETWEEN DATE(" + HibernateUtils.s((String) q.get("salesStartDateStart"))
					+ ") AND DATE(" + HibernateUtils.s((String) q.get("salesStartDateEnd")) + ") ";
		}

		if (q.has("salesStartDateAfter")) {
			whereSQL += " AND DATE(startDate) > DATE(" + HibernateUtils.s((String) q.get("salesStartDateAfter")) + ") ";
		}
		if (q.has("salesStartDateBefore")) {
			whereSQL += " AND DATE(startDate) < DATE(" + HibernateUtils.s((String) q.get("salesStartDateBefore"))
					+ ") ";
		}

		// sales end date
		if (q.has("salesEndDateStart") ^ q.has("salesEndDateEnd")) {
			APILogger.add(APILogType.WARNING, "End Sales start or end date missing.");
		} else if (q.has("salesEndDateStart")) {
			whereSQL += " AND DATE(endDate) BETWEEN DATE(" + HibernateUtils.s((String) q.get("salesEndDateStart"))
					+ ") AND DATE(" + HibernateUtils.s((String) q.get("salesEndDateEnd")) + ") ";
		}

		if (q.has("salesEndDateAfter")) {
			whereSQL += " AND DATE(endDate) > DATE(" + HibernateUtils.s((String) q.get("salesEndDateAfter")) + ") ";
		}
		if (q.has("salesEndDateBefore")) {
			whereSQL += " AND DATE(endDate) < DATE(" + HibernateUtils.s((String) q.get("salesEndDateBefore")) + ") ";
		}


		List<Object> sales = HibernateUtils.select("FROM CartItem " + whereSQL, sqlParams);
		return sales;
	}



}