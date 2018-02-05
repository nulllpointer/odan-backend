package com.odan.inventory.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.odan.inventory.sales.command.CreateCart;
import com.odan.inventory.sales.command.DeleteCart;
import com.odan.inventory.sales.command.UpdateCart;
import com.odan.inventory.sales.model.Cart;
import org.hibernate.Transaction;

import java.text.ParseException;

public class CartCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateCart.class, CartCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateCart.class, CartCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteCart.class, CartCommandHandler.class);


    }

    public void handle(ICommand c) throws CommandException {
        if (c instanceof CreateCart) {
            handle((CreateCart) c);
        } else if (c instanceof UpdateCart) {
            handle((UpdateCart) c);
        } else if (c instanceof DeleteCart) {
            handle((DeleteCart) c);
        }

    }

    public void handle(CreateCart c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Cart v = this._handleSaveCart(c);
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


    public void handle(UpdateCart c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Cart ba = this._handleSaveCart(c);
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


    private Cart _handleSaveCart(ICommand c) throws CommandException, JsonProcessingException, ParseException {

        Cart cart = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateCart) {
            cart = (Cart) (new CartQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (cart == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (cart == null) {
            cart = new Cart();
        }

        if (c.has("identifier")) {
            cart.setIdentifier((String) c.get("identifier"));
        }
        if (c.has("txnDate")) {
            cart.setTxnDate(Parser.convertObjectToTimestamp(c.get("txnDate")));
        }


        if (c.has("status")) {

            cart.setStatus(EntityStatus.ACTIVE);
        }

        if (c.has("cartStatus")) {

            cart.setCartStatus(Flags.CartStatus.OPEN);
        }

        if (isNew) {
            cart.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            cart.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        cart = (Cart) HibernateUtils.save(cart, c.getTransaction());

        return cart;

    }

    public void handle(DeleteCart c) throws CommandException {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();


        Cart cart = (Cart) (new CartQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
        if (cart != null) {
            boolean success = HibernateUtils.delete(cart, trx);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(success);

        }


    }
}
