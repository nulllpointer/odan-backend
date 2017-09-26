package com.odan.billing.catalog.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.catalog.product.command.*;
import com.odan.billing.catalog.product.model.Product;
import com.odan.billing.catalog.product.model.ProductCategoryLink;
import com.odan.billing.catalog.product.model.ProductFeatureLink;
import com.odan.billing.catalog.product.model.ProductRatePlanLink;
import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.IQueryHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class ProductCommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateProduct.class, ProductCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateProduct.class, ProductCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteProduct.class, ProductCommandHandler.class);
	}

	public void handle(ICommand c) throws CommandException, JsonProcessingException {
		if (c instanceof CreateProduct) {
			handle((CreateProduct) c);
		} else if (c instanceof UpdateProduct) {
			handle((UpdateProduct) c);
		} else if (c instanceof DeleteProduct) {
			handle((DeleteProduct) c);
		} else if (c instanceof AddCategoryToProduct) {
			handle((AddCategoryToProduct) c);
		} else if (c instanceof RemoveCategoryFromProduct) {
			handle((RemoveCategoryFromProduct) c);
		} else if (c instanceof AddFeatureToProduct) {
			handle((AddFeatureToProduct) c);
		} else if (c instanceof RemoveFeatureFromProduct) {
			handle((RemoveFeatureFromProduct) c);
		} else if (c instanceof AddRatePlanToProduct) {
			handle((AddRatePlanToProduct) c);
		} else if (c instanceof RemoveRatePlanFromProduct) {
			handle((RemoveRatePlanFromProduct) c);
		}
	}

	public void handle(CreateProduct c) {
		Product p = null;
		Transaction trx = null;
		// HibernateUtils.openSession();
		if (c.getTransaction() != null) {
			trx = c.getTransaction();
		} else {
			trx = HibernateUtils.beginTransaction();
		}

		try {
			p = new Product();

			if (c.has("title")) {
				p.setTitle((String) c.get("title"));
			}

			if (c.has("status")) {
				p.setStatus(Flags.convertInputToStatus(c.get("status")));
			} else {
				p.setStatus(EntityStatus.ACTIVE.getFlag());
			}

			p.setCreatedAt(DateTime.getCurrentTimestamp());
			p.setOwnerId(Authentication.getUserId());

			HibernateUtils.save(p, trx);


			// Save Product Categories
			List<Long> categoryId = Parser.convertObjectToLongList(c.get("categoryId"));
			if (categoryId != null) {
				CommandRegister.getInstance().process(new RemoveCategoryFromProduct(p.getId(), trx));
				if (categoryId != null) {
					for (Long cId : categoryId) {
						CommandRegister.getInstance()
								.process(new AddCategoryToProduct(p.getId(), cId, c.getTransaction()));
					}
				}
			}

			// Save Product Features
			List<Long> featureId = Parser.convertObjectToLongList(c.get("featureId"));
			if (featureId != null) {
				CommandRegister.getInstance().process(new RemoveFeatureFromProduct(p.getId(), trx));
				if (featureId != null) {
					for (Long fId : featureId) {
						CommandRegister.getInstance()
								.process(new AddFeatureToProduct(p.getId(), fId, c.getTransaction()));
					}
				}
			}

			// Save Product Rate Plan
			List<Long> ratePlanId = Parser.convertObjectToLongList(c.get("ratePlanId"));
			if (ratePlanId != null) {
				CommandRegister.getInstance().process(new RemoveRatePlanFromProduct(p.getId(), trx));
				if (ratePlanId != null) {
					for (Long rpId : ratePlanId) {
						CommandRegister.getInstance()
								.process(new AddRatePlanToProduct(p.getId(), rpId, c.getTransaction()));
					}
				}
			}

			HibernateUtils.commitTransaction(trx);
		} catch (Exception e) {
			System.out.println(e);
			HibernateUtils.rollbackTransaction(trx);
			p = null;
		} finally {
			HibernateUtils.closeSession();
		}
		c.setObject(p);
	}

	public void handle(UpdateProduct c) throws CommandException, JsonProcessingException {

		Product p = null;
		Transaction trx = null;
		// HibernateUtils.openSession();
		if (c.getTransaction() != null) {
			trx = c.getTransaction();
		} else {
			trx = HibernateUtils.beginTransaction();
		}

		if (c.has("id")) {
			p = (Product) HibernateUtils.get(Product.class, Parser.convertObjectToLong(c.get("id")));
			if (p == null) {
				APILogger.add(APILogType.ERROR, "Invalid product id.");
				return;
			}
		} else {
			APILogger.add(APILogType.ERROR, "Product id missing.");
			return;
		}

		if (c.has("title")) {
			p.setTitle((String) c.get("title"));
		}

		if (c.has("status")) {
			p.setStatus(Flags.convertInputToStatus(c.get("status")));
		}
		p.setUpdatedAt(DateTime.getCurrentTimestamp());

		HibernateUtils.save(p, trx);
		c.setObject(p);
	}

	public void handle(AddCategoryToProduct c) {
		try {
			Transaction trx = null;
			// HibernateUtils.openSession();
			if (c.getTransaction() != null) {
				trx = c.getTransaction();
			} else {
				trx = HibernateUtils.beginTransaction();
			}

			ProductCategoryLink pcl = new ProductCategoryLink();
			Long productId = Parser.convertObjectToLong(c.get("productId"));
			Long categoryId = Parser.convertObjectToLong(c.get("categoryId"));
			if (productId != null && categoryId != null) {
				pcl.setProductId(productId);
				pcl.setProductCategoryId(categoryId);
				HibernateUtils.save(pcl, trx);
				c.setObject(pcl);
			} else {
				APILogger.add(APILogType.ERROR, "Error adding category to product.");
			}

		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Error adding category to product.", ex);
		}
	}

	public void handle(RemoveCategoryFromProduct c) {
		try {
			Transaction trx = null;
			// HibernateUtils.openSession();
			if (c.getTransaction() != null) {
				trx = c.getTransaction();
			} else {
				trx = HibernateUtils.beginTransaction();
			}

			HashMap<String, Object> sqlParams = new HashMap<>();
			String sql = "DELETE FROM ProductCategoryLink WHERE productId = :productId";
			sqlParams.put("productId", c.get("productId"));
			if (c.has("categoryId")) {
				sqlParams.put("categoryId", c.get("categoryId"));
				sql += " AND categoryId = :categoryId";

			}
			HibernateUtils.query(sql, sqlParams, trx);

			c.setObject(true);
		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Error removing category(s) from product.", ex);
		}
	}

	public void handle(AddFeatureToProduct c) {
		try {
			Transaction trx = null;
			// HibernateUtils.openSession();
			if (c.getTransaction() != null) {
				trx = c.getTransaction();
			} else {
				trx = HibernateUtils.beginTransaction();
			}

			ProductFeatureLink pfl = new ProductFeatureLink();
			Long productId = Parser.convertObjectToLong(c.get("productId"));
			Long featureId = Parser.convertObjectToLong(c.get("featureId"));
			if (productId != null && featureId != null) {
				pfl.setProductId(productId);
				pfl.setProductFeatureId(featureId);
				HibernateUtils.save(pfl, trx);
				c.setObject(pfl);
			} else {
				APILogger.add(APILogType.ERROR, "Error adding feature to product.");
			}

		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Error adding feature to product.", ex);
		}
	}

	public void handle(RemoveFeatureFromProduct c) {
		try {
			Transaction trx = null;
			// HibernateUtils.openSession();
			if (c.getTransaction() != null) {
				trx = c.getTransaction();
			} else {
				trx = HibernateUtils.beginTransaction();
			}

			HashMap<String, Object> sqlParams = new HashMap<>();
			String sql = "DELETE FROM ProductFeatureLink WHERE productId = :productId";
			sqlParams.put("productId", c.get("productId"));
			if (c.has("featureId")) {
				sqlParams.put("featureId", c.get("featureId"));
				sql += " AND featureId = :featureId";

			}
			HibernateUtils.query(sql, sqlParams, trx);

			c.setObject(true);
		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Error removing feature(s) from product.", ex);
		}
	}

	public void handle(AddRatePlanToProduct c) {
		try {
			Transaction trx = null;
			// HibernateUtils.openSession();
			if (c.getTransaction() != null) {
				trx = c.getTransaction();
			} else {
				trx = HibernateUtils.beginTransaction();
			}

			ProductRatePlanLink prl = new ProductRatePlanLink();
			Long productId = Parser.convertObjectToLong(c.get("productId"));
			Long rateplanId = Parser.convertObjectToLong(c.get("rateplanId"));

			if (productId != null && rateplanId != null) {
				prl.setProductId(productId);
				prl.setProductRatePlanId(rateplanId);
				HibernateUtils.save(prl, trx);
				c.setObject(prl);
			} else {
				APILogger.add(APILogType.ERROR, "Error adding rateplan to product.");
			}

		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Error adding rateplan to product.", ex);
		}
	}

	public void handle(RemoveRatePlanFromProduct c) {
		try {
			Transaction trx = null;
			// HibernateUtils.openSession();
			if (c.getTransaction() != null) {
				trx = c.getTransaction();
			} else {
				trx = HibernateUtils.beginTransaction();
			}

			HashMap<String, Object> sqlParams = new HashMap<>();
			String sql = "DELETE FROM ProductRatePlanLink WHERE productId = :productId";
			sqlParams.put("productId", c.get("productId"));
			if (c.has("rateplanId")) {
				sqlParams.put("rateplanId", c.get("rateplanId"));
				sql += " AND rateplanId = :rateplanId";

			}
			HibernateUtils.query(sql, sqlParams, trx);

			c.setObject(true);
		} catch (Exception ex) {
			APILogger.add(APILogType.ERROR, "Error removing rateplan(s) from product.", ex);
		}
	}

	// public static boolean deleteProduct(HashMap<String, Object> data) {
	//
	// boolean result = false;
	// Long productId = Parser.convertObjectToLong(c.get("id"));
	//
	// Product p = (Product) HibernateUtils.get(Product.class, productId);
	//
	//
	// if (p != null) {
	//
	// AccountingCode aCode = p.getAccountingCode();
	//
	// boolean productUsed = ProductCommandHandler.isProductUsed(productId);
	// boolean accountingCodeUsed = false;
	// if (aCode != null) {
	// accountingCodeUsed =
	// AccountingCodeModel.isAccountingCodeUsed(aCode.getId());
	// }
	//
	// HibernateUtils.getSession();
	// Transaction trx = HibernateUtils.beginTransaction();
	// try {
	// if (productUsed) {
	// p.setStatus(EntityStatus.DISABLED.getFlag());
	// HibernateUtils.save(p, trx);
	//
	// result = true;
	// } else {
	// ProductCommandHandler.unassignCategory(productId, trx);
	// ProductCommandHandler.unassignFeature(productId, trx);
	// ProductCommandHandler.unassignRatePlan(productId, trx);
	//
	// result = HibernateUtils.delete(p, trx);
	// }
	//
	// if (aCode != null && (productUsed || accountingCodeUsed)) {
	// aCode.setStatus(EntityStatus.DISABLED.getFlag());
	// HibernateUtils.save(aCode, trx);
	// } else if (aCode != null) {
	// result = result && HibernateUtils.delete(aCode, trx);
	// }
	//
	// if (result) {
	// HibernateUtils.commitTransaction(trx);
	// } else {
	// throw new ValidationException("Product deletion failed.");
	// }
	// } catch (Exception e) {
	// HibernateUtils.rollbackTransaction(trx);
	// e.printStackTrace();
	// } finally {
	// HibernateUtils.closeSession();
	// }
	// }
	//
	// return result;
	// }

	// public static Product cloneProduct(HashMap<String, Object> data,
	// Transaction aTrx) {
	//
	// boolean success = false;
	// boolean isCommitable = false;
	// if (aTrx == null) {
	// isCommitable = true;
	// }
	//
	// Session s = HibernateUtils.getSession();
	// Transaction trx = aTrx;
	// if (isCommitable) {
	// trx = HibernateUtils.beginTransaction();
	// }
	// Product p = null;
	// try {
	// Long productId = Parser.convertObjectToLong(c.get("id"));
	//
	// p = (Product) HibernateUtils.get(Product.class, productId);
	//
	// List<Map<String, Object>> pcList = p.getCategories();
	// List<Map<String, Object>> pfList = p.getFeatures();
	// List<Map<String, Object>> prList = p.getRatePlans();
	//
	// s.evict(p);
	// p.setId(null);
	// p.setAccountingCodeId(null);
	// HibernateUtils.save(p, trx);
	// System.out.println("PRODUCT ACCOUNTING CODE NEW");
	// AccountingCode acode =
	// AccountingModel.getOrCreateProductAccountingCode(p.getId(), trx);
	// if (acode == null) {
	// throw new ValidationException("Error creating product accounting code.");
	// }
	// System.out.println("PRODUCT ACCOUNTING CLASS CREATED");
	//
	// for (Map<String, Object> pc : pcList) {
	// ProductCommandHandler.assignCategory(p.getId(),
	// Parser.convertObjectToLong(pc.get("id")), trx);
	// }
	//
	// for (Map<String, Object> pf : pfList) {
	// ProductCommandHandler.assignFeature(p.getId(),
	// Parser.convertObjectToLong(pf.get("id")), trx);
	// }
	//
	// for (Map<String, Object> pr : prList) {
	// ProductCommandHandler.assignRatePlan(p.getId(),
	// Parser.convertObjectToLong(pr.get("id")), trx);
	// }
	//
	// if (isCommitable) {
	// HibernateUtils.commitTransaction(trx);
	// }
	// success = true;
	// } catch (Exception ex) {
	// System.out.println(ex);
	// if (isCommitable) {
	// HibernateUtils.rollbackTransaction(trx);
	// }
	// success = false;
	// p = null;
	// } finally {
	// if (isCommitable) {
	// HibernateUtils.closeSession();
	// }
	// }
	//
	// return p;
	//
	// }

	// public static Product cloneProduct(HashMap<String, Object> data) {
	// return cloneProduct(data, null);
	// }

	private boolean isDuplicateTitle(String title) {
		HashMap<String, Object> queryParamsTitle = new HashMap<String, Object>();
		queryParamsTitle.put("title", title);
		queryParamsTitle.put("ownerId", Authentication.getUserId());
		Query q = new Query(queryParamsTitle);
		IQueryHandler qHandle = new ProductQueryHandler();
		List<Object> pList = qHandle.get(q);
		return (pList.size() > 0);
	}

	private boolean isProductUsed(Long productId) {
		Query q = new Query();
		q.set("productId", productId);
		Integer count = (new ProductQueryHandler()).getSalesCountById(q);
		return (count != null && count > 0);
	}
}
