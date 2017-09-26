package com.odan.billing.invoice.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invoice_item")
public class InvoiceItem {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="invoice_id")
	private Long invoiceId;
	
	@Column(name="product_id")
	private Long productId;
	
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="accounting_code_id")
	private Long accountingCodeId;
	
	@Column(name="customer_accounting_code_id")
	private Long customerAccountingCodeId;

	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="price")
	private Double price;

	@Column(name="sub_total")
	private Double subTotal;
	
	@Column(name="tax_total")
	private Double taxTotal;
	
	@Column(name="taxable")
	private Byte taxable;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@Column(name="updated_at")
	private Timestamp updatedAt;
	
	@Column(name="deleted_at")
	private Timestamp deletedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAccountingCodeId() {
		return accountingCodeId;
	}

	public void setAccountingCodeId(Long accountingCodeId) {
		this.accountingCodeId = accountingCodeId;
	}
	
	public Long getCustomerAccountingCodeId() {
		return customerAccountingCodeId;
	}

	public void setCustomerAccountingCodeId(Long customerAccountingCodeId) {
		this.customerAccountingCodeId = customerAccountingCodeId;
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

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(Double taxTotal) {
		this.taxTotal = taxTotal;
	}

	public Byte getTaxable() {
		return taxable;
	}

	public void setTaxable(Byte taxable) {
		this.taxable = taxable;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}
}
