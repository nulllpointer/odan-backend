package com.odan.billing.menu.category.api;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.menu.category.CategoryQueryHandler;
import com.odan.billing.menu.category.command.CreateCategory;
import com.odan.billing.menu.category.command.DeleteCategory;
import com.odan.billing.menu.category.command.UpdateCategory;
import com.odan.billing.menu.category.model.Category;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/billing")
public class CategoryResource extends RestAction {

    @Action(value = "category", results = { @Result(type = "json") })
    public String actionCategory() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        if (httpRequest.getMethod().equals("POST")) {
            createCategory();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updateCategory();
        } else if (httpRequest.getMethod().equals("GET")) {
            getCategory();
        } else {
            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String createCategory() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("..Create Category Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

        if (requestData.containsKey("id")) {
            UpdateCategory command = new UpdateCategory(requestData);
            CommandRegister.getInstance().process(command);
            Category c = (Category) command.getObject();
            setJsonResponseForUpdate(c);


        } else {
            CreateCategory command = new CreateCategory(requestData);
            CommandRegister.getInstance().process(command);
            Category c = (Category) command.getObject();
            setJsonResponseForCreate(c, Flags.EntityType.CATEGORY);

        }


        return responseStatus;
    }

    public String updateCategory() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdateCategory command = new UpdateCategory(requestData);
        CommandRegister.getInstance().process(command);
        Category c = (Category) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getCategory() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);

        List<Object> customers = (new CategoryQueryHandler()).get(q);


        return setJsonResponseForGet(customers, "categorys");
    }

    public String actionCategoryById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getCategoryById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deleteCategory(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getCategoryById(Long id) {
        Category normalCategory = null;
        try {
            normalCategory = (Category) (new CategoryQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalCategory);
    }

    public String deleteCategory(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("Delete NormalCategory");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        requestData.put("id",id);
        DeleteCategory command = new DeleteCategory(requestData);
        CommandRegister.getInstance().process(command);
        Boolean c = (Boolean) command.getObject();

        setJsonResponseForDelete(c);

        return responseStatus;
    }

}
