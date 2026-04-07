package com.darlanmarangoni.transactionconsumer.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class BalanceDto(
    @JsonProperty("amount")
    val amount: Double,
    @JsonProperty("currency")
    val currency: String
)