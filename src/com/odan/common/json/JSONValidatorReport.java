package com.odan.common.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;

public class JSONValidatorReport {
	private boolean valid;
	private ProcessingReport r;
	
	public JSONValidatorReport(ProcessingReport r) {
		this.r = r;
		valid = r.isSuccess();
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public List<JSONValidatorLog> getReport() {
		List<JSONValidatorLog> log = new ArrayList<JSONValidatorLog>();
		Iterator<ProcessingMessage> plist = this.r.iterator();
		
		while(plist.hasNext()) {
			log.add(new JSONValidatorLog(plist.next()));			
		}
		return log;
	}
	
	
}
