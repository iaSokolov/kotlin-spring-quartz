package ru.iasokolov.quartz.schedule

import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.JobDetail
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.CronScheduleBuilder

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.scheduling.quartz.SchedulerFactoryBean

@Configuration
class JobConfig {
    @Bean
    fun someJobDetail(): JobDetail {
        return JobBuilder
            .newJob(SomeJob::class.java).withIdentity("SomeJob")
            .withDescription("Some job")
            // Устанавливаем данное значение в true, если хотим, чтобы джоба была перезапущена
            // в случае падения пода
            .requestRecovery(true)
            // не удаляем задание из базы даже в случае, если ни один из триггеров на задание не укаывает
            .storeDurably().build()
    }

    //Trigger
    @Bean
    fun someJobTrigger(
        someJobDetail: JobDetail
    ): Trigger {
        return TriggerBuilder.newTrigger().forJob(someJobDetail)
            .withIdentity("SomeJobTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
            .build()

    }

    // Необходимо также при старте пересоздавать уже имеющиеся задания
    // (нужно на случай, если вы заходите изменить cron выражение для какого-либо из ваших заданий,
    // которые уже были созданы ранее, в противном случае в базе сохранится старое cron выражение)
    @Bean
    fun scheduler(
        triggers: List<Trigger>,
        jobDetails: List<JobDetail>,
        factory: SchedulerFactoryBean
    ): Scheduler {
        factory.setWaitForJobsToCompleteOnShutdown(true)
        val scheduler = factory.scheduler
        factory.setOverwriteExistingJobs(true)
        //https://stackoverflow.com/questions/39673572/spring-quartz-scheduler-race-condition
        factory.setTransactionManager(JdbcTransactionManager())
        rescheduleTriggers(triggers, scheduler)
        scheduler.start()
        return scheduler
    }

    private fun rescheduleTriggers(
        triggers: List<Trigger>,
        scheduler: Scheduler
    ) {
        triggers.forEach {
            if (!scheduler.checkExists(it.key)) {
                scheduler.scheduleJob(it)
            } else {
                scheduler.rescheduleJob(it.key, it)
            }
        }
    }
}