package com.odan.inventory.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.contact.ContactQueryHandler;
import com.odan.billing.contact.model.Contact;
import com.odan.billing.menu.product.command.UpdateProduct;
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
import com.odan.inventory.sales.command.CreateSale;
import com.odan.inventory.sales.command.DeleteSale;
import com.odan.inventory.sales.command.UpdateCart;
import com.odan.inventory.sales.command.UpdateSale;
import com.odan.inventory.sales.model.Cart;
import com.odan.inventory.sales.model.Sale;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.util.HashMap;

public class SaleCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateSale.class, SaleCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateSale.class, SaleCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteSale.class, SaleCommandHandler.class);

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


        if (c.has("taxRate")) {
            sale.setTaxRate(Parser.convertObjectToDouble(c.get("taxRate")));
        }
        if (c.has("discount")) {
            sale.setDiscount(Parser.convertObjectToDouble(c.get("discount")));
        }
        if (c.has("discountType")) {
            Flags.DiscountType pType = Flags.DiscountType.valueOf(c.get("discountType").toString().toUpperCase());
            sale.setDiscountType(pType);
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

           /* Cart cart = (Cart) new CartQueryHandler().getById());
            if (cart == null) {
                APILogger.add(APILogType.ERROR, "Discount type should be defined.");
                throw new ValidationException("Discount type should be defined.");
            }*/



            HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("cartStatus","CLOSED");
            cartMap.put("id",Parser.convertObjectToLong(c.get("cartId")));
            UpdateCart cartUpdateCommand = new UpdateCart(cartMap);
            CommandRegister.getInstance().process(cartUpdateCommand);
            Cart updatedCart = (Cart) cartUpdateCommand.getObject();

            sale.setCart(updatedCart);

            updatedCart.getItems().stream().forEach(cItem -> {


                Product product = cItem.getProductPrice().getProduct();


                if (product.getProductType() == Flags.ProductType.BOTH) {
                    HashMap map = new HashMap();
                    map.put("id", product.getId());
                    map.put("quantity", cItem.getQuantity());
                    map.put("stock","decrease");

                    UpdateProduct command = new UpdateProduct(map);
                    try {
                        CommandRegister.getInstance().process(command);
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (CommandException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Product updatedProduct = (Product) command.getObject();

                }


            });

        }

        if (c.has("txnDate")) {
            sale.setTxnDate(Parser.convertObjectToTimestamp(c.get("txnDate")));
        }

        if (c.has("contactId")) {

            Contact contact = (Contact) new ContactQueryHandler().getById(Parser.convertObjectToLong(c.get("contactId")));
            if (contact == null) {
                APILogger.add(APILogType.ERROR, "Discount type should be defined.");
                throw new ValidationException("Discount type should be defined.");
            }

            sale.setContact(contact);
        }

        if (c.has("taxRate")) {

            sale.setTaxRate(Parser.convertObjectToDouble(c.get("taxRate")));
        }
        if (c.has("serviceChargeRate")) {

            sale.setServiceChargeRate(Parser.convertObjectToDouble(c.get("serviceChargeRate")));
        }

        if (c.has("grandTotal")) {

            sale.setAmount(Parser.convertObjectToDouble(c.get("grandTotal")));
        }
        if (c.has("dueAmount")) {

            sale.setDue(Parser.convertObjectToDouble(c.get("due")));
        }
        if (c.has("cashReceived")) {

            sale.setCashReceived(Parser.convertObjectToDouble(c.get("cashReceived")));
        }
        if (c.has("txnStatus")) {

            Flags.TransactionStatus txnStatus = Flags.TransactionStatus.valueOf((String) c.get("txnStatus").toString().toUpperCase());
            sale.setTxnStatus(txnStatus);
        }

        if (isNew) {


            sale.setCreatedAt(DateTime.getCurrentTimestamp());
        }
        sale = (Sale) HibernateUtils.save(sale, c.getTransaction());
        return sale;
    }
}