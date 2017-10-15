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
import com.odan.inventory.sales.CartQueryHandler;
import com.odan.inventory.sales.command.CreateCart;
import com.odan.inventory.sales.command.DeleteCart;
import com.odan.inventory.sales.command.UpdateCart;
import com.odan.inventory.sales.model.Cart;
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
public class CartResource extends RestAction {


    @Action(value = "cart", results = {@Result(type = "json")})
    public String actionCart() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();


         if (httpRequest.getMethod().equals("POST")) {
            createCart();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updateCart();
        } else if (httpRequest.getMethod().equals("GET")) {
            getCart();
        } else if (httpRequest.getMethod().equals("DELETE")) {

            // deleteCart(id);

            ;
        } else {
            response = "HttpMethodNotAccepted";
        }


        return response;
    }


    public String createCart() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("..Create Customer Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

        if (requestData.containsKey("id")) {
            UpdateCart command = new UpdateCart(requestData);
            CommandRegister.getInstance().process(command);
            Cart c = (Cart) command.getObject();
            setJsonResponseForUpdate(c);


        } else {
            CreateCart command = new CreateCart(requestData);
            CommandRegister.getInstance().process(command);
            Cart c = (Cart) command.getObject();
            setJsonResponseForCreate(c, Flags.EntityType.CONTACTS);

        }


        return responseStatus;
    }

    public String updateCart() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdateCart command = new UpdateCart(requestData);
        CommandRegister.getInstance().process(command);
        Cart c = (Cart) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getCart() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);

        List<Object> customers = (new CartQueryHandler()).get(q);


        return setJsonResponseForGet(customers, "carts");
    }

    public String actionCartById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getCartById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deleteCart(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getCartById(Long id) {
        Cart normalUser = null;
        try {
            normalUser = (Cart) (new CartQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalUser);
    }

    public String deleteCart(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("Delete NormalUser");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        requestData.put("id", id);
        DeleteCart command = new DeleteCart(requestData);
        CommandRegister.getInstance().process(command);
        Boolean c = (Boolean) command.getObject();

        setJsonResponseForDelete(c);

        return responseStatus;
    }


    public String actionCartDeleteById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = deleteCart(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }


}
