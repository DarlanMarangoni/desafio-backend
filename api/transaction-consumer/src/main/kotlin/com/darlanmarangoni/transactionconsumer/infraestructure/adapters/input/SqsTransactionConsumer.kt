package com.darlanmarangoni.transactionconsumer.infraestructure.adapters.input

import com.darlanmarangoni.transactionconsumer.dto.EventDto
import com.darlanmarangoni.transactionconsumer.domain.ports.input.ProcessTransactionUseCase
import io.awspring.cloud.sqs.annotation.SqsListener
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

private val logger = KotlinLogging.logger {} // Define o logger fora da classe ou no companion

@Component
class SqsTransactionConsumer(
    private val useCase: ProcessTransactionUseCase
) {
    @SqsListener("transacoes-financeiras-processadas")
    fun processar(message: EventDto): CompletableFuture<Void?> {
        val init = System.currentTimeMillis()
        logger.info { "Recebendo mensagem: $message" }
        return useCase.execute(message)
            .doOnSuccess {
                logger.info { "Mensagem processada com sucesso, tempo de processamento: ${System.currentTimeMillis() - init} ms" }
            }
            .doOnError { error ->
                logger.error(error) { "Erro ao processar mensagem: ${error.message}" }
            }
            .toFuture()
    }
}