package com.odan.inventory.purchase;

import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.inventory.purchase.command.CreatePurchase;

public class PurchaseCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreatePurchase.class, PurchaseCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreatePurchase) {
			handle((CreatePurchase) c);
		}
	}

	public void handle(CreatePurchase c) {

	}
}
