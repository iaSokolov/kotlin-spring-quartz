package ru.iasokolov.quartz.schedule.calc

import mu.KotlinLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class CalcDataJob() : QuartzJobBean() {
    private val logger = KotlinLogging.logger { }

    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        logger.info { "Calc data ..." }
        Thread.sleep(1_000 * 60)
        logger.info { "Calc data complete" }
    }
}