package com.odan.billing.menu.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.billing.menu.category.model.Category;
import com.odan.common.application.CommandException;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.ProductType;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity {


    @Column(name = "title")
    private String title;

    @ManyToOne
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "product_type")
    private ProductType productType;

    @Column(name = "quantity")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory(Object category_id) {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
       return this.category;

    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }


}
