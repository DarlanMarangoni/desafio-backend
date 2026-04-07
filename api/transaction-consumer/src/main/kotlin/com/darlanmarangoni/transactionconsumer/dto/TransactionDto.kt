package com.darlanmarangoni.transactionconsumer.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class TransactionDto (
    @JsonProperty("id")
    val id: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("amount")
    val amount: Double,
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("timestamp")
    val timestamp: Long
)