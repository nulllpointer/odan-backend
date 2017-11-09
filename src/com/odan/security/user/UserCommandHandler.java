package com.odan.security.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.security.user.command.CreateUser;
import com.odan.security.user.command.DeleteUser;
import com.odan.security.user.command.LoginUser;
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
		CommandRegister.getInstance().registerHandler(DeleteUser.class, UserCommandHandler.class);
		CommandRegister.getInstance().registerHandler(LoginUser.class, UserCommandHandler.class);



	}

	public void handle(ICommand c) throws CommandException {
		if (c instanceof CreateUser) {
			handle((CreateUser) c);
		} else if (c instanceof UpdateUser) {
			handle((UpdateUser) c);
		}
		else if (c instanceof DeleteUser) {
			handle((DeleteUser) c);
		}

		else if (c instanceof LoginUser) {
			handle((LoginUser) c);
		}


	}




	public void handle(CreateUser c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			User v = this._handleSaveUser(c);
			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(c.getTransaction());
			}
			c.setObject(v);
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

		User cust = null;
		boolean isNew = true;

		if (c.has("id") && c instanceof UpdateUser) {
			cust = (User) (new UserQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
			isNew = false;
			if (cust == null) {
				APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
				throw new CommandException("Customer (" + c.get("id") + ") not found.");
			}
		}

		if (cust == null) {
			cust = new User();
		}


		if (c.has("firstName")) {
			cust.setFirstName((String) c.get("firstName"));
		}

		if (c.has("lastName")) {
			cust.setLastName((String) c.get("lastName"));
		}

		if (c.has("email")) {
			cust.setEmail((String) c.get("email"));
		}

		if (c.has("phone")) {
			cust.setPhone((String) c.get("phone"));
		}

		if (c.has("addressLine1")) {
			cust.setAddressLine1((String) c.get("addressLine1"));
		}

		if (c.has("addressLine2")) {
			cust.setAddressLine2((String) c.get("addressLine2"));
		}

		if (c.has("city")) {
			cust.setCity((String) c.get("city"));
		}

		if (c.has("state")) {
			cust.setState((String) c.get("state"));
		}

		if (c.has("country")) {
			cust.setCountry((String) c.get("country"));
		}

		if (c.has("postalCode")) {
			cust.setPostalCode((String) c.get("postalCode"));
		}

		if (c.has("status")) {

			cust.setStatus(EntityStatus.ACTIVE);
		}


		if (c.has("userPassword")) {

			cust.setUserPassword((String) c.get("userPassword"));
		}
		if (c.has("userName")) {

			cust.setUserName((String) c.get("userName"));
		}


		if (isNew) {
			cust.setCreatedAt(DateTime.getCurrentTimestamp());

		} else {
			cust.setUpdatedAt(DateTime.getCurrentTimestamp());
		}

		cust = (User) HibernateUtils.save(cust, c.getTransaction());

		return cust;

	}

	public void handle(DeleteUser c) throws CommandException {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();


		User user = (User) (new UserQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
		if(user!=null){
			boolean success= HibernateUtils.delete(user,trx);
			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(c.getTransaction());
			}
			c.setObject(success);

		}
	}




	public void handle(LoginUser c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			if (c.isCommittable()) {
				HibernateUtils.commitTransaction(c.getTransaction());
			}
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


}
