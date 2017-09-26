package com.odan.billing.catalog.product.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.odan.common.database.HibernateUtils;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "accounting_code_id")
	private Long accountingCodeId;

	@Column(name = "status")
	private Byte status;

	@Column(name = "owner_id")
	private Long ownerId;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getAccountingCodeId() {
		return accountingCodeId;
	}

	public void setAccountingCodeId(Long accountingCodeId) {
		this.accountingCodeId = accountingCodeId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public List<Map<String,Object>> getCategories() {
		List<Map<String,Object>> categories = null;
		if (this.id != null) {
			categories = (List<Map<String,Object>>) HibernateUtils.selectSQL("SELECT pc.* FROM product_category pc "
					+ " INNER JOIN product_category_link pcl ON pc.id = pcl.product_category_id "
					+ " WHERE pcl.product_id = " + this.id);
		}

		return categories;
	}

	public List<Map<String,Object>> getFeatures() {
		List<Map<String,Object>> features = null;
		if (this.id != null) {
			features = (List<Map<String,Object>>) HibernateUtils.selectSQL("SELECT pf.* FROM product_feature pf "
					+ " INNER JOIN product_feature_link pfl ON pf.id = pfl.product_feature_id "
					+ " WHERE pfl.product_id = " + this.id);
		}

		return features;
	}

	public List<Map<String,Object>> getRatePlans() {
		List<Map<String,Object>> ratePlans = null;
		if (this.id != null) {
			ratePlans = (List<Map<String,Object>>) HibernateUtils.selectSQL("SELECT prp.* FROM product_rate_plan prp "
					+ " INNER JOIN product_rate_plan_link prpl ON prp.id = prpl.product_rate_plan_id "
					+ " WHERE prpl.product_id = " + this.id);
		}

		return ratePlans;
	}
	


}
