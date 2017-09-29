package com.odan.billing.menu.product.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import com.odan.billing.menu.category.model.Category;
import com.odan.common.database.HibernateUtils;
import com.odan.common.shared.model.AbstractEntity;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity {


	@Column(name = "title")
	private String title;

	@Column(name = "category_id")
	@ManyToOne
	private Category category;

	@Column(name = "description")
	private String description;

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
}
