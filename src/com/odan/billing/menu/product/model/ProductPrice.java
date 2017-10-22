package com.odan.billing.menu.product.model;

import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "product_price")
public class ProductPrice extends AbstractEntity {

    @ManyToOne
    private Product product;

    @Column(name = "price")
    private Double price;


    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate.toString();
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
