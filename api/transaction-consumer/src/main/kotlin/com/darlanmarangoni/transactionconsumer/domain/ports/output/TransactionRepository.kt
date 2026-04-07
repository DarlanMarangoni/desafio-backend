package com.darlanmarangoni.transactionconsumer.domain.ports.output

import com.darlanmarangoni.transactionconsumer.domain.model.Transaction
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TransactionRepository: ReactiveMongoRepository<Transaction, String> {
}