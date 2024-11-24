package ru.iasokolov.quartz.schedule

import mu.KotlinLogging
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class SomeJob() : QuartzJobBean() {
    private val logger = KotlinLogging.logger {  }

    override fun executeInternal(jobExecutionContext: JobExecutionContext) {
        try {
            logger.info("Doing awesome work...")

            if ((1..10).random() > 5) throw RuntimeException("Something went wrong...")
        } catch (e: Exception) {
            throw JobExecutionException(e)
        }
    }
}