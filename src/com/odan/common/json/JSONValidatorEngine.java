package com.odan.common.json;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.format.draftv3.DateAttribute;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.odan.common.utils.IO;

public class JSONValidatorEngine {

	public static JSONValidatorReport validate(String schemaString, String jsonString)
			throws JsonProcessingException, IOException, ProcessingException {
		ObjectMapper mapper1 = new ObjectMapper();
		JsonNode json = mapper1.readTree(jsonString);

		ObjectMapper mapper2 = new ObjectMapper();
		JsonNode schemaJson = mapper2.readTree(schemaString);

		final Library library = DraftV4Library.get().thaw().addFormatAttribute("date", DateAttribute.getInstance())
				.freeze();

		final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
				.setDefaultLibrary("http://koderlabs.com/myschema#", library).freeze();

		final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder().setValidationConfiguration(cfg).freeze();

		// final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

		JsonSchema schema = factory.getJsonSchema(schemaJson);

		ProcessingReport report;
		report = schema.validate(json, true);
		return new JSONValidatorReport(report);
	}

	public static JSONValidatorReport validateRequest(String schemaName, Object requestData)
			throws JsonProcessingException, IOException, ProcessingException {
		HashMap<String, Object> data = (HashMap<String, Object>) requestData;
		String json = (new JSONObject(data).toString());
		String schema = IO.loadSchema(schemaName);
		return JSONValidatorEngine.validate(schema, json);
	}
}
