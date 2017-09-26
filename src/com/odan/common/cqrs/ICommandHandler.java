package com.odan.common.cqrs;

public interface ICommandHandler {
	void handle(ICommand c);
}
