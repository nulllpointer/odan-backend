package com.odan.inventory.accountingperiod.job;

import java.util.List;

import com.odan.finance.accountingperiod.model.AccountingPeriod;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.ICommand;
import com.odan.common.utils.DateTime;
import com.odan.finance.accountingperiod.AccountingPeriodQueryHandler;
import com.odan.finance.accountingperiod.command.CloseAccountingPeriod;

public class AccountingPeriodJob implements Job {
	public static boolean isRunning = false;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if(isRunning) {
				System.out.println("Already Running Accounting Period Job. Aborted New Execution Request.");
				return;
			}
			isRunning = true;
			
			
			System.out.println("Accounting Period Job Started");
			System.out.println(DateTime.getCurrentTimestamp());
			
			AccountingPeriodQueryHandler q = new AccountingPeriodQueryHandler();
			List<AccountingPeriod> acpList = q.getCloseable();
			
			for(AccountingPeriod acp: acpList) {
				ICommand c = new CloseAccountingPeriod(acp);
				CommandRegister.getInstance().process(c);	
			}
			
			System.out.println(DateTime.getCurrentTimestamp());
			System.out.println("Accounting Period Job Ended");
			
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