package com.darlanmarangoni.balanceapi

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.ZoneOffset


@RestController
class BalanceController(val balanceRepository: BalanceRepository) {

    @GetMapping("/balance/{accountId}")
    fun balance(@PathVariable accountId: String) = balanceRepository.findAccountById(accountId)
        .map { account ->
            val epochSecond = account.updatedAt / 1000000
            val value = account.updatedAt % 1000000
            val instant: Instant = Instant.ofEpochSecond(epochSecond, value * 1000)

            ResponseDto(
                accountId = account.id,
                owner = account.owner,
                balance = BalanceDto(
                    amount = account.balance.amount,
                    currency = account.balance.currency
                ),
                updatedAt = instant.atOffset(ZoneOffset.of("-03:00")).toString()
            )
        }
        .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")))
}