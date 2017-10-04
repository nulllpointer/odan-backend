package com.odan.common.cqrs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Transaction;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.odan.common.database.HibernateUtils;
import com.odan.common.json.JSONValidatorEngine;
import com.odan.common.json.JSONValidatorLog;
import com.odan.common.json.JSONValidatorReport;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;

public class Command implements ICommand {
	protected Transaction trx;
	protected HashMap<String, Object> data;
	protected Object obj;
	protected String
			validationSchema;
	protected boolean isValid = true;
	private boolean isCommittable = false;

	public Command() {
		HibernateUtils.openSession();
		this.trx = HibernateUtils.beginTransaction();
		this.data = new HashMap<String, Object>();
		this.obj = null;
	}

	public Command(HashMap<String, Object> params) {
		HibernateUtils.openSession();
		this.trx = HibernateUtils.beginTransaction();
		this.isCommittable = true;
		this.data = params;
		this.obj = null;
	}

	public Command(HashMap<String, Object> params, Transaction trx) {

		if (trx != null) {
			this.trx = trx;
			this.isCommittable = false;
		} else {
			HibernateUtils.openSession();
			this.trx = HibernateUtils.beginTransaction();
			this.isCommittable = true;
		}
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

	public boolean 	has(String name) {
		if (this.data.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCommittable() {
		return this.isCommittable;
	}

	public boolean validate() {
		boolean valid = false;
		if (this.validationSchema != null) {
			JSONValidatorReport report = null;
			try {
				report = JSONValidatorEngine.validateRequest(this.validationSchema, this.data);
			} catch (IOException | ProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (report.isValid()) {
				valid = true;
			} else {
				valid = false;
				List<JSONValidatorLog> logs = report.getReport();
				for (JSONValidatorLog log : logs) {
					APILogger.add(APILogType.ERROR, log.getFormattedMessage());
				}
			}
		} else {
			valid = true;
		}
		this.isValid = valid;

		return valid;
	}

	public void setObject(Object obj) {
		this.obj = obj;
	}

	public Object getObject() {
		return this.obj;
	}

	public Transaction getTransaction() {
		return this.trx;
	}

	public boolean isValid() {
		return this.isValid;
	}
}