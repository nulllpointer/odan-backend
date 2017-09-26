package com.odan.finance.sales.model;


import com.odan.billing.catalog.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.database.HibernateUtils;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="sales")
public class Sales {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="product_id")
	private Long productId;

	@Column(name="product_rate_plan_id")
	private Long productRatePlanId;
	
	@Column(name="customer_id")
	private Long customerId;

	@Column(name="start_date")
	private Timestamp startDate;
	
	@Column(name="end_date")
	private Timestamp endDate;
	
	@Column(name = "owner_id")
	private Long ownerId;

	@Column(name="is_invoiced")
	private Byte isInvoiced;
	
	@Column(name="next_invoice_at")
	private Timestamp nextInvoiceAt;
	
	@Column(name="status")
	private Byte status;
	
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getProductRatePlanId() {
		return productRatePlanId;
	}

	public void setProductRatePlanId(Long productRatePlanId) {
		this.productRatePlanId = productRatePlanId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIsInvoiced() {
		return isInvoiced;
	}

	public void setIsInvoiced(Byte isInvoiced) {
		this.isInvoiced = isInvoiced;
	}

	public Timestamp getNextInvoiceAt() {
		return nextInvoiceAt;
	}

	public void setNextInvoiceAt(Timestamp nextInvoiceAt) {
		this.nextInvoiceAt = nextInvoiceAt;
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
	
	public Product getProduct() throws CommandException {
		Product product = null;
		if(this.productId != null) {
			product = (Product) HibernateUtils.get(Product.class, this.productId);
		}
		
		return product;
	}
	

		
}
