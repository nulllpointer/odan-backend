package com.odan.common.application;

import com.odan.billing.catalog.product.ProductCommandHandler;
import com.odan.billing.catalog.productcategory.ProductCategoryCommandHandler;
import com.odan.billing.customer.CustomerCommandHandler;
import com.odan.billing.invoice.InvoiceCommandHandler;
import com.odan.billing.invoice.InvoiceEventHandler;
import com.odan.billing.sales.SalesCommandHandler;
import com.odan.billing.sales.SalesEventHandler;
import com.odan.billing.user.UserCommandHandler;
import com.odan.finance.accountingperiod.AccountingPeriodCommandHandler;

import javax.servlet.ServletContext;

public class Application {
	private static ServletContext context;

	/* Called by Listener */
	public static void setServletContext(ServletContext context) {
		Application.context = context;
		System.out.println("CONTEXT INIT");
		
		//Command Handler
		InvoiceCommandHandler.registerCommands();

		
		ProductCategoryCommandHandler.registerCommands();
		ProductCommandHandler.registerCommands();
		SalesCommandHandler.registerCommands();
		
		AccountingPeriodCommandHandler.registerCommands();


		//I am the change
		CustomerCommandHandler.registerCommands();

		UserCommandHandler.registerCommands();
		
		
		//Event Handler
		InvoiceEventHandler.registerEvents();
		SalesEventHandler.registerEvents();

	}

	/* Use this method to access context from any location */
	public static ServletContext getServletContext() {
		return Application.context;
		
	}
}
