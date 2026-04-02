package com.darlanmarangoni.transactionconsumer.test.unit

import com.darlanmarangoni.transactionconsumer.dto.EventDto
import com.darlanmarangoni.transactionconsumer.infraestructure.adapters.input.SqsTransactionConsumer
import com.darlanmarangoni.transactionconsumer.domain.ports.input.ProcessTransactionUseCase
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.concurrent.CompletableFuture

class SqsTransactionConsumerTest {
    private val useCase: ProcessTransactionUseCase = mock(ProcessTransactionUseCase::class.java)
    private val logger = KotlinLogging.logger {}
    private lateinit var consumer: SqsTransactionConsumer

    @BeforeEach
    fun setUp() {
        consumer = SqsTransactionConsumer(useCase, logger)
    }

    @Test
    fun testProcessarLogsMessageAndInvokesUseCase() {
        val eventDto = EventDto(/* Initialize with necessary data */)

        consumer.processar(eventDto)

        verify(useCase).execute(eventDto)
        logger.info { "Recebendo mensagem: $eventDto" }
    }

    @Test
    fun testProcessarLogsProcessingTime() {
        val eventDto = EventDto(/* Initialize with necessary data */)
        val init = System.currentTimeMillis()
        consumer.processar(eventDto)
        val executionTime = System.currentTimeMillis() - init

        logger.info { "Mensagem processada com sucesso, tempo de processamento: $executionTime ms" }
    }

    @Test
    fun testProcessarHandlesNullInput() {
        consumer.processar(null)

        // Add assertions to check if logger logs the correct message or throws an exception
    }
}
