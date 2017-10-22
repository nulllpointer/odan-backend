package com.odan.billing.menu.product.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.product.ProductQueryHandler;
import com.odan.billing.menu.product.command.CreateProduct;
import com.odan.billing.menu.product.command.DeleteProduct;
import com.odan.billing.menu.product.command.UpdateProduct;
import com.odan.billing.menu.product.model.Product;
import com.odan.common.api.RestAction;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/billing")
public class ProductResource extends RestAction {


	@Action(value = "product", results = {@Result(type = "json")})
	public String actionProduct() throws ValidationException, CommandException, ParseException, JsonProcessingException {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();


		if (httpRequest.getMethod().equals("POST")) {
			createProduct();
		} else if (httpRequest.getMethod().equals("PUT")) {
			updateProduct();
		} else if (httpRequest.getMethod().equals("GET")) {
			getProduct();
		} else if (httpRequest.getMethod().equals("DELETE")) {

			// deleteProduct(id);

			;
		} else {
			response = "HttpMethodNotAccepted";
		}


		return response;
	}


	public String createProduct() throws JsonProcessingException, CommandException, ParseException, ValidationException {
		String responseStatus = SUCCESS;
		System.out.println("..Create Customer Request");
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

		if (requestData.containsKey("id")) {
			UpdateProduct command = new UpdateProduct(requestData);
			CommandRegister.getInstance().process(command);
			Product c = (Product) command.getObject();
			setJsonResponseForUpdate(c);


		} else {
			CreateProduct command = new CreateProduct(requestData);
			CommandRegister.getInstance().process(command);
			Product c = (Product) command.getObject();
			setJsonResponseForCreate(c, Flags.EntityType.CONTACTS);

		}


		return responseStatus;
	}

	public String updateProduct() throws JsonProcessingException, CommandException, ParseException, ValidationException {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateProduct command = new UpdateProduct(requestData);
		CommandRegister.getInstance().process(command);
		Product c = (Product) command.getObject();
		setJsonResponseForUpdate(c);


		return SUCCESS;
	}

	public String getProduct() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData);

		List<Object> products = (new ProductQueryHandler()).get(q);


		return setJsonResponseForGet(products, "products");
	}

	public String actionProductById() throws Exception {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		String[] val = httpRequest.getServletPath().split("/");

		Long id = Long.parseLong(val[val.length - 1]);
		if (httpRequest.getMethod().equals("GET")) {
			response = getProductById(id);
		} else if (httpRequest.getMethod().equals("DELETE")) {

			deleteProduct(id);
		} else {

			response = "HttpMethodNotAccepted";
		}

		return response;
	}

	public String getProductById(Long id) {
		Product normalUser = null;
		try {
			normalUser = (Product) (new ProductQueryHandler()).getById(id);
		} catch (Exception e) {
			if (e instanceof CommandException) {
				APILogger.add(APILogType.ERROR, "Permission denied");
			}

		}
		return setJsonResponseForGetById(normalUser);
	}

	public String deleteProduct(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
		String responseStatus = SUCCESS;
		System.out.println("Delete NormalUser");
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		requestData.put("id", id);
		DeleteProduct command = new DeleteProduct(requestData);
		CommandRegister.getInstance().process(command);
		Boolean c = (Boolean) command.getObject();

		setJsonResponseForDelete(c);

		return responseStatus;
	}


	public String actionProductDeleteById() throws Exception {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		String[] val = httpRequest.getServletPath().split("/");

		Long id = Long.parseLong(val[val.length - 1]);
		if (httpRequest.getMethod().equals("GET")) {
			response = deleteProduct(id);
		} else {

			response = "HttpMethodNotAccepted";
		}

		return response;
	}


}
