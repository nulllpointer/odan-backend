package com.odan.security.sales.model;


import com.odan.billing.menu.product.model.Product;
import com.odan.common.application.CommandException;
import com.odan.common.database.HibernateUtils;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="sales")
public class SaleItem extends AbstractEntity{
	

	
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


	

		
}
