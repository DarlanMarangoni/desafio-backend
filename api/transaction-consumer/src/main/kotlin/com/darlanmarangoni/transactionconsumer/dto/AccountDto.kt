package com.darlanmarangoni.transactionconsumer.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AccountDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("owner")
    val owner: String,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("balance")
    val balanceDto: BalanceDto
)
