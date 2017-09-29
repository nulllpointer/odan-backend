package com.odan.common.scheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.odan.inventory.sales.job.SalesInvoiceJob;

public class JobScheduler implements ServletContextListener {
    Scheduler scheduler = null;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                System.out.println("Scheduler Shutdown Error");
                e.printStackTrace();
            }
        }
        System.out.println("Scheduler Ended.");
    }

    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Scheduler Started.");
        try {

            Trigger trigger02sec = TriggerBuilder.newTrigger().withIdentity("trigger02sec", "cron02")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build();

            Trigger trigger60sec = TriggerBuilder.newTrigger().withIdentity("trigger60sec", "cron60")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/60 * * * * ?")).build();

            Trigger trigger5min = TriggerBuilder.newTrigger().withIdentity("trigger5min", "cron5")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/60 0/5 * * * ?")).build();

            Trigger trigger24h = TriggerBuilder.newTrigger().withIdentity("trigger24h", "cron24")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/60 1 0/24 * * ?")).build();


            JobDetail eventJob = JobBuilder.newJob(EventJob.class)
                    .withIdentity("EventJob", "cron02").build();


            JobDetail heroJob = JobBuilder.newJob(EventJob.class)
                    .withIdentity("HeroJob", "cron60").build();


            JobDetail salesInvoiceJob = JobBuilder.newJob(SalesInvoiceJob.class)
                    .withIdentity("SalesInvoiceJob", "cron5").build();


           /* scheduler = new StdSchedulerFactory().getScheduler();

            scheduler.start();
            scheduler.scheduleJob(eventJob, trigger02sec);
            scheduler.scheduleJob(heroJob, trigger60sec);
            //  scheduler.scheduleJob(invoiceJournalJob, trigger60sec);
            // scheduler.scheduleJob(invoiceRecurJob, trigger5min);
            scheduler.scheduleJob(salesInvoiceJob, trigger5min);
//            scheduler.scheduleJob(accountingPeriodJob, trigger24h);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}