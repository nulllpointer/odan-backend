package com.odan.security.accountingperiod;

import java.sql.Timestamp;

import com.odan.security.accountingperiod.command.ReopenAccountingPeriod;
import com.odan.security.accountingperiod.command.UpdateAccountingPeriod;
import com.odan.security.accountingperiod.model.AccountingPeriod;
import org.hibernate.Transaction;

import com.odan.common.application.Authentication;
import com.odan.common.application.CommandException;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.cqrs.ICommandHandler;
import com.odan.common.cqrs.Query;
import com.odan.common.database.HibernateUtils;
import com.odan.common.model.Flags;
import com.odan.common.utils.APILogType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.DateTime;
import com.odan.common.utils.Parser;
import com.odan.security.accountingperiod.command.CloseAccountingPeriod;
import com.odan.security.accountingperiod.command.CreateAccountingPeriod;
import com.odan.security.accountingperiod.command.DeleteAccountingPeriod;

public class AccountingPeriodCommandHandler implements ICommandHandler {

	public static void registerCommands() {
		CommandRegister.getInstance().registerHandler(CreateAccountingPeriod.class,
				AccountingPeriodCommandHandler.class);
		CommandRegister.getInstance().registerHandler(UpdateAccountingPeriod.class,
				AccountingPeriodCommandHandler.class);
		CommandRegister.getInstance().registerHandler(DeleteAccountingPeriod.class,
				AccountingPeriodCommandHandler.class);
		CommandRegister.getInstance().registerHandler(CloseAccountingPeriod.class,
				AccountingPeriodCommandHandler.class);
		CommandRegister.getInstance().registerHandler(ReopenAccountingPeriod.class,
				AccountingPeriodCommandHandler.class);
	}

	public void handle(ICommand c) {
		if (c instanceof CreateAccountingPeriod) {
			handle((CreateAccountingPeriod) c);
		} else if (c instanceof UpdateAccountingPeriod) {
			handle((UpdateAccountingPeriod) c);
		} else if (c instanceof DeleteAccountingPeriod) {
			handle((DeleteAccountingPeriod) c);
		} else if (c instanceof CloseAccountingPeriod) {
			handle((CloseAccountingPeriod) c);
		} else if (c instanceof ReopenAccountingPeriod) {
			handle((ReopenAccountingPeriod) c);
		}
	}

	public void handle(CreateAccountingPeriod c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Timestamp startDate = null;
			Timestamp endDate = null;
			Long ownerId = Authentication.getUserId();

			if (c.has("ownerId")) {
				ownerId = Parser.convertObjectToLong(c.get("ownerId"));
			}

			if (c.has("timestamp")) {
				String tString = (String) c.get("timestamp");
				Timestamp ts = Parser.convertObjectToTimestamp(tString);
				startDate = DateTime.getMonthStartDate(ts);
				endDate = DateTime.getMonthEndDate(ts);
			} else if (c.has("startDate") && c.has("endDate")) {
				startDate = Parser.convertObjectToTimestamp(c.get("startDate"));
				endDate = Parser.convertObjectToTimestamp(c.get("endDate"));
			}

			if ((new AccountingPeriodQueryHandler()).getByTimestamp(startDate, ownerId) != null
					|| (new AccountingPeriodQueryHandler()).getByTimestamp(endDate, ownerId) != null) {

				APILogger.add(APILogType.ERROR, "Accounting period already exists.");
				throw new CommandException();
			}

			AccountingPeriod ap = new AccountingPeriod();
			ap.setStartDate(startDate);
			ap.setEndDate(endDate);
			ap.setOwnerId(ownerId);
			ap.setCreatedAt(DateTime.getCurrentTimestamp());
			HibernateUtils.save(ap, trx);

			c.setObject(ap);
		} catch (Exception ex) {
			if (c.isCommittable()) {
				HibernateUtils.rollbackTransaction(trx);
			}
			ex.printStackTrace();
		} finally {
			if (c.isCommittable()) {
				HibernateUtils.closeSession();
			}
		}
	}

	public void handle(UpdateAccountingPeriod c) {
		System.out.println("Command not defined");
	}

	public void handle(DeleteAccountingPeriod c) {
		System.out.println("Command not defined");
	}

	public void handle(CloseAccountingPeriod c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Query q = new Query();
			q.set("id", c.get("id"));
			AccountingPeriod ap = (AccountingPeriod) (new AccountingPeriodQueryHandler()).get(q);
			HibernateUtils.save(ap, trx);

			c.setObject(ap);
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

	public void handle(ReopenAccountingPeriod c) {
		// HibernateUtils.openSession();
		Transaction trx = c.getTransaction();

		try {
			Query q = new Query();
			q.set("id", c.get("id"));
			AccountingPeriod ap = (AccountingPeriod) (new AccountingPeriodQueryHandler()).get(q);
			HibernateUtils.save(ap, trx);

			c.setObject(ap);
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
