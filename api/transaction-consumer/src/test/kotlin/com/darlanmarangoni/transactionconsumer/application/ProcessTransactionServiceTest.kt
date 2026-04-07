package com.darlanmarangoni.transactionconsumer.application

import com.darlanmarangoni.transactionconsumer.domain.model.Account
import com.darlanmarangoni.transactionconsumer.domain.model.Balance
import com.darlanmarangoni.transactionconsumer.domain.model.Transaction
import com.darlanmarangoni.transactionconsumer.domain.ports.output.AccountRepository
import com.darlanmarangoni.transactionconsumer.domain.ports.output.TransactionRepository
import com.darlanmarangoni.transactionconsumer.dto.AccountDto
import com.darlanmarangoni.transactionconsumer.dto.BalanceDto
import com.darlanmarangoni.transactionconsumer.dto.EventDto
import com.darlanmarangoni.transactionconsumer.dto.TransactionDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class ProcessTransactionServiceTest {

    @Test
    fun `should process transaction and update account balance`() {
        val transactionRepository = mockk<TransactionRepository>(relaxed = true)
        val accountRepository = mockk<AccountRepository>(relaxed = true)

        every { transactionRepository.save(any()) } returns Mono.just(
            Transaction(
                id = "tx-123",
                type = "DEPOSIT",
                amount = 100.0,
                currency = "BRL",
                status = "COMPLETED",
                timestamp = 1712000000000L,
                accountId = "account-123"
            )
        )

        every { accountRepository.increaseBalance(any()) } returns Mono.just(
            Account(
                id = "account-123",
                owner = "João Silva",
                status = "ACTIVE",
                balance = Balance(amount = 100.0, currency = "BRL"),
                updatedAt = 1712000000000L
            )
        )

        val service = ProcessTransactionService(transactionRepository, accountRepository)

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

        service.execute(event)

        verify { transactionRepository.save(any()) }
        verify { accountRepository.increaseBalance(any()) }
    }

    @Test
    fun `should process transaction with different currency`() {
        val transactionRepository = mockk<TransactionRepository>(relaxed = true)
        val accountRepository = mockk<AccountRepository>(relaxed = true)

        every { transactionRepository.save(any()) } returns Mono.just(
            Transaction(
                id = "tx-456",
                type = "TRANSFER",
                amount = 500.0,
                currency = "USD",
                status = "COMPLETED",
                timestamp = 1712100000000L,
                accountId = "account-456"
            )
        )

        every { accountRepository.increaseBalance(any()) } returns Mono.just(
            Account(
                id = "account-456",
                owner = "John Doe",
                status = "ACTIVE",
                balance = Balance(amount = 500.0, currency = "USD"),
                updatedAt = 1712100000000L
            )
        )

        val service = ProcessTransactionService(transactionRepository, accountRepository)

        val event = EventDto(
            transaction = TransactionDto(
                id = "tx-456",
                type = "TRANSFER",
                amount = 500.0,
                currency = "USD",
                status = "COMPLETED",
                timestamp = 1712100000000L
            ),
            account = AccountDto(
                id = "account-456",
                owner = "John Doe",
                createdAt = "2024-01-01",
                status = "ACTIVE",
                balanceDto = BalanceDto(amount = 500.0, currency = "USD")
            )
        )

        service.execute(event)

        verify { transactionRepository.save(match { it.currency == "USD" }) }
    }
}