package com.odan.common.application;

import com.odan.billing.contact.ContactCommandHandler;
import com.odan.billing.menu.category.CategoryCommandHandler;
import com.odan.billing.menu.product.ProductCommandHandler;
import com.odan.inventory.purchase.PurchaseCommandHandler;
import com.odan.inventory.sales.*;
import com.odan.inventory.sales.model.SaleOffer;
import com.odan.security.accountingperiod.AccountingPeriodCommandHandler;
import com.odan.security.user.UserCommandHandler;

import javax.servlet.ServletContext;

public class Application {
    private static ServletContext context;

    /* Called by Listener */
    public static void setServletContext(ServletContext context) {
        Application.context = context;
        System.out.println("CONTEXT INIT");

        CategoryCommandHandler.registerCommands();
        ProductCommandHandler.registerCommands();

        PurchaseCommandHandler.registerCommands();

        AccountingPeriodCommandHandler.registerCommands();

        ContactCommandHandler.registerCommands();

        UserCommandHandler.registerCommands();

        SalesCommandHandler.registerCommands();

        CartCommandHandler.registerCommands();

        CartItemCommandHandler.registerCommands();

        SaleOfferCommandHandler.registerCommands();


        SalesEventHandler.registerEvents();

    }

    /* Use this method to access context from any location */
    public static ServletContext getServletContext() {
        return Application.context;

    }
}
