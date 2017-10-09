package com.odan.billing.menu.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.billing.menu.category.model.Category;
import com.odan.billing.menu.product.command.*;
import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class ProductCommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreateProduct.class, ProductCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdateProduct.class, ProductCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeleteProduct.class, ProductCommandHandler.class);
    }

    public void handle(ICommand c) throws CommandException, JsonProcessingException {
        if (c instanceof CreateProduct) {
            handle((CreateProduct) c);
        } else if (c instanceof UpdateProduct) {
            handle((UpdateProduct) c);
        } else if (c instanceof DeleteProduct) {
            handle((DeleteProduct) c);
        } else if (c instanceof AddProductToCategory) {
            handle((AddProductToCategory) c);
        } else if (c instanceof RemoveCategoryFromProduct) {
            handle((RemoveCategoryFromProduct) c);
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

    public void handle(UpdateProduct c) throws CommandException, JsonProcessingException {

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
        Product product = null;
        boolean isNew = true;

        if (c.has("id") && c instanceof UpdateProduct) {
            product = (Product) (new ProductQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
            isNew = false;
            if (product == null) {
                APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
                throw new CommandException("Customer (" + c.get("id") + ") not found.");
            }
        }

        if (product == null) {
            product = new Product();
        }


        if (c.has("title")) {
            product.setTitle((String) c.get("title"));
        }
        if (c.has("description")) {
            product.setDescription((String) c.get("description"));
        }

        if (c.has("category_id")) {
            Category category = (Category) new CategoryQueryHandler().getById(Parser.convertObjectToLong(c.get("category_id")));
            product.setCategory(category);
        }

        if (c.has("status")) {

            product.setStatus(EntityStatus.valueOf((String) c.get("status")));
        }
        if (c.has("type")) {

            product.setType(Flags.ProductType.valueOf((String) c.get("type").toString().toUpperCase()));
        } else {
            product.setStatus(EntityStatus.ACTIVE);
        }

        if (isNew) {
            product.setCreatedAt(DateTime.getCurrentTimestamp());

        } else {
            product.setUpdatedAt(DateTime.getCurrentTimestamp());
        }

        product = (Product) HibernateUtils.save(product, c.getTransaction());


        ProductPrice productPrice = new ProductPrice();

        if (isNew) {
            productPrice.setPrice((Integer) c.get("price"));
            productPrice.setOldPrice(0);

            productPrice.setCreatedAt(DateTime.getCurrentTimestamp());
            HibernateUtils.save(productPrice, c.getTransaction());
        } else {
            int currentPrice=(Integer) c.get("price");
            productPrice = new ProductPriceQueryHandler().getLatestPrice(product.getId());
            if(currentPrice!=productPrice.getPrice()){
                productPrice.setOldPrice(productPrice.getPrice());
                productPrice.setPrice(currentPrice);
                productPrice.setProduct(product);
                productPrice.setUpdatedAt(DateTime.getCurrentTimestamp());
                HibernateUtils.save(productPrice, c.getTransaction());
            }
            else {
                return product;
            }

        }
        return product;

    }


	/*private boolean isDuplicateTitle(String title) {
        HashMap<String, Object> queryParamsTitle = new HashMap<String, Object>();
		queryParamsTitle.put("title", title);
		queryParamsTitle.put("ownerId", Authentication.getUserId());
		Query q = new Query(queryParamsTitle);
		IQueryHandler qHandle = new ProductQueryHandler();
		List<Object> pList = qHandle.get(q);
		return (pList.size() > 0);
	}

	private boolean isProductUsed(Long productId) {
		Query q = new Query();
		q.set("productId", productId);
		Integer count = (new ProductQueryHandler()).getSalesCountById(q);
		return (count != null && count > 0);
	}*/
}
