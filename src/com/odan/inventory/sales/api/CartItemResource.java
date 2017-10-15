package com.odan.inventory.sales.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.common.api.RestAction;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.inventory.sales.CartItemQueryHandler;
import com.odan.inventory.sales.command.CreateCartItem;
import com.odan.inventory.sales.command.DeleteCartItem;
import com.odan.inventory.sales.command.UpdateCartItem;
import com.odan.inventory.sales.model.CartItem;
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
public class CartItemResource extends RestAction {


    @Action(value = "cart", results = {@Result(type = "json")})
    public String actionCartItem() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();


         if (httpRequest.getMethod().equals("POST")) {
            createCartItem();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updateCartItem();
        } else if (httpRequest.getMethod().equals("GET")) {
            getCartItem();
        } else if (httpRequest.getMethod().equals("DELETE")) {

            // deleteCartItem(id);

            ;
        } else {
            response = "HttpMethodNotAccepted";
        }


        return response;
    }


    public String createCartItem() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("..Create Customer Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

        if (requestData.containsKey("id")) {
            UpdateCartItem command = new UpdateCartItem(requestData);
            CommandRegister.getInstance().process(command);
            CartItem c = (CartItem) command.getObject();
            setJsonResponseForUpdate(c);


        } else {
            CreateCartItem command = new CreateCartItem(requestData);
            CommandRegister.getInstance().process(command);
            CartItem c = (CartItem) command.getObject();
            setJsonResponseForCreate(c, Flags.EntityType.CART_ITEM);

        }


        return responseStatus;
    }

    public String updateCartItem() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdateCartItem command = new UpdateCartItem(requestData);
        CommandRegister.getInstance().process(command);
        CartItem c = (CartItem) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getCartItem() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);

        List<Object> customers = (new CartItemQueryHandler()).get(q);


        return setJsonResponseForGet(customers, "carts");
    }

    public String actionCartItemById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getCartItemById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deleteCartItem(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getCartItemById(Long id) {
        CartItem normalUser = null;
        try {
            normalUser = (CartItem) (new CartItemQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalUser);
    }

    public String deleteCartItem(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("Delete NormalUser");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        requestData.put("id", id);
        DeleteCartItem command = new DeleteCartItem(requestData);
        CommandRegister.getInstance().process(command);
        Boolean c = (Boolean) command.getObject();

        setJsonResponseForDelete(c);

        return responseStatus;
    }


    public String actionCartItemDeleteById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = deleteCartItem(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }


}
