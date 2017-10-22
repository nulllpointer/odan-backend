package com.odan.billing.menu.product.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.product.ProductPriceQueryHandler;
import com.odan.billing.menu.product.ProductQueryHandler;
import com.odan.billing.menu.product.command.*;
import com.odan.billing.menu.product.model.Product;
import com.odan.billing.menu.product.model.ProductPrice;
import com.odan.common.api.RestAction;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
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
	@Action(value = "sales/product-price", results = {@Result(type = "json")})
	public String actionProductPrice() throws ValidationException, CommandException, ParseException, JsonProcessingException {
		String responseStatus = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("GET")) {
			responseStatus = getProductPrice();
		} else if (httpRequest.getMethod().equals("POST")) {
			responseStatus = createProductPrice();
		} else if (httpRequest.getMethod().equals("PUT")) {
			responseStatus = updateProductPrice();
		} else {
			responseStatus = "HttpMethodNotAccepted";
		}

		return responseStatus;
	}

    public String actionProductPriceById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getProductPriceById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deleteProductPrice(id);
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
			setJsonResponseForCreate(c, Flags.EntityType.PRODUCT);

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
	public String createProductPrice() throws JsonProcessingException, CommandException, ParseException, ValidationException {
		String responseStatus = SUCCESS;
		System.out.println("..Create Customer Request");
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

		if (requestData.containsKey("id")) {
			UpdateProductPrice command = new UpdateProductPrice(requestData);
			CommandRegister.getInstance().process(command);
			ProductPrice c = (ProductPrice) command.getObject();
			setJsonResponseForUpdate(c);


		} else {
			CreateProductPrice command = new CreateProductPrice(requestData);
			CommandRegister.getInstance().process(command);
			ProductPrice c = (ProductPrice) command.getObject();
			setJsonResponseForCreate(c, Flags.EntityType.PRODUCT_PRICE);

		}


		return responseStatus;
	}

	public String updateProductPrice() throws JsonProcessingException, CommandException, ParseException, ValidationException {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateProductPrice command = new UpdateProductPrice(requestData);
		CommandRegister.getInstance().process(command);
		ProductPrice c = (ProductPrice) command.getObject();
		setJsonResponseForUpdate(c);


		return SUCCESS;
	}

	public String getProductPrice() throws ParseException {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

		Long productId = null;
		Timestamp txnDate = null;
		if (requestData.containsKey("productId")) {
			productId = Parser.convertObjectToLong(requestData.get("productId"));
		}
		if (requestData.containsKey("txnDate")) {
			txnDate = Parser.convertObjectToTimestamp(requestData.get("txnDate"));
		}

		ProductPrice productPrice = (new ProductPriceQueryHandler()).getProductPrice(productId, txnDate);


		return setJsonResponseForGetById(productPrice);
	}


	public String getProductPriceById(Long id) {
		ProductPrice productPrice = null;
		try {
			productPrice = (ProductPrice) (new ProductPriceQueryHandler()).getById(id);
		} catch (Exception e) {
			if (e instanceof CommandException) {
				APILogger.add(APILogType.ERROR, "Permission denied");
			}

		}
		return setJsonResponseForGetById(productPrice);
	}

	public String deleteProductPrice(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
		String responseStatus = SUCCESS;
		System.out.println("Delete NormalUser");
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		requestData.put("id", id);
		DeleteProductPrice command = new DeleteProductPrice(requestData);
		CommandRegister.getInstance().process(command);
		Boolean c = (Boolean) command.getObject();

		setJsonResponseForDelete(c);

		return responseStatus;
	}


	public String actionProductPriceDeleteById() throws Exception {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		String[] val = httpRequest.getServletPath().split("/");

		Long id = Long.parseLong(val[val.length - 1]);
		if (httpRequest.getMethod().equals("GET")) {
			response = deleteProductPrice(id);
		} else {

			response = "HttpMethodNotAccepted";
		}

		return response;
	}




}
