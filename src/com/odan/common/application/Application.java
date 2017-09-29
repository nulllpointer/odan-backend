package com.odan.common.application;

import com.odan.billing.contact.ContactCommandHandler;
import com.odan.billing.menu.category.CategoryCommandHandler;
import com.odan.billing.menu.product.ProductCommandHandler;
import com.odan.inventory.sales.SalesCommandHandler;
import com.odan.inventory.sales.SalesEventHandler;
import com.odan.security.accountingperiod.AccountingPeriodCommandHandler;
import com.odan.security.user.UserCommandHandler;

import javax.servlet.ServletContext;

public class Application {
	private static ServletContext context;

	/* Called by Listener */
	public static void setServletContext(ServletContext context) {
		Application.context = context;
		System.out.println("CONTEXT INIT");
		
		//Command Handler
//		InvoiceCommandHandler.registerCommands();

		
		CategoryCommandHandler.registerCommands();
		ProductCommandHandler.registerCommands();
		SalesCommandHandler.registerCommands();
		
		AccountingPeriodCommandHandler.registerCommands();


		//I am the change
		ContactCommandHandler.registerCommands();

		UserCommandHandler.registerCommands();
		
		
		//Event Handler
//		InvoiceEventHandler.registerEvents();
		SalesEventHandler.registerEvents();

	}

	/* Use this method to access context from any location */
	public static ServletContext getServletContext() {
		return Application.context;
		
	}
}
