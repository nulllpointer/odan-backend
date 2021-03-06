package com.odan.inventory.purchase.model;


import com.odan.billing.contact.model.Contact;
import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.application.CommandException;
import com.odan.common.database.HibernateUtils;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="purchase")
public class Purchase extends AbstractEntity {


	@Column(name="product_price_id")
	@ManyToOne
	ProductPrice productPrice;

	@Column(name="amount")
	private Integer amount;

	@Column(name="contact_id")
	@ManyToOne
	private Contact contact;




	

		
}
