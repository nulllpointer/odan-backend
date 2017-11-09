/*
package com.odan.inventory.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.inventory.sales.command.CreateSaleOffer;
import com.odan.inventory.sales.command.DeleteSaleOffer;
import com.odan.inventory.sales.command.UpdateSaleOffer;
import com.odan.inventory.sales.model.SaleOffer;
import org.hibernate.Transaction;

public class SaleOfferCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateSaleOffer.class, SaleOfferCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateSaleOffer.class, SaleOfferCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteSaleOffer.class, SaleOfferCommandHandler.class);


    }

    public void handle(ICommand c) throws CommandException {
        if (c instanceof CreateSaleOffer) {
            handle((CreateSaleOffer) c);
        } else if (c instanceof UpdateSaleOffer) {
            handle((UpdateSaleOffer) c);
        } else if (c instanceof DeleteSaleOffer) {
            handle((DeleteSaleOffer) c);
        }

    }

    public void handle(CreateSaleOffer c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            SaleOffer v = this._handleSaveSaleOffer(c);
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


    public void handle(UpdateSaleOffer c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            SaleOffer ba = this._handleSaveSaleOffer(c);
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


    private SaleOffer _handleSaveSaleOffer(ICommand c) throws CommandException, JsonProcessingException {

        SaleOffer cart = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateSaleOffer) {
            cart = (SaleOffer) (new SaleOfferQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (cart == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (cart == null) {
            cart = new SaleOffer();
        }

        if (c.has("identifier")) {
           // cart.setIdentifier((String) c.get("firstName"));
        }


        if (c.has("status")) {

            cart.setStatus(EntityStatus.ACTIVE);
        }

        if (isNew) {
            cart.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            cart.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        cart = (SaleOffer) HibernateUtils.save(cart, c.getTransaction());

        return cart;

    }

    public void handle(DeleteSaleOffer c) throws CommandException {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();


        SaleOffer cart = (SaleOffer) (new SaleOfferQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
        if (cart != null) {
            boolean success = HibernateUtils.delete(cart, trx);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(success);

        }


    }
}
*/
