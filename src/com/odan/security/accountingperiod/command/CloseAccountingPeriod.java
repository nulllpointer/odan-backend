package com.odan.security.accountingperiod.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CloseAccountingPeriod extends Command implements ICommand {
	public CloseAccountingPeriod(HashMap<String, Object> data) {
		super(data);
		this.validate();
	}

	public CloseAccountingPeriod(HashMap<String, Object> data, Transaction trx) {
		this(data);
		this.trx = trx;
		this.validate();
	}

	public CloseAccountingPeriod(Object acp, Transaction trx) {
		super(null, trx);
		this.set("acp", acp);
		this.validate();
	}

	public CloseAccountingPeriod(Object acp) {
		super();
		this.set("acp", acp);
		this.validate();
	}
}