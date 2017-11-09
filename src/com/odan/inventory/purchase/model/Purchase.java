package com.odan.inventory.purchase.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odan.billing.contact.model.Contact;
import com.odan.billing.menu.product.model.Product;
import com.odan.common.model.Flags;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name="purchase")
public class Purchase extends AbstractEntity {

	@Column(name = "txn_date")
	private Timestamp txnDate;

	@Column(name="price")
	private Double price;

	@ManyToOne
	private Contact contact;

	@ManyToOne
	//@JsonIgnore
	@JoinColumn(name = "product_id")
	private Product product;

	private Integer quantity;

	private Flags.Uom uom;

	@Column(name="time_unit")
	private TimeUnit timeUnit;

	public Timestamp getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Timestamp txnDate) {
		this.txnDate = txnDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Flags.Uom getUom() {
		return uom;
	}

	public void setUom(Flags.Uom uom) {
		this.uom = uom;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
}
