package com.odan.inventory.sales.model;


import com.odan.billing.menu.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.database.HibernateUtils;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "cart_item")
public class CartItem extends AbstractEntity {

    @ManyToOne
    private Product product;

    @ManyToOne
    private Cart cart;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
