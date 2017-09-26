package com.odan.billing.catalog.productcategory;

import org.hibernate.Transaction;

import com.odan.billing.catalog.productcategory.command.CreateProductCategory;
import com.odan.billing.catalog.productcategory.command.DeleteProductCategory;
import com.odan.billing.catalog.productcategory.command.UpdateProductCategory;
import com.odan.billing.catalog.productcategory.model.ProductCategory;
import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;

public class ProductCategoryCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateProductCategory.class, ProductCategoryCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateProductCategory.class, ProductCategoryCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteProductCategory.class, ProductCategoryCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateProductCategory) {
			handle((CreateProductCategory) c);
		} else if (c instanceof UpdateProductCategory) {
			handle((UpdateProductCategory) c);
		} else if (c instanceof DeleteProductCategory) {
			handle((DeleteProductCategory) c);
		}
	}

	public void handle(CreateProductCategory c) {

		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			ProductCategory pc = null;

			pc = new ProductCategory();

			if (c.has("title")) {
				pc.setTitle((String) c.get("title"));
			}

			if (c.has("status")) {
				pc.setStatus(Flags.convertInputToStatus(c.get("status")));
			} else {
				pc.setStatus(EntityStatus.ACTIVE.getFlag());
			}

			pc.setOwnerId(Authentication.getUserId());
			pc.setCreatedAt(DateTime.getCurrentTimestamp());

			HibernateUtils.save(pc, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}
			c.setObject(pc);
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

	public void handle(UpdateProductCategory c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {

			ProductCategory pc = (ProductCategory) (new ProductCategoryQueryHandler())
					.getById(Parser.convertObjectToLong(c.get("id")));
			if (pc == null) {
				APILogger.add(APILogType.ERROR, "Category id(" + c.get("id") + ") not found.");
				throw new CommandException();
			}

			if (c.has("title")) {
				pc.setTitle((String) c.get("title"));
			}
			if (c.has("status")) {
				pc.setStatus(Flags.convertInputToStatus(c.get("status")));
			}
			pc.setUpdatedAt(DateTime.getCurrentTimestamp());

			HibernateUtils.save(pc, trx);

			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(trx);
			}
			c.setObject(pc);
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

	public void handle(DeleteProductCategory c) {

	}

}
