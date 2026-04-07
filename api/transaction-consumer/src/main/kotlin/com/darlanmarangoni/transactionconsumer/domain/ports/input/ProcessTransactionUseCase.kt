package com.darlanmarangoni.transactionconsumer.domain.ports.input

import com.darlanmarangoni.transactionconsumer.dto.EventDto

import reactor.core.publisher.Mono

interface ProcessTransactionUseCase {
    fun execute(transaction: EventDto): Mono<Void>
}