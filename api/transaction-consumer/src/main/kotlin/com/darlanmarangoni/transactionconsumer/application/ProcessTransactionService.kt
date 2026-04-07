package com.darlanmarangoni.transactionconsumer.application

import com.darlanmarangoni.transactionconsumer.domain.model.Account
import com.darlanmarangoni.transactionconsumer.domain.model.Balance
import com.darlanmarangoni.transactionconsumer.domain.model.Transaction
import com.darlanmarangoni.transactionconsumer.dto.EventDto
import com.darlanmarangoni.transactionconsumer.domain.ports.input.ProcessTransactionUseCase
import com.darlanmarangoni.transactionconsumer.domain.ports.output.AccountRepository
import com.darlanmarangoni.transactionconsumer.domain.ports.output.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class ProcessTransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ProcessTransactionUseCase {

    @Transactional
    override fun execute(event: EventDto): Mono<Void> {
        val transaction = Transaction(
            id = event.transaction.id,
            type = event.transaction.type,
            amount = event.transaction.amount,
            currency = event.transaction.currency,
            status = event.transaction.status,
            timestamp = event.transaction.timestamp,
            accountId = event.account.id
        )

        val balance = Balance(
            amount = event.account.balanceDto.amount,
            currency = event.account.balanceDto.currency,
        )

        val account = Account(
            id = event.account.id,
            owner = event.account.owner,
            status = event.account.status,
            balance = balance,
            updatedAt = event.transaction.timestamp
        )

        return transactionRepository.save(transaction)
            .then(accountRepository.increaseBalance(account))
            .then()
    }
}