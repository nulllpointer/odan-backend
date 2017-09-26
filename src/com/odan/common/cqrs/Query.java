package com.odan.common.cqrs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.odan.common.json.JSONValidatorEngine;
import com.odan.common.json.JSONValidatorLog;
import com.odan.common.json.JSONValidatorReport;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;

public class Query {
	private HashMap<String, Object> query;
	private String validationSchema;

	public Query() {
		this.query = new HashMap<String, Object>();
		this.validationSchema = null;
	}

	public Query(HashMap<String, Object> params) {
		if (params != null) {
			this.query = params;
		} else {
			this.query = new HashMap<String, Object>();
		}
		this.validationSchema = null;
	}

	public Query(HashMap<String, Object> params, String schema) {
		this(params);
		this.validationSchema = schema;
	}

	public Object get(String name) {
		Object param = null;
		if (this.has(name)) {
			param = this.query.get(name);
		}
		return param;
	}

	public void set(String name, Object value) {
		this.query.put(name, value);
	}

	public boolean has(String name) {
		if (this.query.containsKey(name) && this.query.get(name) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validate() {
		boolean valid = true;
		if (this.validationSchema != null) {
			valid = false;
			JSONValidatorReport report = null;
			try {
				report = JSONValidatorEngine.validateRequest(this.validationSchema, this.query);
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
					APILogger.add(APILogType.ERROR, log.getMessage());
				}
			}
		}

		return valid;
	}

}
