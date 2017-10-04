package com.odan.security.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.security.user.command.CreateUser;
import com.odan.security.user.command.DeleteUser;
import com.odan.security.user.command.UpdateUser;
import com.odan.security.user.model.User;
import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import org.hibernate.Transaction;

public class UserCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateUser.class, UserCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateUser.class, UserCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateUser) {
			handle((CreateUser) c);
		} else if (c instanceof UpdateUser) {
			handle((UpdateUser) c);
		}
		else if (c instanceof DeleteUser) {
			handle((DeleteUser) c);
		}
	}

	public void handle(CreateUser c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			User u = this._handleSaveUser(c);
			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(c.getTransaction());
			}
			c.setObject(u);
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	public void handle(UpdateUser c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			User ba = this._handleSaveUser(c);
			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(c.getTransaction());
			}
			c.setObject(ba);
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	private User _handleSaveUser(ICommand c) throws CommandException, JsonProcessingException {
		User user = null;
		boolean isNew = true;

		if (c.has("id") && c instanceof UpdateUser) {
			user = (User) (new UserQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
			isNew = false;
			if (user == null) {
				APILogger.add(APILogType.ERROR, "User (" + c.get("id") + ") not found.");
				throw new CommandException("User (" + c.get("id") + ") not found.");
			}
		}

		if (user == null) {
			user = new User();
		}

		if (c.has("firstName")) {
			user.setFirstName((String) c.get("firstName"));
		}

		if (c.has("lastName")) {
			user.setLastName((String) c.get("lastName"));
		}

		if (c.has("email")) {
			user.setEmail((String) c.get("email"));
		}

		if (c.has("phone")) {
			user.setPhone((String) c.get("phone"));
		}

		if (c.has("addressLine1")) {
			user.setAddressLine1((String) c.get("addressLine1"));
		}

		if (c.has("addressLine2")) {
			user.setAddressLine2((String) c.get("addressLine2"));
		}

		if (c.has("city")) {
			user.setCity((String) c.get("city"));
		}

		if (c.has("state")) {
			user.setState((String) c.get("state"));
		}

		if (c.has("country")) {
			user.setCountry((String) c.get("country"));
		}

		if (c.has("postalCode")) {
			user.setPostalCode((String) c.get("postalCode"));
		}


		if (c.has("status")) {
			user.setStatus(EntityStatus.ACTIVE);
		} else if (isNew) {
			user.setStatus(EntityStatus.ACTIVE);
		}

		if (isNew) {
			user.setCreatedAt(DateTime.getCurrentTimestamp());
			Long ownerId = Parser.convertObjectToLong(c.get("ownerId"));
			if (ownerId == null) {
				ownerId = Authentication.getUserId();
			}
		} else {
			user.setUpdatedAt(DateTime.getCurrentTimestamp());
		}

		user = (User) HibernateUtils.save(user, c.getTransaction());



		return user;

	}
}
