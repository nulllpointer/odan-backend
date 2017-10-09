package com.odan.common.cqrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.common.application.CommandException;
import com.odan.common.application.ValidationException;

import java.text.ParseException;

public interface ICommandHandler {
	void handle(ICommand c) throws JsonProcessingException, CommandException, ParseException, ValidationException;
}
