package com.odan.billing.menu.product.model;

import com.odan.common.model.Flags.ProductPriceType;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "product_price")
public class ProductPrice extends AbstractEntity {

    @ManyToOne
    private Product product;

    @Column(name = "price")
    private Integer price;

    @Column(name = "old_price")
    private Integer oldPrice;

    @Column(name = "active_from")
    private Date activeFrom;

    @Column(name = "type")
    private  ProductPriceType type;

    @Column(name = "alert")
    private  String alert;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Integer oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }
}
