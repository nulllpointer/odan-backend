package com.odan.inventory.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.contact.ContactQueryHandler;
import com.odan.billing.contact.model.Contact;
import com.odan.billing.menu.product.ProductQueryHandler;
import com.odan.billing.menu.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.inventory.purchase.command.*;
import com.odan.inventory.purchase.model.Purchase;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class PurchaseCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreatePurchase.class, PurchaseCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdatePurchase.class, PurchaseCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeletePurchase.class, PurchaseCommandHandler.class);
        CommandRegister.getInstance().registerHandler(CreatePurchaseItem.class, PurchaseCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdatePurchaseItem.class, PurchaseCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeletePurchaseItem.class, PurchaseCommandHandler.class);

    }

    public void handle(ICommand c) {
        if (c instanceof CreatePurchase) {
            handle((CreatePurchase) c);
        }
        if (c instanceof UpdatePurchase) {
            handle((UpdatePurchase) c);
        }
        if (c instanceof DeletePurchase) {
            handle((DeletePurchase) c);
        }
       /* if (c instanceof CreatePurchaseItem) {
            handle((CreatePurchaseItem) c);
        }
        if (c instanceof UpdatePurchaseItem) {
            handle((UpdatePurchaseItem) c);
        }
        if (c instanceof DeletePurchaseItem) {
            handle((DeletePurchaseItem) c);
        }*/

    }

    public void handle(CreatePurchase c) {
        Transaction trx = c.getTransaction();

        try {
            Purchase ba = this._handleSavePurchase(c);
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

    private Purchase _handleSavePurchase(ICommand c) throws CommandException, JsonProcessingException, ValidationException, ParseException {

        boolean isNew = false;

        Purchase purchase = null;

        if (c.has("id") && c instanceof UpdatePurchase) {
            purchase = (Purchase) new PurchaseQueryHandler().getById(Parser.convertObjectToLong(c.get("id")));
            purchase.setId(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
        } else {
            purchase = new Purchase();
            isNew = true;
        }


        if (c.has("txnDate")) {
            purchase.setTxnDate(Parser.convertObjectToTimestamp(c.get("txnDate")));
        }


        if (c.has("contactId")) {

            Contact contact = (Contact) new ContactQueryHandler().getById(Parser.convertObjectToLong(c.get("contactId")));
            if (contact == null) {
                APILogger.add(APILogType.ERROR, "Contact id should be defined.");
                throw new ValidationException("Contact id should be defined.");
            }

            purchase.setContact(contact);
        }

        if (c.has("productId")) {

            Product product = (Product) new ProductQueryHandler().getById(Parser.convertObjectToLong(c.get("productId")));
            if (product == null) {
                APILogger.add(APILogType.ERROR, "Product id should be defined.");
                throw new ValidationException("Product id should be defined.");
            }
            purchase.setProduct(product);
        }

        if (c.has("price")) {
            purchase.setPrice(Parser.convertObjectToDouble(c.get("price")));
        }

        if (c.has("quantity")) {
            purchase.setQuantity(Parser.convertObjectToInteger(c.get("quantity")));
        }

        if (c.has("uom")) {
            Flags.Uom uom = Flags.Uom.valueOf((String) c.get("uom").toString().toUpperCase());
            purchase.setUom(uom);
        }
        if (c.has("timeUnit")) {

            TimeUnit tu = TimeUnit.valueOf((String) c.get("timeUnit").toString().toUpperCase());
            purchase.setTimeUnit(tu);
        }

        if (isNew) {
            purchase.setCreatedAt(DateTime.getCurrentTimestamp());
        } else {
            purchase.setUpdatedAt(DateTime.getCurrentTimestamp());
        }


        purchase.setCreatedAt(DateTime.getCurrentTimestamp());

        purchase = (Purchase) HibernateUtils.save(purchase, c.getTransaction());
        return purchase;

    }

    public void handle(UpdatePurchase c) {
        Transaction trx = c.getTransaction();

        try {
            Purchase ba = this._handleSavePurchase(c);
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


    public void handle(DeletePurchase c) {

    }


}
