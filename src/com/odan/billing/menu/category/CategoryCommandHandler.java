package com.odan.billing.menu.category;

import org.hibernate.Transaction;

import com.odan.billing.menu.category.command.CreateCategory;
import com.odan.billing.menu.category.command.DeleteCategory;
import com.odan.billing.menu.category.command.UpdateCategory;
import com.odan.billing.menu.category.model.Category;
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

public class CategoryCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateCategory.class, CategoryCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateCategory.class, CategoryCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteCategory.class, CategoryCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateCategory) {
			handle((CreateCategory) c);
		} else if (c instanceof UpdateCategory) {
			handle((UpdateCategory) c);
		} else if (c instanceof DeleteCategory) {
			handle((DeleteCategory) c);
		}
	}

	public void handle(CreateCategory c) {

		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Category pc = null;

			pc = new Category();

			if (c.has("title")) {
				pc.setTitle((String) c.get("title"));
			}

			if (c.has("status")) {

				pc.setStatus(EntityStatus.valueOf((String) c.get("status")));
			}else {
				pc.setStatus(EntityStatus.ACTIVE);
			}

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

	public void handle(UpdateCategory c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {

			Category pc = (Category) (new CategoryQueryHandler())
					.getById(Parser.convertObjectToLong(c.get("id")));
			if (pc == null) {
				APILogger.add(APILogType.ERROR, "Category id(" + c.get("id") + ") not found.");
				throw new CommandException();
			}

			if (c.has("title")) {
				pc.setTitle((String) c.get("title"));
			}
			if (c.has("status")) {
				pc.setStatus(EntityStatus.valueOf((String) c.get("status")));
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

	public void handle(DeleteCategory c) {

	}

}
