package com.odan.billing.invoice.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.odan.billing.invoice.InvoiceQueryHandler;
import com.odan.billing.invoice.command.UpdateInvoiceDueStatus;
import com.odan.billing.invoice.model.Invoice;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.utils.DateTime;

public class InvoiceStatusJob implements Job {
	public static boolean isRunning = false;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if(isRunning) {
				System.out.println("Already Running Invoice Status Job. Aborted New Execution Request.");
				return;
			}
			isRunning = true;
			
			System.out.println("Invoice Status Job Started");
			System.out.println(DateTime.getCurrentTimestamp());
			
			InvoiceQueryHandler q = new InvoiceQueryHandler();
			List<Invoice> invoiceList = q.getDueableInvoices();
			
			for(Invoice i: invoiceList) {
				ICommand c = new UpdateInvoiceDueStatus(i);
				CommandRegister.getInstance().process(c);	
			}

			System.out.println(DateTime.getCurrentTimestamp());
			System.out.println("Invoice Status Job Ended");
			
			
			isRunning = false;
		}
		catch (Exception ex) {
			isRunning = false;
			throw ex;
		}
	}
}