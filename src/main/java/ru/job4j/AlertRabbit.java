package ru.job4j;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            Properties property = rabbitProperties();
            data.put("store", initConnection(property));
            JobDetail job = newJob(Rabbit.class).usingJobData(data).build();

            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(property
                            .getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException se) {
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

    private static Connection initConnection(Properties property) {
        Connection connection = null;
        try {
            Class.forName(property.getProperty("driver_class"));
            connection = DriverManager.getConnection(
                    property.getProperty("url"),
                    property.getProperty("username"),
                    property.getProperty("password"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            Connection connection = (Connection) context
                    .getJobDetail().getJobDataMap().get("Store");
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into rabbit (created_date) values (current_timestamp)"
            )) {
                preparedStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Rabbit runs here ...");
        }
    }
}