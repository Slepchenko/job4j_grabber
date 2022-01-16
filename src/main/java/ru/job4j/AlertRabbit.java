package ru.job4j;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(deleyTime())
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException | FileNotFoundException se) {
            se.printStackTrace();
        }
    }

    private static SimpleScheduleBuilder deleyTime() throws FileNotFoundException {
        Properties pr = new Properties();
        try (FileInputStream in = new FileInputStream(
                "./src/main/java/resources/rabbit.properties")) {
            pr.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int res = Integer.parseInt(pr.getProperty("rabbit.interval"));
        return simpleSchedule()
                .withIntervalInSeconds(res)
                .repeatForever();
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}