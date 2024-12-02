package ru.iasokolov.quartz.schedule.load

import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadDataScheduleConfig() {
    @Bean
    fun loadDataDetail(): JobDetail {
        return JobBuilder
            .newJob(LoadDataJob::class.java).withIdentity("LoadDataJob")
            .withDescription("Load data job")
            .requestRecovery(true) // Устанавливаем данное значение в true, если хотим, чтобы джоба была перезапущена в случае падения пода
            .storeDurably() // не удаляем задание из базы даже в случае, если ни один из триггеров на задание не укаывает
            .build()
    }

    @Bean
    fun loadDataJobTrigger(
        loadDataDetail: JobDetail
    ): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(loadDataDetail)
            .withIdentity("Load data job trigger")
            .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1))
            .build()
    }
}