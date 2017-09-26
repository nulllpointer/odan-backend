package com.odan.common.cqrs;

import java.util.HashMap;

public class CommandRegister {
	private HashMap<Class<?>, Class<?>> registry;
	private static CommandRegister instance = null;

	public CommandRegister() {
		registry = new HashMap<Class<?>, Class<?>>();
	}

	public static CommandRegister getInstance() {
		if (CommandRegister.instance == null) {
			CommandRegister.instance = new CommandRegister();
		}
		return CommandRegister.instance;
	}

	public void registerHandler(Class<?> command, Class<?> handler) {
		this.registry.put(command, handler);
	}

	public void process(ICommand command) {
		Class commandType = command.getClass();
		Class commandHandlerType = null;
		ICommandHandler commandHandler = null;

	/*	if (!command.isValid()) {
			if (command.isCommittable()) {
				HibernateUtils.rollbackTransaction(command.getTransaction());
				HibernateUtils.closeSession();
			}
			return;
		}*/

		if (registry.containsKey(commandType)) {
			commandHandlerType = registry.get(commandType);
		}

		if (commandHandlerType != null) {
			try {
				commandHandler = (ICommandHandler) commandHandlerType.getConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("..CommandHandler Registry Not Defined for " + commandType.getName());
		}

		if (commandHandler != null) {
			commandHandler.handle(command);
		} else {
			System.out.println("..CommandHandler Not Defined");
		}

	}

}
