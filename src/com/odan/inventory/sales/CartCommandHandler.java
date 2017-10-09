package com.odan.inventory.sales;

import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.inventory.sales.command.*;

public class CartCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateCart.class, CartCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateCart.class, CartCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteCart.class, CartCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateCartItem.class, CartCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateCartItem.class, CartCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteCartItem.class, CartCommandHandler.class);


	}

	public void handle(ICommand c) {
		if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}else if (c instanceof UpdateCart) {
			handle((UpdateCart) c);
		}if (c instanceof DeleteCart) {
			handle((DeleteCart) c);
		}if (c instanceof CreateCartItem) {
			handle((CreateCartItem) c);
		}if (c instanceof UpdateCartItem) {
			handle((UpdateCartItem) c);
		}if (c instanceof DeleteCartItem) {
			handle((DeleteCartItem) c);
		}
	}

	public void handle(CreateSale c) {

	}
}
