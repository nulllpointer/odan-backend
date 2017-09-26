package com.odan.inventory.sales.job;

import com.odan.billing.invoice.command.CreateSalesInvoice;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.utils.DateTime;
import com.odan.finance.sales.SalesQueryHandler;
import com.odan.finance.sales.model.Sales;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class SalesInvoiceJob implements Job {
	public static boolean isRunning = false;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if(isRunning) {
				System.out.println("Already Running Sales Invoice Job. Aborted New Execution Request.");
				return;
			}
			isRunning = true;
			
			
			System.out.println("Sales Invoice Job Started");
			System.out.println(DateTime.getCurrentTimestamp());
			
			SalesQueryHandler q = new SalesQueryHandler();
			List<Sales> salesList = q.getInvoiceableSales();
			
			for(Sales s: salesList) {
				ICommand c = new CreateSalesInvoice(s);
				CommandRegister.getInstance().process(c);	
			}
			
			System.out.println(DateTime.getCurrentTimestamp());
			System.out.println("Sales Invoice Job Ended");
			
			
			isRunning = false;
		}
		catch (Exception ex) {
			isRunning = false;
			throw ex;
		}
		// try {
		// Mailer.send("jawaidgadiwala@hotmail.com", "CRON RAN", "Invoice Job
		// Ran Successfully");
		// } catch (AddressException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (MessagingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}