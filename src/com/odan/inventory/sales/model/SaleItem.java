/*
package com.odan.inventory.sales.model;


import com.odan.billing.contact.model.Contact;
import com.odan.billing.menu.product.model.Product;
import com.odan.common.model.Flags;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name="sale_item")
public class SaleItem extends AbstractEntity {

	@ManyToOne
	private Product product;

	@ManyToOne
	private Contact contact;

	@ManyToOne
	private Sale sale;

	@Column(name="discount_type")
	private Flags.DiscountType discountType;

	@Column(name="discount")
	private Double discount;

	@Column(name="offer_type")
	private Flags.OfferType offerType;

	@Column(name="offer")
	private Double offer;

	@Column(name="price")
	private Integer price;

	@Column(name="quantity")
	private Integer quantity;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
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

	public Flags.OfferType getOfferType() {
		return offerType;
	}

	public void setOfferType(Flags.OfferType offerType) {
		this.offerType = offerType;
	}

	public Double getOffer() {
		return offer;
	}

	public void setOffer(Double offerValue) {
		this.offer = offerValue;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer itemPrice) {
		this.price = itemPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
*/
