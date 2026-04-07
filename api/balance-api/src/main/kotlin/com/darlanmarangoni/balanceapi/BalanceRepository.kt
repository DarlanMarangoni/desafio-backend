package com.darlanmarangoni.balanceapi

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface BalanceRepository: ReactiveMongoRepository<Account, String> {

    fun findAccountById(accountId: String): Mono<Account>
}