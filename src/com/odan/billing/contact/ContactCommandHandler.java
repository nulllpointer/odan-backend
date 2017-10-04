package com.odan.billing.contact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.contact.command.CreateContact;
import com.odan.billing.contact.command.DeleteContact;
import com.odan.billing.contact.command.UpdateContact;
import com.odan.billing.contact.model.Contact;
import com.odan.common.application.Authentication;
import com.odan.common.utils.DateTime;
import com.odan.security.user.command.UpdateUser;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;
import org.hibernate.Transaction;

public class ContactCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateContact.class, ContactCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateContact.class, ContactCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteContact.class, ContactCommandHandler.class);


    }

    public void handle(ICommand c) {
        if (c instanceof CreateContact) {
            handle((CreateContact) c);
        } else if (c instanceof UpdateContact) {
            handle((UpdateContact) c);
        }
        else if (c instanceof DeleteContact) {
            handle((DeleteContact) c);
        }

    }

    public void handle(CreateContact c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Contact v = this._handleSaveContact(c);
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

    public void handle(UpdateContact c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Contact ba = this._handleSaveContact(c);
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

    public void handle(DeleteContact c) {
        Transaction trx = c.getTransaction();

        try {
            Contact user = (Contact) new ContactQueryHandler().getById(Parser.convertObjectToLong(c.get("id")));

            HibernateUtils.delete(user);

            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(user);
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


    private Contact _handleSaveContact(ICommand c) throws CommandException, JsonProcessingException {

        Contact cust = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateUser) {
            cust = (Contact) (new ContactQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (cust == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (cust == null) {
            cust = new Contact();
        }

        if (c.has("firstName")) {
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

        if (c.has("status")) {

            cust.setStatus(EntityStatus.ACTIVE);
        }

        if (isNew) {
            cust.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            cust.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        cust = (Contact) HibernateUtils.save(cust, c.getTransaction());

        return cust;

    }

   /* public void handle(DeleteContact c) {
        Transaction trx = c.getTransaction();

        try {
            Contact user = (Contact) new ContactQueryHandler().getById(Parser.convertObjectToLong(c.get("id")));
            if (user != null) {
                if (!user.hasDeletePermission(Authentication.getRew3UserId(), Authentication.getRew3GroupId())) {
                    APILogger.add(APILogType.ERROR, "Permission denied");
                    throw new CommandException("Permission denied");
                }
                user.setStatus(Flags.EntityStatus.DISABLED.getFlag());
                HibernateUtils.save(user, trx);
            }
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(user);
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
*/


}
