package com.darlanmarangoni.transactionconsumer.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "transactions")
data class Transaction (
    @Id
    val id: String,
    val type: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val timestamp: Long,
    var accountId: String
)