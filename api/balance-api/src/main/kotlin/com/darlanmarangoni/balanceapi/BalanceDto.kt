package com.darlanmarangoni.balanceapi

data class ResponseDto(
    val accountId: String,
    val owner: String,
    val balance: BalanceDto,
    val updatedAt: String
)

data class BalanceDto(
    val amount: Double,
    val currency: String
)