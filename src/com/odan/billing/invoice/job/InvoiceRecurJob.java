package com.odan.billing.invoice.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.odan.billing.invoice.InvoiceQueryHandler;
import com.odan.billing.invoice.command.CreateRecurringInvoice;
import com.odan.billing.invoice.model.Invoice;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.utils.DateTime;

public class InvoiceRecurJob implements Job {
	public static boolean isRunning = false;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if(isRunning) {
				System.out.println("Already Running Invoice Recur Job. Aborted New Execution Request.");
				return;
			}
			isRunning = true;
			
			
			System.out.println("Invoice Recur Job Started");
			System.out.println(DateTime.getCurrentTimestamp());
			
			InvoiceQueryHandler q = new InvoiceQueryHandler();
			List<Invoice> invoiceList = q.getRecurrableInvoices();
			
			for(Invoice i: invoiceList) {
				ICommand c = new CreateRecurringInvoice(i);
				CommandRegister.getInstance().process(c);	
			}

			System.out.println(DateTime.getCurrentTimestamp());
			System.out.println("Invoice Recur Job Ended");
			
			isRunning = false;
		}
		catch (Exception ex) {
			isRunning = false;
			throw ex;
		}
	}
}