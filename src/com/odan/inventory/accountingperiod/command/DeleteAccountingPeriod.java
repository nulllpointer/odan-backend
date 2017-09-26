package com.odan.inventory.accountingperiod.command;

import java.util.HashMap;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;

public class DeleteAccountingPeriod extends Command implements ICommand {
	public DeleteAccountingPeriod(HashMap<String, Object> data) {
		super(data);
	}
}