package ru.iasokolov.quartz.schedule.calc

import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CalcDataScheduleConfig() {
    @Bean
    fun calcDataDetail(): JobDetail {
        return JobBuilder
            .newJob(CalcDataJob::class.java).withIdentity("CalcDataJob")
            .withDescription("Calc data job")
            .requestRecovery(true) // Устанавливаем данное значение в true, если хотим, чтобы джоба была перезапущена в случае падения пода
            .storeDurably() // не удаляем задание из базы даже в случае, если ни один из триггеров на задание не укаывает
            .build()
    }

    @Bean
    fun calcDataJobTrigger(
        calcDataDetail: JobDetail
    ): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(calcDataDetail)
            .withIdentity("Calc data job trigger")
            .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1))
            .build()
    }
}