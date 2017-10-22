package com.odan.inventory.purchase;

import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.inventory.purchase.command.*;

public class PurchaseItemCommandHandler implements ICommandHandler {

    public static void registerCommands() {
        CommandRegister.getInstance().registerHandler(CreatePurchase.class, PurchaseItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdatePurchase.class, PurchaseItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeletePurchase.class, PurchaseItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(CreatePurchaseItem.class, PurchaseItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(UpdatePurchaseItem.class, PurchaseItemCommandHandler.class);
        CommandRegister.getInstance().registerHandler(DeletePurchaseItem.class, PurchaseItemCommandHandler.class);

    }

    public void handle(ICommand c) {
        if (c instanceof CreatePurchase) {
            handle((CreatePurchase) c);
        }
        if (c instanceof UpdatePurchase) {
            handle((UpdatePurchase) c);
        }
        if (c instanceof DeletePurchase) {
            handle((DeletePurchase) c);
        }
        if (c instanceof CreatePurchaseItem) {
            handle((CreatePurchaseItem) c);
        }
        if (c instanceof UpdatePurchaseItem) {
            handle((UpdatePurchaseItem) c);
        }
        if (c instanceof DeletePurchaseItem) {
            handle((DeletePurchaseItem) c);
        }

    }

    public void handle(CreatePurchase c) {



    }

    public void handle(UpdatePurchase c) {

    }

    public void handle(DeletePurchase c) {

    }

    public void handle(CreatePurchaseItem c) {

    }

    public void handle(UpdatePurchaseItem c) {

    }

    public void handle(DeletePurchaseItem c) {

    }


}