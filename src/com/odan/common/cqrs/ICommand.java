package com.odan.common.cqrs;

import java.util.HashMap;

import org.hibernate.Transaction;

public interface ICommand {

	public boolean has(String name);

	public void set(String name, Object value);

	public Object get(String name);

	public HashMap<String,Object> getData();

	public boolean isCommittable();

	public boolean validate();

	public void setObject(Object obj);

	public Object getObject();

	public Transaction getTransaction();

	public boolean isValid();
}
