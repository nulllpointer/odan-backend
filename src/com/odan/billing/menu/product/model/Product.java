package com.odan.billing.menu.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odan.billing.menu.category.model.Category;
import com.odan.common.model.Flags.ProductType;
import com.odan.common.shared.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity {


	@Column(name = "title")
	private String title;

	@ManyToOne
	@JsonIgnore
	private Category category;

	@Column(name = "description")
	private String description;

	@Column(name = "type")
	private ProductType type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
}
