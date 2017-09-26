package com.odan.common.cqrs;

import java.util.HashMap;

public interface IEvent {

	public boolean has(String name);

	public void set(String name, Object value);

	public Object get(String name);

	public HashMap<String,Object> getData();
	
	public void setObject(Object obj);

	public Object getObject();
}
