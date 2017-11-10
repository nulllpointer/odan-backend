package com.odan.common.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.odan.common.cqrs.Query;
import com.odan.common.model.Flags.EntityType;
import com.odan.common.shared.model.AbstractEntity;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;
import com.odan.security.user.model.User;
import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.Action;

import static org.json.XMLTokener.entity;

public abstract class RestAction extends BaseAction {


    private static final long serialVersionUID = 8653391256322997267L;

    public Map<String, Object> getJSONRequest(HttpServletRequest req) {
        System.out.println(req.getServletPath());
        StringBuilder buffer = new StringBuilder();
        String json = "";
        Map<String, Object> map = new HashMap<>();
        BufferedReader reader;
        try {
            reader = req.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            json = buffer.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (json != null) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

    public ArrayList<HashMap<String, Object>> getJSONRequestForBulk(HttpServletRequest req) {
        System.out.println(req.getServletPath());
        StringBuilder buffer = new StringBuilder();
        String json = "";
        ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = req.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            json = buffer.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (json != null) {
            try {
                // List<Map<String, Object>> maps = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();
                maps = mapper.readValue(json, new TypeReference<List<HashMap<String, Object>>>() {


                });
                System.out.println(maps);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return maps;
    }

    public Map<String, Object> getQueryRequest(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String[]> query = req.getParameterMap();
        for (String key : query.keySet()) {
            String[] arr = query.get(key);
            if (arr.length >= 1) {
                map.put(key, arr[0]);
            }
        }
        return map;
    }

    public RestAction() {
        HttpServletRequest req = ServletActionContext.getRequest();
        String method = req.getMethod();
        if (method.equals("GET")) {
            this.setRequest(getQueryRequest(req));
        } else if (req.getServletPath().contains("/bulk") && (method.equals("PUT") | method.equals("POST"))) {

            this.setRequests(getJSONRequestForBulk(req));
        } else if(method.equals("DELETE")){
            System.out.println("do nothing");
        }

        else {
            this.setRequest(getJSONRequest(req));
        }


    }

    public String execute() {
        return Action.SUCCESS;
    }

    protected String setJsonResponseForUpdate(Object c) {
        if (c != null) {
            HashMap<String, Object> map = new HashMap<>();

            AbstractEntity entity = (AbstractEntity) c;
            map.put("result", SUCCESS);
            map.put("message", "Id : " + entity.getId() + " successfully updated");
            setJsonResponse(map);
            return SUCCESS;
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            setJsonResponse(map);
            return ERROR;
        }
    }



    protected String setJsonResponseForLogin(List<Object> value) {
        HashMap<String, Object> map = new HashMap<>();


        if (value!=null) {
       //     map.put("result", SUCCESS);
            map.put("user", value);
            setJsonResponse(map);
            return SUCCESS;



        } else {

            map.put("result", ERROR);
            setJsonResponse(map);
            return ERROR;


        }
    }




    protected String setJsonResponseForDelete(Boolean value) {
        HashMap<String, Object> map = new HashMap<>();


        if (value) {
            map.put("result", SUCCESS);
            map.put("message", " " + "successfully deleted");
            setJsonResponse(map);

            return SUCCESS;


        } else {

            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            setJsonResponse(map);

            return ERROR;

        }
    }

    protected String setJsonResponseForGet(Query q, List<Object> val) {

        HashMap<String, Object> map = new HashMap<>();
        if (q.validate()) {
            map.put("data", Parser.convert(val));
            setJsonResponse(map);
            return SUCCESS;
        } else {
            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            APILogger.clear();
            return ERROR;
        }


    }


    protected String setJsonResponseForGet(List<Object> val) {
        HashMap<String, Object> map = new HashMap<>();
        if (val != null) {


            map.put("data", Parser.convert(val));
            setJsonResponse(map);
            //  getData().put("logs", APILogger.getList());
            //setJsonResponse();
            return SUCCESS;
        } else {

            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            APILogger.clear();
            return ERROR;
        }
    }

    protected String setJsonResponseForGet(List<Object> val, String nodeValue) {
        HashMap<String, Object> map = new HashMap<>();
        if (val != null) {


            map.put(nodeValue, Parser.convert(val));
            setJsonResponse(map);
            //  getData().put("logs", APILogger.getList());
            //setJsonResponse();
            return SUCCESS;
        } else {

            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            APILogger.clear();
            return ERROR;
        }


    }

    protected String setJsonResponseForGetById(Object obj) {
        HashMap<String, Object> map = new HashMap<>();
        if (obj != null) {
            map.put("data", Parser.convert(obj));
            setJsonResponse(map);
            return SUCCESS;
        } else {
            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            setJsonResponse(map);
            APILogger.clear();
            return ERROR;
        }
    }

    protected void setJsonResponseForBulkRemove(String bta) {
        HashMap<String, Object> map = new HashMap<>();


        if (bta != null) {
            map.put("result", SUCCESS);
            map.put("message", "Entities successfully deleted");

            setJsonResponse(map);
        } else {
            map.put("result", ERROR);

            map.put("log", APILogger.getList());

            setJsonResponse(map);

            APILogger.clear();
        }
    }

    protected void setJsonResponseForBulkUpdate(List<Object> bta) {
        HashMap<String, Object> map = new HashMap<>();
        if (bta != null) {
            map.put("result", SUCCESS);
            map.put("message", "Entities successfully updated");

            setJsonResponse(map);
        } else {
            map.put("result", ERROR);

            map.put("log", APILogger.getList());

            setJsonResponse(map);

            APILogger.clear();
        }
    }

    protected String setStandardJsonResponse(List<Object> bta, Object msg) {
        HashMap<String, Object> map = new HashMap<>();
        if (bta != null) {
            map.put("result", SUCCESS);
            map.put("ids", bta);
            map.put("message", msg);

            setJsonResponse(map);
            return SUCCESS;
        } else {
            map.put("result", ERROR);

            map.put("log", APILogger.getList());

            setJsonResponse(map);

            APILogger.clear();
            return ERROR;
        }
    }

    protected String setJsonResponseForCreate(Object c, EntityType type) {
        if (c != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("result", SUCCESS);
            map.put("id", ((AbstractEntity) c).getId());

            map.put("message", type + " successfully added");
            setJsonResponse(map);
            return SUCCESS;
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("result", ERROR);
            map.put("log", APILogger.getList());
            setJsonResponse(map);
            return ERROR;
        }
    }
}
