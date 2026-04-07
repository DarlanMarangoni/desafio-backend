package com.darlanmarangoni.transactionconsumer.infraestructure.adapters.input

import com.darlanmarangoni.transactionconsumer.domain.ports.input.ProcessTransactionUseCase
import com.darlanmarangoni.transactionconsumer.dto.AccountDto
import com.darlanmarangoni.transactionconsumer.dto.BalanceDto
import com.darlanmarangoni.transactionconsumer.dto.EventDto
import com.darlanmarangoni.transactionconsumer.dto.TransactionDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class SqsTransactionConsumerTest {

    @Test
    fun `should process message from SQS`() {
        val useCase = mockk<ProcessTransactionUseCase>(relaxed = true)
        val consumer = SqsTransactionConsumer(useCase)

        val event = EventDto(
            transaction = TransactionDto(
                id = "tx-123",
                type = "DEPOSIT",
                amount = 100.0,
                currency = "BRL",
                status = "COMPLETED",
                timestamp = 1712000000000L
            ),
            account = AccountDto(
                id = "account-123",
                owner = "João Silva",
                createdAt = "2024-01-01",
                status = "ACTIVE",
                balanceDto = BalanceDto(amount = 100.0, currency = "BRL")
            )
        )

        consumer.processar(event)

        verify { useCase.execute(event) }
    }

    @Test
    fun `should process message with different transaction types`() {
        val useCase = mockk<ProcessTransactionUseCase>(relaxed = true)
        val consumer = SqsTransactionConsumer(useCase)

        val event = EventDto(
            transaction = TransactionDto(
                id = "tx-789",
                type = "WITHDRAWAL",
                amount = 50.0,
                currency = "BRL",
                status = "PENDING",
                timestamp = 1712200000000L
            ),
            account = AccountDto(
                id = "account-789",
                owner = "Maria Santos",
                createdAt = "2024-01-01",
                status = "ACTIVE",
                balanceDto = BalanceDto(amount = 500.0, currency = "BRL")
            )
        )

        consumer.processar(event)

        verify { useCase.execute(match {
            it.transaction.type == "WITHDRAWAL" &&
            it.transaction.status == "PENDING"
        }) }
    }
}