package com.odan.common.cqrs;

import com.odan.common.application.CommandException;

import java.util.List;

public interface IQueryHandler {
	Object getById(Long id) throws CommandException;
	List<Object> get(Query q);

}
