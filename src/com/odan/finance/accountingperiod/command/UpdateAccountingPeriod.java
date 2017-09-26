package com.odan.finance.accountingperiod.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class UpdateAccountingPeriod extends Command implements ICommand {
	public UpdateAccountingPeriod(HashMap<String, Object> data) {
		super(data);
	}
}