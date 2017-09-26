package com.odan.inventory.sales;

import com.odan.billing.catalog.product.model.Product;
import com.odan.common.application.Authentication;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.model.Flags.SalesIsInvoiced;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.finance.sales.command.CreateSales;
import com.odan.finance.sales.model.Sales;
import org.hibernate.Transaction;

public class SalesCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateSales.class, SalesCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateSales) {
			handle((CreateSales) c);
		}
	}

	public void handle(CreateSales c) {
		//HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Sales s = null;

			Long productId = Parser.convertObjectToLong(c.get("productId"));
			Long rateplanId = Parser.convertObjectToLong(c.get("ratePlanId"));

			Product p = (Product) HibernateUtils.get(Product.class, productId);

			if (p == null) {
				APILogger.add(APILogType.ERROR, "Product id is invalid.");
				throw new ValidationException("Product id is invalid");
			}
			boolean rpMatched = false;

			if (!rpMatched) {
				APILogger.add(APILogType.ERROR, "Rateplan doesnt belong to selected product.");
				throw new ValidationException("Rateplan doesnt belong to selected product.");
			}

			s = new Sales();
			boolean isNew = true;

			s.setProductId(productId);
			s.setProductRatePlanId(rateplanId);
			s.setCustomerId(Parser.convertObjectToLong(c.get("customerId")));

			s.setStartDate(Parser.convertObjectToTimestamp(c.get("startDate")));
			s.setEndDate(Parser.convertObjectToTimestamp(c.get("endDate")));

			// s.setStatus(Flags.convertInputToStatus(data.get("status")));
			s.setStatus(EntityStatus.ACTIVE.getFlag());
			s.setNextInvoiceAt(s.getStartDate());
			s.setIsInvoiced(SalesIsInvoiced.NO.getFlag());

			if (isNew) {
				s.setCreatedAt(DateTime.getCurrentTimestamp());
				s.setOwnerId(Authentication.getUserId());
			} else {
				s.setUpdatedAt(DateTime.getCurrentTimestamp());
			}

			HibernateUtils.save(s, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(c.getTransaction());
			}
			c.setObject(s);
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}
}
