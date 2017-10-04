package com.odan.billing.contact.api;

import com.odan.billing.contact.ContactQueryHandler;
import com.odan.billing.contact.command.CreateContact;
import com.odan.billing.contact.command.UpdateContact;
import com.odan.billing.contact.model.Contact;
import com.odan.common.api.RestAction;
import com.odan.common.application.CommandException;
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
import java.util.HashMap;
import java.util.List;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/billing")
public class ContactResource extends RestAction {

    @Action(value = "contact", results = {@Result(type = "json")})
    public String actionContact() {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();


        if (httpRequest.getMethod().equals("POST")) {
            createContact();
        } else if (httpRequest.getMethod().equals("PUT")) {
            updateContact();
        } else if (httpRequest.getMethod().equals("GET")) {
            getContact();
        } else if (httpRequest.getMethod().equals("DELETE")) {
           // deleteContact(id);
        } else {
            response = "HttpMethodNotAccepted";
        }

        return response;
    }

  /*  private String deleteContact(Long id) {

        DeleteContact command = new DeleteContact(requestData);
        CommandRegister.getInstance().process(command);
        boolean c = (boolean) command.getObject();
        setJsonResponseForDelete(c);
        return SUCCESS;
    }
*/
    public String createContact() {
        String responseStatus = SUCCESS;
        System.out.println("..Create Customer Request");
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        CreateContact command = new CreateContact(requestData);
        CommandRegister.getInstance().process(command);
        Contact c = (Contact) command.getObject();

		/*if (c != null) {
            responseStatus = SUCCESS;
			setSuccess("System Customer synced successfully.");
			getData().put("customerId", c.getId().toString());
		} else {
			setError("System Customer sync failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/
        setJsonResponseForCreate(c, Flags.EntityType.CONTACTS);

        return responseStatus;
    }

    public String updateContact() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        UpdateContact command = new UpdateContact(requestData);
        CommandRegister.getInstance().process(command);
        Contact c = (Contact) command.getObject();
        setJsonResponseForUpdate(c);


        return SUCCESS;
    }

    public String getContact() {
        String responseStatus = SUCCESS;
        HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
        Query q = new Query(requestData);

        List<Object> customers = (new ContactQueryHandler()).get(q);


        return setJsonResponseForGet(customers, "contacts");
    }

    public String actionContactById() throws Exception {
        String response = SUCCESS;
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        String[] val = httpRequest.getServletPath().split("/");

        Long id = Long.parseLong(val[val.length - 1]);
        if (httpRequest.getMethod().equals("GET")) {
            response = getContactById(id);
        } else if (httpRequest.getMethod().equals("DELETE")) {

           //deleteContact(id);
        } else {

            response = "HttpMethodNotAccepted";
        }

        return response;
    }

    public String getContactById(Long id) {
        Contact normalUser = null;
        try {
            normalUser = (Contact) (new ContactQueryHandler()).getById(id);
        } catch (Exception e) {
            if (e instanceof CommandException) {
                APILogger.add(APILogType.ERROR, "Permission denied");
            }

        }
        return setJsonResponseForGetById(normalUser);
    }


}
