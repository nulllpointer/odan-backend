package com.odan.billing.menu.category.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.billing.menu.category.command.CreateCategory;
import com.odan.billing.menu.category.command.UpdateCategory;
import com.odan.billing.menu.category.model.Category;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1")
public class CategoryResource extends RestAction {
	
	public CategoryResource() {
		System.out.println("PRODUCT CATEGORY RESOURCES");
	}
	
	@Action(value = "menu/category", results = { @Result(type = "json") })
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
		CreateCategory command = new CreateCategory(requestData);
		CommandRegister.getInstance().process(command);
		Category pc = (Category) command.getObject();

		/*if (pc != null) {
			responseStatus = SUCCESS;
			getData().put("categoryId", pc.getId());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/

		return responseStatus;
	}

	public String updateCategory() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateCategory command = new UpdateCategory(requestData);
		CommandRegister.getInstance().process(command);
		Category pc = (Category) command.getObject();

		/*if (pc != null) {
			responseStatus = SUCCESS;
			getData().put("categoryId", pc.getId());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/

		return responseStatus;
	}
	
	public String getCategory() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData, "billing/menu/category/get");
		List<Object> categories = (new CategoryQueryHandler()).get(q);

		/*if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("categories", categories);
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/

		return responseStatus;
	}

}
