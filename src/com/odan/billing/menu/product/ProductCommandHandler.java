package com.odan.billing.menu.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.billing.menu.category.model.Category;
import com.odan.billing.menu.product.ProductQueryHandler;
import com.odan.billing.menu.product.command.CreateProduct;
import com.odan.billing.menu.product.command.DeleteProduct;
import com.odan.billing.menu.product.command.UpdateProduct;
import com.odan.billing.menu.product.model.Product;
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

public class ProductCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateProduct.class, ProductCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateProduct.class, ProductCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteProduct.class, ProductCommandHandler.class);


    }

    public void handle(ICommand c) throws CommandException {
        if (c instanceof CreateProduct) {
            handle((CreateProduct) c);
        } else if (c instanceof UpdateProduct) {
            handle((UpdateProduct) c);
        } else if (c instanceof DeleteProduct) {
            handle((DeleteProduct) c);
        }

    }

    public void handle(CreateProduct c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Product v = this._handleSaveProduct(c);
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


    public void handle(UpdateProduct c) {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();

        try {
            Product ba = this._handleSaveProduct(c);
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


    private Product _handleSaveProduct(ICommand c) throws CommandException, JsonProcessingException {

        Product prod = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateProduct) {
            prod = (Product) (new ProductQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (prod == null) {
                APILogger.add(APILogType.ERROR, "Product (" + c.get("id") + ") not found.");
                throw new CommandException("Product (" + c.get("id") + ") not found.");
            }
        }

        if (prod == null) {
            prod = new Product();
        }

        if (c.has("status")) {
            prod.setStatus(EntityStatus.ACTIVE);
        }

        if (c.has("title")) {
            prod.setTitle((String) c.get("title"));
        }

        if (c.has("principalCategoryType")) {
            Flags.PrincipalCategoryType type= Flags.PrincipalCategoryType.valueOf(c.get("principalCategoryType").toString().toUpperCase());
            prod.setPrincipalCategoryType(type);
        }
        if (c.has("productType")) {
            Flags.ProductType pType= Flags.ProductType.valueOf(c.get("productType").toString().toUpperCase());
            prod.setProductType(pType);
        }

        if (c.has("categoryId")) {
            Category category = (Category) new CategoryQueryHandler().getById(Parser.convertObjectToLong(c.get("categoryId")));
            prod.setCategory(category);
        }
            if (isNew) {
                prod.setCreatedAt(DateTime.getCurrentTimestamp());

            } else {
                prod.setUpdatedAt(DateTime.getCurrentTimestamp());
            }

            prod = (Product) HibernateUtils.save(prod, c.getTransaction());

            return prod;
        }


    public void handle(DeleteProduct c) throws CommandException {
        // HibernateUtils.openSession();
        Transaction trx = c.getTransaction();


        Product product = (Product) (new ProductQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
        if (product != null) {
            boolean success = HibernateUtils.delete(product, trx);
            if (c.isCommittable()) {
                HibernateUtils.commitTransaction(c.getTransaction());
            }
            c.setObject(success);

        }


    }
}
