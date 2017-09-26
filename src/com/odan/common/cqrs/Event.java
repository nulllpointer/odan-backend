package com.odan.common.cqrs;

import java.util.HashMap;

import org.hibernate.Transaction;

public class Event implements IEvent {
	protected HashMap<String, Object> data;
	protected Object obj;

	public Event() {
		this.data = new HashMap<String, Object>();
		this.obj = null;
	}

	public Event(HashMap<String, Object> params) {
		this.data = params;
		this.obj = null;
	}

	public Event(HashMap<String, Object> params, Transaction trx) {
		this.data = params;
		this.obj = null;
	}

	public Object get(String name) {
		Object param = null;
		if (this.has(name)) {
			param = this.data.get(name);
		}
		return param;
	}

	public void set(String name, Object value) {
		if (this.data == null) {
			this.data = new HashMap<String, Object>();
		}
		this.data.put(name, value);
	}

	public HashMap<String, Object> getData() {
		return this.data;
	}

	public boolean has(String name) {
		if (this.data.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	public void setObject(Object obj) {
		this.obj = obj;
	}

	public Object getObject() {
		return this.obj;
	}
}
