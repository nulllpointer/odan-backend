package com.odan.inventory.sales.model;


import com.odan.billing.menu.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.*;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.shared.model.AbstractEntity;
import com.odan.inventory.sales.CartItemQueryHandler;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "cart")
public class Cart extends AbstractEntity {

    @Column(name = "identifier")
    private String identifier;




    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<CartItem> getItems() {
        List<CartItem> items = null;
        if(this.getId() != null) {
            items = (List<CartItem>) HibernateUtils.select("FROM CartItem WHERE cart_id = "+this.getId());
        }

        return items;
    }

}
