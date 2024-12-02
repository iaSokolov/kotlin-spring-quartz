package ru.iasokolov.quartz.schedule.load

import mu.KotlinLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class LoadDataJob() : QuartzJobBean() {
    private val logger = KotlinLogging.logger { }

    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        logger.info { "Load data ..." }
        Thread.sleep(1_000 * 60 * 2)
        logger.info { "Load data complete" }
    }
}