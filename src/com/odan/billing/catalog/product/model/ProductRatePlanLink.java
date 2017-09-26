package com.odan.billing.catalog.product.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_rate_plan_link")
public class ProductRatePlanLink {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;	

	@Column(name="product_id")
	private Long productId;	

	@Column(name="product_rate_plan_id")
	private Long productRatePlanId;

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
	
}
