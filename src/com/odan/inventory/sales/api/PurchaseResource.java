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
import com.odan.inventory.purchase.PurchaseQueryHandler;
import com.odan.inventory.purchase.command.CreatePurchase;
import com.odan.inventory.purchase.command.DeletePurchase;
import com.odan.inventory.purchase.command.UpdatePurchase;
import com.odan.inventory.purchase.model.Purchase;
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
public class PurchaseResource extends RestAction {


    @Action(value = "purchase", results = {@Result(type = "json")})
    public String actionPurchase() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();


         if (httpRequest.getMethod().equals("POST")) {
            createPurchase();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updatePurchase();
        } else if (httpRequest.getMethod().equals("GET")) {
            getPurchase();
        } else if (httpRequest.getMethod().equals("DELETE")) {

            // deletePurchase(id);

            ;
        } else {
            response = "HttpMethodNotAccepted";
        }


        return response;
    }


    public String createPurchase() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("..Create Customer Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

        if (requestData.containsKey("id")) {
            UpdatePurchase command = new UpdatePurchase(requestData);
            CommandRegister.getInstance().process(command);
            Purchase c = (Purchase) command.getObject();
            setJsonResponseForUpdate(c);


        } else {
            CreatePurchase command = new CreatePurchase(requestData);
            CommandRegister.getInstance().process(command);
            Purchase c = (Purchase) command.getObject();
            setJsonResponseForCreate(c, Flags.EntityType.CONTACTS);

        }


        return responseStatus;
    }

    public String updatePurchase() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdatePurchase command = new UpdatePurchase(requestData);
        CommandRegister.getInstance().process(command);
        Purchase c = (Purchase) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getPurchase() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);

        List<Object> customers = (new PurchaseQueryHandler()).get(q);


        return setJsonResponseForGet(customers, "purchases");
    }

    public String actionPurchaseById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getPurchaseById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deletePurchase(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getPurchaseById(Long id) {
        Purchase normalUser = null;
        try {
            normalUser = (Purchase) (new PurchaseQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalUser);
    }

    public String deletePurchase(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("Delete NormalUser");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        requestData.put("id", id);
        DeletePurchase command = new DeletePurchase(requestData);
        CommandRegister.getInstance().process(command);
        Boolean c = (Boolean) command.getObject();

        setJsonResponseForDelete(c);

        return responseStatus;
    }


    public String actionPurchaseDeleteById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = deletePurchase(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }


}
