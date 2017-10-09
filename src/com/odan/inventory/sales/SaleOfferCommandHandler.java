package com.odan.inventory.sales;

import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.inventory.sales.command.*;

public class SaleOfferCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateSale.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateSale.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteSale.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateCart.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateCart.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteCart.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateCartItem.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateCartItem.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteCartItem.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CreateSaleOffer.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateSaleOffer.class, SaleOfferCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteSaleOffer.class, SaleOfferCommandHandler.class);


	}

	public void handle(ICommand c) {
		if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}else if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}if (c instanceof CreateSale) {
			handle((CreateSale) c);
		}

	}

	public void handle(CreateSale c) {

	}
}
