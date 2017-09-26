package com.odan.finance.accountingperiod.command;

import java.util.HashMap;

import org.hibernate.Transaction;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class CreateAccountingPeriod extends Command implements ICommand {
	public CreateAccountingPeriod(HashMap<String, Object> data) {
		super(data);
	}
	
	public CreateAccountingPeriod(HashMap<String, Object> data, Transaction trx) {
		super(data, trx);
	}
}