package com.odan.common.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingMessage;

public class JSONValidatorLog {
	private String level;
	private String pointer;
	private String validation;
	private String message;
	private String expected;

	private ProcessingMessage processingMessage;

	public JSONValidatorLog(ProcessingMessage m) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.convertValue(m.asJson(), Map.class);

		this.processingMessage = m;
		this.level = (String) result.get("level");
		this.message = (String) result.get("message");
		this.validation = (String) result.get("keyword");
		Object instance = result.get("instance");
		if (instance != null) {
			this.pointer = (String) ((HashMap<String, Object>) result.get("instance")).get("pointer");
		}

		if (result.containsKey("expected")) {
			ArrayList<String> list = new ArrayList<>();
			try {
				list = (ArrayList<String>) result.get("expected");
			} catch (Exception e) {
				list.add((String) result.get("expected"));
			}

			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (i = 0; i < list.size() - 1; i++) {
				sb.append(list.get(i));
				sb.append("  OR  ");
			}
			sb.append(list.get(i));
			this.expected = sb.toString();
		} else if (result.containsKey("attribute")) {
			this.expected = (String) result.get("attribute");
		}
	}

	public String getLevel() {
		return level;
	}

	public String getPointer() {
		return pointer;
	}

	public String getValidation() {
		return validation;
	}

	public String getMessage() {
		return message;
	}

	public String getExpected() {
		return expected;
	}

	public String getFormattedMessage() {
		return this.getPointer() + " : " + this.getMessage();
	}
}
