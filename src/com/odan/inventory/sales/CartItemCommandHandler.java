package com.odan.inventory.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.product.ProductQueryHandler;
import com.odan.billing.menu.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.inventory.sales.command.*;
import com.odan.inventory.sales.model.Cart;
import com.odan.inventory.sales.model.CartItem;
import org.hibernate.Transaction;

public class CartItemCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateCartItem.class, CartItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateCartItem.class, CartItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteCartItem.class, CartItemCommandHandler.class);


    }

    public void handle(ICommand c) throws CommandException {
        if (c instanceof CreateCartItem) {
            handle((CreateCartItem) c);
        } else if (c instanceof UpdateCartItem) {
            handle((UpdateCartItem) c);
        } else if (c instanceof DeleteCartItem) {
            handle((DeleteCartItem) c);
        }

    }

    public void handle(CreateCartItem c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            CartItem v = this._handleSaveCartItem(c);
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


    public void handle(UpdateCartItem c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            CartItem ba = this._handleSaveCartItem(c);
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


    private CartItem _handleSaveCartItem(ICommand c) throws CommandException, JsonProcessingException {

        CartItem item = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateCartItem) {
            item = (CartItem) (new CartItemQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (item == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (item == null) {
            item = new CartItem();
        }

        if (c.has("cartId")) {

            Query query = new Query();
            query.set("cartId", Parser.convertObjectToLong(c.get("cartId")));

            Cart cart = (Cart) new CartQueryHandler().getById(Parser.convertObjectToLong(c.get("cartId")));

            item.setCart(cart);

        }
        if (c.has("productId")) {
            Product product = (Product) new ProductQueryHandler().getById(Parser.convertObjectToLong(c.get("productId")));

            item.setProduct(product);
        }
        if (c.has("quantity")) {
            item.setQuantity((Integer) c.get("quantity"));
        }


        if (c.has("status")) {

            item.setStatus(EntityStatus.ACTIVE);
        }

        if (isNew) {
            item.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            item.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        item = (CartItem) HibernateUtils.save(item, c.getTransaction());

        return item;

    }

    public void handle(DeleteCartItem c) throws CommandException {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();


        CartItem cart = (CartItem) (new CartItemQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
        if (cart != null) {
            boolean success = HibernateUtils.delete(cart, trx);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(success);

        }


    }
}
