package com.odan.inventory.purchase.model;


import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.Uom;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name="purchase_item")
public class PurchaseItem extends AbstractEntity {

	@ManyToOne
	private ProductPrice productPrice;

	@ManyToOne
	private Purchase purchase;


	private Integer quantity;

	private Uom uom;

	@Column(name="amount")
	private Integer alertAfter;

	@Column(name="time_unit")
	private TimeUnit timeUnit;

	public ProductPrice getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}

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

	public Integer getAlertAfter() {
		return alertAfter;
	}

	public void setAlertAfter(Integer alertAfter) {
		this.alertAfter = alertAfter;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
}
