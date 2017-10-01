package com.odan.security.sales.model;


import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.model.Flags.TransactionStatus;
import com.odan.common.model.Flags.DiscountType;
import com.odan.common.model.Flags.Uom;
import com.odan.common.shared.model.AbstractEntity;
import com.odan.inventory.purchase.model.Purchase;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="sale")
public class Sale extends AbstractEntity{

	@ManyToOne
	private ProductPrice productPrice;

	@ManyToOne
	private Purchase purchase;

	private Integer quantity;

	private  Uom uom;

	private DiscountType discountType;

	private Integer serviceChargePercent;

	private TransactionStatus txnStatus;

	private Integer taxPercent;

		
}
