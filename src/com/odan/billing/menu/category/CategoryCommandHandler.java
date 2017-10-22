package com.odan.billing.menu.category;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.hibernate.Transaction;

public class CategoryCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateCategory.class, CategoryCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateCategory.class, CategoryCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteCategory.class, CategoryCommandHandler.class);


    }

    public void handle(ICommand c) throws CommandException {
        if (c instanceof CreateCategory) {
            handle((CreateCategory) c);
        } else if (c instanceof UpdateCategory) {
            handle((UpdateCategory) c);
        }
        else if (c instanceof DeleteCategory) {
            handle((DeleteCategory) c);
        }

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
        // HibernateUtils.openSession();
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




    private Category _handleSaveCategory(ICommand c) throws CommandException, JsonProcessingException {

        Category cust = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateCategory) {
            cust = (Category) (new CategoryQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (cust == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (cust == null) {
            cust = new Category();
        }

     /*   if (c.has("firstName")) {
            cust.setFirstName((String) c.get("firstName"));
        }

        if (c.has("lastName")) {
            cust.setLastName((String) c.get("lastName"));
        }

        if (c.has("email")) {
            cust.setEmail((String) c.get("email"));
        }

        if (c.has("phone")) {
            cust.setPhone((String) c.get("phone"));
        }

        if (c.has("addressLine1")) {
            cust.setAddressLine1((String) c.get("addressLine1"));
        }

        if (c.has("addressLine2")) {
            cust.setAddressLine2((String) c.get("addressLine2"));
        }

        if (c.has("city")) {
            cust.setCity((String) c.get("city"));
        }

        if (c.has("state")) {
            cust.setState((String) c.get("state"));
        }

        if (c.has("country")) {
            cust.setCountry((String) c.get("country"));
        }

        if (c.has("postalCode")) {
            cust.setPostalCode((String) c.get("postalCode"));
        }
*/
        if (c.has("status")) {

            cust.setStatus(EntityStatus.ACTIVE);
        }
        if (c.has("title")) {

            cust.setTitle((String) c.get("title"));
        }

        if (c.has("type")) {

            cust.setTitle((String) c.get("type"));
        }

        if (isNew) {
            cust.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            cust.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        cust = (Category) HibernateUtils.save(cust, c.getTransaction());

        return cust;

    }

    public void handle(DeleteCategory c) throws CommandException {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();


        Category category = (Category) (new CategoryQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
        if(category!=null){
            boolean success= HibernateUtils.delete(category,trx);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(success);

        }







    }



}
