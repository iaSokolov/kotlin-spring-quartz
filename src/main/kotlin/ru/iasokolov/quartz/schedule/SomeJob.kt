package ru.iasokolov.quartz.schedule

import mu.KotlinLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class SomeJob() : QuartzJobBean() {
    private val logger = KotlinLogging.logger {  }

    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        try {
            while(true) {
                Thread.sleep(5_000)
                logger.info("Doing awesome work...")
            }
        } catch (e: Exception) {
            throw JobExecutionException(e)
        }
    }
}