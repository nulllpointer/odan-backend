/*
package com.odan.inventory.sales;

import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.security.sales.command.CreateSales;

public class SalesCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateSales.class, SalesCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateSales) {
			handle((CreateSales) c);
		}
	}

	public void handle(CreateSales c) {

	}
}
*/
