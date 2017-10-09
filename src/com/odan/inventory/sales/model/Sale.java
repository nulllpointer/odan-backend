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

	@ManyToOne
	private SaleOffer saleOffer;


	@Column(name="amount")
	private Integer amount;

	@Column(name = "txn_date")
	private Timestamp txn_date;


	@Column(name="discount_type")
	private Flags.DiscountType discountType;

	@Column(name="discount")
	private Double discount;

	@Column(name="txn_type")
	private Flags.TransactionType txnType;

	@Column(name="txn_status")
	private Flags.TransactionStatus txnStatus;

	@Column(name="tax_rate")
	private  Double taxRate;

	@Column(name="service_charge_rate")
	private  Double serviceChargeRate;

	@Column(name = "cash_reveived")
	private Double cashReceived;

	@Column(name = "due")
	private Double due;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public SaleOffer getSaleOffer() {
		return saleOffer;
	}

	public void setSaleOffer(SaleOffer saleOffer) {
		this.saleOffer = saleOffer;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Timestamp getTxn_date() {
		return txn_date;
	}

	public void setTxn_date(Timestamp txn_date) {
		this.txn_date = txn_date;
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

	public Flags.TransactionType getTxnType() {
		return txnType;
	}

	public void setTxnType(Flags.TransactionType txnType) {
		this.txnType = txnType;
	}

	public Flags.TransactionStatus getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(Flags.TransactionStatus txnStatus) {
		this.txnStatus = txnStatus;
	}
}
