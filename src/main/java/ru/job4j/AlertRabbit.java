package ru.job4j;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private static Connection connection;

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", connection);
            JobDetail job = newJob(Rabbit.class).usingJobData(data).build();
            initConnection();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(rabbitProperties()
                            .getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException | ClassNotFoundException se) {
            se.printStackTrace();
        }
    }

    private static Properties rabbitProperties() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(
                "./src/main/java/resources/rabbit.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
        }
    }

    private static void initConnection() throws ClassNotFoundException {
        Class.forName(rabbitProperties().getProperty("driver_class"));
        try {
            connection = DriverManager.getConnection(
                    rabbitProperties().getProperty("url"),
                    rabbitProperties().getProperty("username"),
                    rabbitProperties().getProperty("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}