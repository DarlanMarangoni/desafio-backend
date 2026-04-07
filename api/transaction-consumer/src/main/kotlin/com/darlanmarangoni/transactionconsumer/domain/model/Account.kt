package com.darlanmarangoni.transactionconsumer.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "accounts")
data class Account(
    @Id
    val id: String,
    val owner: String,
    val status: String,
    val balance: Balance,
    val updatedAt: Long
)
