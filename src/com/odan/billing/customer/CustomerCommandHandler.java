package com.odan.billing.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.odan.billing.customer.command.CreateCustomer;
import com.odan.billing.customer.command.UpdateCustomer;
import com.odan.billing.customer.model.Customer;
import com.odan.billing.user.command.UpdateUser;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.EntityStatus;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;
import org.hibernate.Transaction;

public class CustomerCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateCustomer.class, CustomerCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateCustomer.class, CustomerCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateCustomer) {
			handle((CreateCustomer) c);
		} else if (c instanceof UpdateCustomer) {
			handle((UpdateCustomer) c);
		}
	}

	public void handle(CreateCustomer c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Customer v = this._handleSaveCustomer(c);
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

	public void handle(UpdateCustomer c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Customer ba = this._handleSaveCustomer(c);
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

	private Customer _handleSaveCustomer(ICommand c) throws CommandException, JsonProcessingException {

		Customer cust = null;
		boolean isNew = true;

		if (c.has("id") && c instanceof UpdateUser) {
			cust = (Customer) (new CustomerQueryHandler()).getById(Parser.convertObjectToLong(c.get("id")));
			isNew = false;
			if (cust == null) {
				APILogger.add(APILogType.ERROR, "Customer (" + c.get("id") + ") not found.");
				throw new CommandException("Customer (" + c.get("id") + ") not found.");
			}
		}

		if (cust == null) {
			cust = new Customer();
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
			cust.setStatus(Flags.convertInputToStatus(c.get("status")));
		} else  {
			cust.setStatus(EntityStatus.ACTIVE.getFlag());
		}

		/*if (isNew) {
			cust.setCreatedAt(DateTime.getCurrentTimestamp());
			Long ownerId = Parser.convertObjectToLong(c.get("ownerId"));
			if(ownerId == null) {
				ownerId = Authentication.getUserId();
			}
			cust.setOwnerId(ownerId);
		} else {
			cust.setUpdatedAt(DateTime.getCurrentTimestamp());
		}
*/
		cust = (Customer) HibernateUtils.save(cust, c.getTransaction());
		
		String entityTitle = cust.getFirstName() + " " + cust.getLastName();

		ICommand customerEntityCommand = null;

		// Create customer entity code.
		// Entity code is used in creating accounting code.



		// If new customer created then create user's gross commission entity code.
		// Entity code is used in creating accounting code.
//		if (isNew) {
//			ICommand gcEntityCommand = new CreateEntityCode(u.getId(), 0L, EntityType.COMMISSION, "Gross Commission",
//					c.getTransaction());
//			CommandRegister.getInstance().process(gcEntityCommand);
//			EntityCode gcEntity = (EntityCode) gcEntityCommand.getObject();
//			if (gcEntity == null) {
//				APILogger.add(APILogType.ERROR, "Error creating user gross commission code.");
//				throw new CommandException("Error creating user gross commission code.");
//			}
//		}
		return cust;

	}
}
