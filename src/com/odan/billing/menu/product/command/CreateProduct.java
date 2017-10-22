package com.odan.billing.menu.product.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class CreateProduct extends CreateOrUpdateProductAbstract implements ICommand {
    public CreateProduct(HashMap<String, Object> data) {
        super(data);
        this.validationSchema = "billing/menu/product/create";

       // this.validate();
    }
}