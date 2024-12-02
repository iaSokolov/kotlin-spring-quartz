package ru.iasokolov.quartz.config

import org.quartz.Scheduler
import org.quartz.Trigger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.scheduling.quartz.SchedulerFactoryBean

@Configuration
class ScheduleConfig(
    private val factory: SchedulerFactoryBean
) {
    companion object {
        const val START_DELAYED_SECONDS = 60
    }

    // Необходимо также при старте пересоздавать уже имеющиеся задания
    // (нужно на случай, если вы заходите изменить cron выражение для какого-либо из ваших заданий,
    // которые уже были созданы ранее, в противном случае в базе сохранится старое cron выражение)
    @Bean
    fun scheduler(
        triggers: List<Trigger>,
    ): Scheduler {
        factory.setWaitForJobsToCompleteOnShutdown(true)
        val scheduler = factory.scheduler

        scheduler.startDelayed(START_DELAYED_SECONDS)
        factory.setOverwriteExistingJobs(true)
        //https://stackoverflow.com/questions/39673572/spring-quartz-scheduler-race-condition
        factory.setTransactionManager(JdbcTransactionManager())
        rescheduleTriggers(triggers, scheduler)
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