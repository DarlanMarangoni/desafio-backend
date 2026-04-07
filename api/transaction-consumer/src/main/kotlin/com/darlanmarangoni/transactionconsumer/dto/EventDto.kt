package com.darlanmarangoni.transactionconsumer.dto

import com.darlanmarangoni.transactionconsumer.domain.model.Account
import com.darlanmarangoni.transactionconsumer.domain.model.Transaction
import com.fasterxml.jackson.annotation.JsonProperty

data class EventDto(
    @JsonProperty("transaction")
    val transaction: TransactionDto,
    @JsonProperty("account")
    val account: AccountDto
)