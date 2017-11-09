package com.odan.inventory.sales.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cart_item")
public class CartItem extends AbstractEntity {

    @ManyToOne
    private ProductPrice productPrice;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

   private Integer quantity;

    private Double price;

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
