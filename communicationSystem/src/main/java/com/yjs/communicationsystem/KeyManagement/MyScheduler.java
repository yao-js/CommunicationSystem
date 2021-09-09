package com.yjs.communicationsystem.KeyManagement;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class MyScheduler {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        // 1、创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(BatchKeyUpdateJob.class)
                .withIdentity("job1", "group1").build();

        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 5000);

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 5000);

        // 3、构建Trigger实例,每隔1s执行一次
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .usingJobData("trigger1", "这是jobDetail1的trigger")
                .startNow()//立即生效
                //表示触发器首次被触发的时间;
//                .startAt(startDate)
                //表示触发器结束触发的时间
//                .endAt(endDate)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 11 11 5 3 ? *"))
                .build();

        //4、执行
        scheduler.scheduleJob(jobDetail, cronTrigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
//        System.out.println("--------scheduler shutdown ! ------------");



    }

}
