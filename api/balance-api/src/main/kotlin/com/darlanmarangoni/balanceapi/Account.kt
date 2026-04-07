package com.darlanmarangoni.balanceapi

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "accounts")
data class Account(
    @Id
    val id: String,
    val owner: String,
    val status: String,
    val balance: Balance,
    val updatedAt: Long
)
