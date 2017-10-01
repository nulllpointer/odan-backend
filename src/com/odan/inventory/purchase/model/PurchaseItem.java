package com.odan.inventory.purchase.model;


import com.odan.billing.menu.product.model.ProductPrice;
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

	private Integer alertAfter;

	private TimeUnit timeUnit;




}
