package com.odan.security.sales.job;

import com.odan.common.utils.DateTime;
import com.odan.security.sales.SalesQueryHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
			/*List<SaleItem> salesList = q.getInvoiceableSales();
			
			for(SaleItem s: salesList) {
				ICommand c = new CreateSalesInvoice(s);
				CommandRegister.getInstance().process(c);	
			}*/
			
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