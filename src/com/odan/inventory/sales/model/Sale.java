package com.odan.inventory.sales.model;


import com.odan.billing.contact.model.Contact;
import com.odan.common.model.Flags;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="sale")
public class Sale extends AbstractEntity{

	@ManyToOne
	private Contact contact;

	@ManyToOne
	private Cart cart;

	@Column(name="amount")
	private Double amount;

	@Column(name = "txn_date")
	private Timestamp txnDate;


	@Column(name="discount_type")
	private Flags.DiscountType discountType;

	@Column(name="discount")
	private Double discount;


	@Column(name="txn_status")
	private Flags.TransactionStatus txnStatus;

	@Column(name="tax_rate")
	private  Double taxRate;

	@Column(name="service_charge_rate")
	private  Double serviceChargeRate;

	@Column(name = "cash_received")
	private Double cashReceived;

	@Column(name = "due")
	private Double due;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}


	public Timestamp getTxnDate() {
		return txnDate;
	}

	public void dxnDate(Timestamp txn_date) {
		this.txnDate = txn_date;
	}

	public Flags.DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Flags.DiscountType discountType) {
		this.discountType = discountType;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discountValue) {
		this.discount = discountValue;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getServiceChargeRate() {
		return serviceChargeRate;
	}

	public void setServiceChargeRate(Double serviceChargeRate) {
		this.serviceChargeRate = serviceChargeRate;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Double getCashReceived() {
		return cashReceived;
	}

	public void setCashReceived(Double cashReceived) {
		this.cashReceived = cashReceived;
	}

	public Double getDue() {
		return due;
	}

	public void setDue(Double due) {
		this.due = due;
	}

	public Flags.TransactionStatus getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(Flags.TransactionStatus txnStatus) {
		this.txnStatus = txnStatus;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setTxnDate(Timestamp txnDate) {
		this.txnDate = txnDate;
	}
}
