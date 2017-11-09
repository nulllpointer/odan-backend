/*
package com.odan.inventory.purchase.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.Uom;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name="purchase_item")
public class PurchaseItem extends AbstractEntity {

	private Double price;

	@ManyToOne
	private Purchase purchase;


	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "product_id")
	private Product product;

	private Integer quantity;





	private Uom uom;

	@Column(name="time_unit")
	private TimeUnit timeUnit;

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Uom getUom() {
		return uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}


	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
*/
