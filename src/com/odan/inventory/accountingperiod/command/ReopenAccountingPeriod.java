package com.odan.inventory.accountingperiod.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class ReopenAccountingPeriod extends Command implements ICommand {
	public ReopenAccountingPeriod(HashMap<String, Object> data) {
		super(data);
		this.validate();
	}

	public ReopenAccountingPeriod(HashMap<String, Object> data, Transaction trx) {
		this(data);
		this.trx = trx;
		this.validate();
	}

	public ReopenAccountingPeriod(Object acp, Transaction trx) {
		super(null, trx);
		this.set("acp", acp);
		this.validate();
	}

	public ReopenAccountingPeriod(Object acp) {
		super();
		this.set("acp", acp);
		this.validate();
	}
}