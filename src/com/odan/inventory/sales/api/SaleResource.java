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
import com.odan.inventory.sales.SaleQueryHandler;
import com.odan.inventory.sales.command.*;
import com.odan.inventory.sales.model.Cart;
import com.odan.inventory.sales.model.Sale;
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
public class SaleResource extends RestAction {


    @Action(value = "sales", results = {@Result(type = "json")})
    public String actionSale() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();


         if (httpRequest.getMethod().equals("POST")) {
            createSale();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updateSale();
        } else if (httpRequest.getMethod().equals("GET")) {
            getSale();
        } else if (httpRequest.getMethod().equals("DELETE")) {

            // deleteSale(id);

            ;
        } else {
            response = "HttpMethodNotAccepted";
        }


        return response;
    }


    public String createSale() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("..Create Customer Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

        if (requestData.containsKey("id")) {
            UpdateSale command = new UpdateSale(requestData);
            CommandRegister.getInstance().process(command);
            Sale c = (Sale) command.getObject();
            setJsonResponseForUpdate(c);


        } else {
            CreateSale command = new CreateSale(requestData);
            CommandRegister.getInstance().process(command);
            Sale c = (Sale) command.getObject();
            setJsonResponseForCreate(c, Flags.EntityType.SALES);

        }


        return responseStatus;
    }

    public String updateSale() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdateSale command = new UpdateSale(requestData);
        CommandRegister.getInstance().process(command);
        Sale c = (Sale) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getSale() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);

        List<Object> customers = (new SaleQueryHandler()).get(q);


        return setJsonResponseForGet(customers, "sales");
    }

    public String actionSaleById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getSaleById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deleteSale(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getSaleById(Long id) {
        Sale normalUser = null;
        try {
            normalUser = (Sale) (new SaleQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalUser);
    }

    public String deleteSale(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("Delete NormalUser");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        requestData.put("id", id);
        DeleteSale command = new DeleteSale(requestData);
        CommandRegister.getInstance().process(command);
        Boolean c = (Boolean) command.getObject();

        setJsonResponseForDelete(c);

        return responseStatus;
    }


    public String actionSaleDeleteById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = deleteSale(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }


}
