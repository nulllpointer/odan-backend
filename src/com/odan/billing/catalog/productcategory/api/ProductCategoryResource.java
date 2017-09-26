package com.odan.billing.catalog.productcategory.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.catalog.productcategory.ProductCategoryQueryHandler;
import com.odan.billing.catalog.productcategory.command.CreateProductCategory;
import com.odan.billing.catalog.productcategory.command.UpdateProductCategory;
import com.odan.billing.catalog.productcategory.model.ProductCategory;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1")
public class ProductCategoryResource extends RestAction {
	
	public ProductCategoryResource() {
		System.out.println("PRODUCT CATEGORY RESOURCES");
	}
	
	@Action(value = "sales/category", results = { @Result(type = "json") })
	public String actionCategory() {
		String responseStatus = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("GET")) {
			responseStatus = getCategory();
		} else if (httpRequest.getMethod().equals("POST")) {
			responseStatus = createCategory();
		} else if (httpRequest.getMethod().equals("PUT")) {
			responseStatus = updateCategory();
		} else {
			responseStatus = "HttpMethodNotAccepted";
		}

		return responseStatus;
	}

	public String createCategory() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateProductCategory command = new CreateProductCategory(requestData);
		CommandRegister.getInstance().process(command);
		ProductCategory pc = (ProductCategory) command.getObject();

		if (pc != null) {
			responseStatus = SUCCESS;
			getData().put("categoryId", pc.getId());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String updateCategory() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateProductCategory command = new UpdateProductCategory(requestData);
		CommandRegister.getInstance().process(command);
		ProductCategory pc = (ProductCategory) command.getObject();

		if (pc != null) {
			responseStatus = SUCCESS;
			getData().put("categoryId", pc.getId());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}
	
	public String getCategory() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData, "billing/catalog/category/get");
		List<Object> categories = (new ProductCategoryQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("categories", categories);
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

}
