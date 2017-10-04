package com.odan.billing.contact.command;

import java.util.HashMap;

import com.odan.common.cqrs.ICommand;

public class CreateContact extends CreateOrUpdateContactAbstract implements ICommand {
    public CreateContact(HashMap<String, Object> data) {
        super(data);
        this.validationSchema = "/billing/contact/create";
        this.validate();
    }
}