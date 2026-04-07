package com.darlanmarangoni.transactionconsumer.domain.ports.output

import com.darlanmarangoni.transactionconsumer.domain.model.Account
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class AccountRepository(val mongoTemplate: ReactiveMongoTemplate, private val meterRegistry: MeterRegistry) {

    fun increaseBalance(account: Account): Mono<Account> {

        val upsertCounter = meterRegistry.counter("balance.upsert.count", "account", account.id)

        val criteriaDefinition = Criteria
            .where("_id").`is`(account.id)
            .orOperator(
                Criteria.where("updatedAt").exists(false),
                Criteria.where("updatedAt").lt(account.updatedAt))
        val query = Query(criteriaDefinition)

        val update = Update()
            .setOnInsert("createdAt", account.updatedAt)
            .set("status", account.status)
            .set("balance.amount", account.balance.amount)
            .set("balance.currency", account.balance.currency)
            .set("owner", account.owner)
            .set("updatedAt", account.updatedAt)

        val options = FindAndModifyOptions.options()
            .returnNew(true)
            .upsert(true)

        return mongoTemplate.findAndModify(query, update, options, Account::class.java)
            .doOnSuccess { upsertCounter.increment() }
    }

}