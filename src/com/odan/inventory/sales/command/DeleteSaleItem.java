package com.odan.inventory.sales.command;

import com.odan.common.cqrs.Command;
import com.odan.common.cqrs.ICommand;
import org.hibernate.Transaction;

import java.util.HashMap;

public class DeleteSaleItem extends Command implements ICommand {
	public DeleteSaleItem(HashMap<String, Object> data) {
		super(data);
	}
	public DeleteSaleItem(HashMap<String, Object> data,Transaction trx) {
		super(data,trx);
	}
}