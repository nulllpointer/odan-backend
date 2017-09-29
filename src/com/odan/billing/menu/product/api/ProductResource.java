package com.odan.billing.menu.product.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.menu.product.command.CreateProduct;
import com.odan.billing.menu.product.command.DeleteProduct;
import com.odan.billing.menu.product.command.UpdateProduct;
import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1")
public class ProductResource extends RestAction {

	@Action(value = "sales/product", results = { @Result(type = "json") })
	public String actionProduct() {
		String responseStatus = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("GET")) {
			responseStatus = getProduct();
		} else if (httpRequest.getMethod().equals("POST")) {
			responseStatus = createProduct();
		} else if (httpRequest.getMethod().equals("PUT")) {
			responseStatus = updateProduct();
		} else if (httpRequest.getMethod().equals("DELETE")) {
			responseStatus = deleteProduct();
		} else {
			responseStatus = "HttpMethodNotAccepted";
		}

		return responseStatus;
	}

	public String createProduct() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateProduct command = new CreateProduct(requestData);
		CommandRegister.getInstance().process(command);
		Product p = (Product) command.getObject();

		if (p != null) {
			responseStatus = SUCCESS;
			getData().put("productId", p.getId());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String updateProduct() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateProduct command = new UpdateProduct(requestData);
		CommandRegister.getInstance().process(command);
		Product p = (Product) command.getObject();

		if (p != null) {
			responseStatus = SUCCESS;
			getData().put("productId", p.getId());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String deleteProduct() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		DeleteProduct command = new DeleteProduct(requestData);
		CommandRegister.getInstance().process(command);
		boolean deleted = (boolean) command.getObject();

		if (deleted) {
			responseStatus = SUCCESS;
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}
	
	public String getProduct() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData, "billing/menu/product/get");

		List<Object> categories = (new CategoryQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("products", categories);
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}
	
//
//	@Action(value = "sales/product/{id}/clone", results = { @Result(type = "json") })
//	public String actionProductClone() {
//		String responseStatus = SUCCESS;
//		HttpServletRequest httpRequest = ServletActionContext.getRequest();
//
//		if (httpRequest.getMethod().equals("POST")) {
//			responseStatus = cloneProduct();
//		} else {
//			responseStatus = "HttpMethodNotAccepted";
//		}
//
//		return responseStatus;
//	}
//
//	public String cloneProduct() {
//		String responseStatus = SUCCESS;
//		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
//		JSONValidatorReport report = null;
//		try {
//			report = JSONValidatorEngine.validateRequest("sales/product/clone", getRequest());
//		} catch (IOException | ProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			responseStatus = ERROR;
//		}
//
//		if (report.isValid()) {
//			Product p = ProductCommandHandler.cloneProduct(requestData);
//			if (p != null) {
//				setSuccess("Product is saved");
//				getData().put("id", p.getId());
//				responseStatus = SUCCESS;
//			} else {
//				setError("Error saving product");
//				responseStatus = ERROR;
//			}
//		} else {
//			List<JSONValidatorLog> logs = report.getReport();
//			getData().put("log", logs);
//			setError("Schema Validation Failed");
//			responseStatus = ERROR;
//		}
//
//		return responseStatus;
//	}
//
//	@Action(value = "sales/product/{id}/rateplan", results = { @Result(type = "json") })
//	public String actionProductRatePlan() {
//		String response = SUCCESS;
//		HttpServletRequest httpRequest = ServletActionContext.getRequest();
//
//		if (httpRequest.getMethod().equals("GET")) {
//			getProductRatePlan();
//		} else {
//			response = "HttpMethodNotAccepted";
//		}
//
//		return response;
//	}
//
//	public String getProductRatePlan() {
//		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
//		JSONValidatorReport report = null;
//		try {
//			report = JSONValidatorEngine.validateRequest("sales/product/get", getRequest());
//
//		} catch (IOException | ProcessingException e) {
//			// TODO Auto-generated catch block
//			setError("Exception: " + e.getMessage());
//			e.printStackTrace();
//		}
//
//		if (report.isValid()) {
//			setSuccess();
//			Long productId = null;
//			if (requestData.containsKey("productId")) {
//				productId = Parser.convertObjectToLong(requestData.get("productId"));
//			}
//			List<Map<String, Object>> ratePlans = ProductCommandHandler.getProductRatePlanList(productId);
//			getData().put("ratePlans", ratePlans);
//		} else {
//			List<JSONValidatorLog> logs = report.getReport();
//			getData().put("log", logs);
//			setError("Schema Validation Failed");
//		}
//
//		return SUCCESS;
//	}
}
