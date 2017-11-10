package com.odan.security.user.api;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.security.user.command.DeleteUser;
import com.odan.security.user.command.LoginUser;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.security.user.UserQueryHandler;
import com.odan.security.user.command.CreateUser;
import com.odan.security.user.command.UpdateUser;
import com.odan.security.user.model.User;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/security")
public class UserResource extends RestAction {

    @Action(value = "user", results = {@Result(type = "json")})
    public String actionUser() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        if (httpRequest.getMethod().equals("POST")) {
            createUser();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updateUser();
        } else if (httpRequest.getMethod().equals("GET")) {
            getUser();
        } else {
            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String createUser() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("..Create Customer Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

        if (requestData.containsKey("id")) {
            UpdateUser command = new UpdateUser(requestData);
            CommandRegister.getInstance().process(command);
            User c = (User) command.getObject();
            setJsonResponseForUpdate(c);


        }
       else {
            CreateUser command = new CreateUser(requestData);
            CommandRegister.getInstance().process(command);
            User c = (User) command.getObject();
            setJsonResponseForCreate(c, Flags.EntityType.USER);

        }


        return responseStatus;
    }

    public String updateUser() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdateUser command = new UpdateUser(requestData);
        CommandRegister.getInstance().process(command);
        User c = (User) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getUser() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);
        List<Object> users = (new UserQueryHandler()).get(q);
        return setJsonResponseForGet(users, "users");
    }

    public String actionUserById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getUserById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

            deleteUser(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getUserById(Long id) {
        User normalUser = null;
        try {
            normalUser = (User) (new UserQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalUser);
    }

    public String deleteUser(Long id) throws JsonProcessingException, CommandException, ParseException, ValidationException {
        String responseStatus = SUCCESS;
        System.out.println("Delete NormalUser");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        requestData.put("id", id);
        DeleteUser command = new DeleteUser(requestData);
        CommandRegister.getInstance().process(command);
        Boolean c = (Boolean) command.getObject();

        setJsonResponseForDelete(c);

        return responseStatus;
    }

    @Action(value = "login", results = {@Result(type = "json")})
    public String actionLogin() throws ValidationException, CommandException, ParseException, JsonProcessingException {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        if (httpRequest.getMethod().equals("POST")) {
            loginUser();
        } else {
            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public User loginUser() throws JsonProcessingException, CommandException, ParseException, ValidationException {
        User user=new User();
        String responseStatus = SUCCESS;
        System.out.println("Check NormalUser");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

            LoginUser command = new LoginUser(requestData);
            CommandRegister.getInstance().process(command);
            Query q=new Query(requestData);
            UserQueryHandler c=new UserQueryHandler();
            List<Object> users=c.get(q);
            if (users.size()==1){
                setJsonResponseForGetById(users.get(0));


            }


            return user;
    }



}
