package com.odan.common.cqrs;

public interface IEventHandler {
	void handle(IEvent c);
}
