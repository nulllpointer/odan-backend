package com.odan.inventory.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.contact.ContactQueryHandler;
import com.odan.billing.contact.model.Contact;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.inventory.sales.command.*;
import com.odan.inventory.sales.model.Cart;
import com.odan.inventory.sales.model.Sale;
import com.odan.inventory.sales.model.SaleItem;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.util.*;

public class SalesCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateSale.class, SalesCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateSale.class, SalesCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteSale.class, SalesCommandHandler.class);

    }

    public void handle(ICommand c) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        if (c instanceof CreateSale) {
            handle((CreateSale) c);
        } else if (c instanceof UpdateSale) {
            handle((UpdateSale) c);
        } else if (c instanceof DeleteSale) {
            handle((DeleteSale) c);
        }

    }


    public void handle(CreateSale c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Sale v = this._handleSaveSale(c);
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

    public void handle(UpdateSale c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Sale ba = this._handleSaveSale(c);
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

    public Sale _handleSaveSale(ICommand c) throws ValidationException, CommandException, ParseException, JsonProcessingException {

        boolean isNew = false;

        Sale sale = null;

        if (c.has("id") && c instanceof UpdateSale) {
            sale = (Sale) HibernateUtils.get(Sale.class, Parser.convertObjectToLong(c.get("id")));
            sale.setId(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
        } else {
            sale = new Sale();
            isNew = true;
        }


        if (!c.has("taxRate")) {
            APILogger.add(APILogType.ERROR, "Tax rate should be defined.");
            throw new ValidationException("Tax rate should be defined.");
        }
        // If discount type is defined then discount should be defined
        // too and vice versa.
        if (c.has("discountType") && !c.has("discount")) {
            APILogger.add(APILogType.ERROR, "Discount should be defined.");
            throw new ValidationException("Discount should be defined.");
        } else if (c.has("discount") && !c.has("discountType")) {
            APILogger.add(APILogType.ERROR, "Discount type should be defined.");
            throw new ValidationException("Discount type should be defined.");
        }

        if (c.has("cartId")) {
            sale.setCart((Cart) new CartQueryHandler().getById((Long) c.get("cart_id")));
        }

        if (c.has("txnDate")) {
            sale.setTxn_date(Parser.convertObjectToTimestamp(c.get("invoiceDate")));
        }

        if (c.has("contactId")) {
            sale.setContact((Contact) new ContactQueryHandler().getById((Long) c.get("cart_id")));
        }


        if (c.has("taxRate")) {

            sale.setTaxRate((Double) c.get("taxRate"));
        }


        if (c.has("discountType")) {
            Flags.DiscountType discountType = (Flags.DiscountType) Flags.DiscountType.valueOf((String) c.get("discountType").toString().toUpperCase());
            sale.setDiscountType(discountType);
        }

        if (c.has("discount")) {
            Double discount = Parser.convertObjectToDouble(c.get("discount"));
            sale.setDiscount(discount);
        }


        List<HashMap<String, Object>> itemsData = (List<HashMap<String, Object>>) c.get("saleItems");

        if (isNew) {
            Flags.TransactionStatus txnStatus = Flags.TransactionStatus.valueOf((String) c.get("txnStatus").toString().toUpperCase());
            sale.setTxnStatus(txnStatus);
            Flags.TransactionType txnType = Flags.TransactionType.valueOf((String) c.get("txnType").toString().toUpperCase());
            sale.setTxnType(txnType);

            HibernateUtils.save(sale, c.getTransaction());


            sale.setCreatedAt(DateTime.getCurrentTimestamp());
        } else {


            Query query = new Query();
            query.set("id", sale.getId());

            List<Object> oldSaleItems = new SaleItemQueryHandler().get(query);

            if (oldSaleItems != null) {

                for (Object o : oldSaleItems) {

                    HashMap<String, Object> oldIdMap = new HashMap<String, Object>();
                    oldIdMap.put("id", ((SaleItem) o).getId());
                    ICommand deleteItemCommand = new DeleteSaleItem(oldIdMap, c.getTransaction());
                    CommandRegister.getInstance().process(deleteItemCommand);
                    SaleItem item = (SaleItem) deleteItemCommand.getObject();
                    if (item == null) {
                        APILogger.add(APILogType.ERROR, "Error creating sale item.");
                        throw new ValidationException("Error creating sale item.");
                    }

                }

            }


            sale.setUpdatedAt(DateTime.getCurrentTimestamp());
        }


        List<Object> saleItems = new ArrayList<Object>();
        if (itemsData != null) {
            for (HashMap<String, Object> itemData : itemsData) {
                itemData.put("sale", sale);
                ICommand createItemCommand = new CreateSaleItem(itemData, c.getTransaction());
                CommandRegister.getInstance().process(createItemCommand);
                SaleItem item = (SaleItem) createItemCommand.getObject();
                if (item == null) {
                    APILogger.add(APILogType.ERROR, "Error creating sale item.");
                    throw new ValidationException("Error creating sale item.");
                }
                saleItems.add(item);

            }
        }


        sale = (Sale) HibernateUtils.save(sale, c.getTransaction());
        return sale;
    }
}