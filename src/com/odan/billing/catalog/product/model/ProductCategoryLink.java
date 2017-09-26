package com.odan.billing.catalog.product.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_category_link")
public class ProductCategoryLink {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;	

	@Column(name="product_id")
	private Long productId;	

	@Column(name="product_category_id")
	private Long productCategoryId;

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

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	
}
