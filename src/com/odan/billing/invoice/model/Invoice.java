package com.odan.billing.invoice.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.odan.common.database.HibernateUtils;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "invoice_number")
	private String invoiceNumber;

	@Column(name = "transaction_number")
	private String transactionNumber;

	@Column(name = "customer_id")
	private Long customerId;
	
	@Column(name = "vendor_id")
	private Long vendorId;
	
	@Column(name = "sale_id")
	private Long saleId;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "payment_term")
	private Byte paymentTerm;

	@Column(name = "invoice_date")
	private Timestamp invoiceDate;

	@Column(name = "due_date")
	private Timestamp dueDate;

	@Column(name = "sub_total")
	private Double subTotal;

	@Column(name = "tax_total")
	private Double taxTotal;
	
	@Column(name = "discount_total")
	private Double discountTotal;

	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "due_amount")
	private Double dueAmount;

	@Column(name = "balance_amount")
	private Double balanceAmount;
	
	//Status of invoice for sender in Internal invoice, recipient in External invoice
	@Column(name = "status")
	private Byte status;
	
	//Status of invoice for recipient, used in Internal invoice
	@Column(name = "customer_status")
	private Byte customerStatus;
	
	//Status of payment for invoice
	@Column(name = "payment_status")
	private Byte paymentStatus;
	
	//Status of due for invoice
	@Column(name = "due_status")
	private Byte dueStatus;
	
	//Status of refund for invoice
	@Column(name = "refund_status")
	private Byte refundStatus;
	
	//Status of writeoff for invoice
	@Column(name = "writeoff_status")
	private Byte writeoffStatus;

	@Column(name = "tax_rate_type")
	private Byte taxRateType;

	@Column(name = "tax_rate")
	private Double taxRate;

	@Column(name = "discount_type")
	private Byte discountType;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "note")
	private String note;

	@Column(name = "data")
	private String data;

	@Column(name = "type")
	private Byte type;
	
	@Column(name = "recurring_period")
	private Byte recurringPeriod;
	
	@Column(name = "recur_end_date")
	private Timestamp recurEndDate;

	@Column(name = "recur_start_date")
	private Timestamp recurStartDate;

	@Column(name = "next_recur_date")
	private Timestamp nextRecurDate;
	
	@Column(name = "recur_parent_id")
	private Long recurParentId;
	
	@Column(name = "owner_id")
	private Long ownerId;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;
	
	public Long getRecurParentId() {
		return recurParentId;
	}

	public void setRecurParentId(Long recurParentId) {
		this.recurParentId = recurParentId;
	}

	public Byte getRecurringPeriod() {
		return recurringPeriod;
	}

	public void setRecurringPeriod(Byte recurringPeriod) {
		this.recurringPeriod = recurringPeriod;
	}

	public Timestamp getRecurEndDate() {
		return recurEndDate;
	}

	public void setRecurEndDate(Timestamp recurEndDate) {
		this.recurEndDate = recurEndDate;
	}

	public Timestamp getRecurStartDate() {
		return recurStartDate;
	}

	public void setRecurStartDate(Timestamp recurStartDate) {
		this.recurStartDate = recurStartDate;
	}

	public Timestamp getNextRecurDate() {
		return nextRecurDate;
	}

	public void setNextRecurDate(Timestamp nextRecurDate) {
		this.nextRecurDate = nextRecurDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Byte getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(Byte paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
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
	
	public Double getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(Double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(Byte customerStatus) {
		this.customerStatus = customerStatus;
	}

	public Byte getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Byte paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Byte getDueStatus() {
		return dueStatus;
	}

	public void setDueStatus(Byte dueStatus) {
		this.dueStatus = dueStatus;
	}

	public Byte getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Byte refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Byte getWriteoffStatus() {
		return writeoffStatus;
	}

	public void setWriteoffStatus(Byte writeoffStatus) {
		this.writeoffStatus = writeoffStatus;
	}

	public Byte getTaxRateType() {
		return taxRateType;
	}

	public void setTaxRateType(Byte taxType) {
		this.taxRateType = taxType;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	
	public Byte getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Byte discountType) {
		this.discountType = discountType;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	public List<InvoiceItem> getItems() {
		List<InvoiceItem> items = null;
		if(this.id != null) {
			items = (List<InvoiceItem>) HibernateUtils.select("FROM InvoiceItem WHERE invoiceId = "+this.id);
		}
		
		return items;
	}
	


}
