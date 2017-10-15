package com.odan.billing.menu.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.category.command.CreateCategory;
import com.odan.billing.menu.category.command.DeleteCategory;
import com.odan.billing.menu.category.command.UpdateCategory;
import com.odan.billing.menu.category.model.Category;
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
import org.hibernate.Transaction;

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


    private Category _handleSaveCategory(ICommand c) throws CommandException, JsonProcessingException, JsonProcessingException {

        Category category = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateCategory) {
            category = (Category) (new CategoryQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (category == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (category == null) {
            category = new Category();
        }

        if (c.has("title")) {
            category.setTitle((String) c.get("firstName"));
        }
        if (c.has("description")) {
            category.setDescription((String) c.get("firstName"));
        }
        if (c.has("type")) {
            category.setType(Flags.MainCategoryType.valueOf((String) c.get("type").toString().toUpperCase()));
        }

        if (c.has("parent_id")) {
            Category parent = (Category) new CategoryQueryHandler().getById(Parser.convertObjectToLong(c.get("parent_id")));
            category.setParent(parent);
        }

        if (c.has("status")) {

            category.setStatus(EntityStatus.ACTIVE);
        }

        if (isNew) {
            category.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            category.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        category = (Category) HibernateUtils.save(category, c.getTransaction());

        return category;

    }


    public void handle(CreateCategory c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Category v = this._handleSaveCategory(c);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(v);
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

        Transaction trx = c.getTransaction();

        try {
            Category ba = this._handleSaveCategory(c);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(ba);
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
