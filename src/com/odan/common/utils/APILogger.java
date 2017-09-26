package com.odan.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.odan.common.application.Authentication;

public class APILogger {

	private APILogger() {

	}

	private static Map getSession() {
		Map session = null;
		try {
			session = ActionContext.getContext().getSession();
		} catch (Exception e) {
			session = new HashMap<>();
		}
		return session;
	}

	private static String getKey() {
		Long userId = null;
		String key = "";
		try {
			userId = Authentication.getUserId();
			if (userId == null) {
				userId = 0L;
			}
			key = "API_LOG_USER_" + userId.toString();
		}
		catch(Exception ex) {
			userId = 0L;
			key = "CRON";
		}
		return key;
	}

	public static void add(APILogType type, String message) {
		APILogger.add(type, message, null);
	}

	public static void add(APILogType type, String message, Exception exception) {
		ArrayList<APILog> list = getList();
		
		//If running in quartz
		if(list == null) {
			return;
		}
		APILog log = new APILog(type, message, exception);
		list.add(log);
		updateList(list);
	}

	public static ArrayList<APILog> getList() {
		Map session = getSession();
		String key = getKey();
		ArrayList<APILog> list = null;
		
		//If running in quartz
		if(key == "CRON") {
			return list;
		}
		
		if (session.containsKey(key)) {
			list = (ArrayList<APILog>) session.get(key);
		} else {
			list = new ArrayList<APILog>();
		}
		return list;
	}

	private static boolean updateList(ArrayList<APILog> newList) {
		Map session = getSession();
		String key = getKey();
		session.put(key, newList);
		return true;
	}

	public static boolean clear() {
		Map session = getSession();
		String key = getKey();
		session.remove(key);
		return true;
	}

	public static boolean hasError() {
		boolean hasError = false;
		ArrayList<APILog> list = getList();
		for (APILog log : list) {
			if (log.getType().getFlag() >= APILogType.ERROR.getFlag()) {
				hasError = true;
			}
		}
		return hasError;
	}

}
