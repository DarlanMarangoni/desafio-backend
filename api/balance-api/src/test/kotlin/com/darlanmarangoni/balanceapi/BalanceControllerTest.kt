package com.darlanmarangoni.balanceapi

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class BalanceControllerTest {

    @Test
    fun `should return account balance when account exists`() {
        val accountId = "account-123"
        val account = Account(
            id = accountId,
            owner = "João Silva",
            status = "ACTIVE",
            balance = Balance(amount = 1500.50, currency = "BRL"),
            updatedAt = 1712000000000000L
        )

        val balanceRepository = mockk<BalanceRepository>()
        every { balanceRepository.findAccountById(accountId) } returns Mono.just(account)

        val controller = BalanceController(balanceRepository)
        val webTestClient = WebTestClient.bindToController(controller).build()

        webTestClient.get()
            .uri("/balance/{accountId}", accountId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.accountId").isEqualTo(accountId)
            .jsonPath("$.owner").isEqualTo("João Silva")
            .jsonPath("$.balance.amount").isEqualTo(1500.50)
            .jsonPath("$.balance.currency").isEqualTo("BRL")
    }

    @Test
    fun `should return 404 when account does not exist`() {
        val accountId = "non-existent-account"

        val balanceRepository = mockk<BalanceRepository>()
        every { balanceRepository.findAccountById(accountId) } returns Mono.empty()

        val controller = BalanceController(balanceRepository)
        val webTestClient = WebTestClient.bindToController(controller).build()

        webTestClient.get()
            .uri("/balance/{accountId}", accountId)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return account with zero balance`() {
        val accountId = "account-zero"
        val account = Account(
            id = accountId,
            owner = "Maria Santos",
            status = "ACTIVE",
            balance = Balance(amount = 0.0, currency = "BRL"),
            updatedAt = 1712000000000000L
        )

        val balanceRepository = mockk<BalanceRepository>()
        every { balanceRepository.findAccountById(accountId) } returns Mono.just(account)

        val controller = BalanceController(balanceRepository)
        val webTestClient = WebTestClient.bindToController(controller).build()

        webTestClient.get()
            .uri("/balance/{accountId}", accountId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.accountId").isEqualTo(accountId)
            .jsonPath("$.balance.amount").isEqualTo(0.0)
    }

    @Test
    fun `should return account with different currency`() {
        val accountId = "account-usd"
        val account = Account(
            id = accountId,
            owner = "John Doe",
            status = "ACTIVE",
            balance = Balance(amount = 500.75, currency = "USD"),
            updatedAt = 1712000000000000L
        )

        val balanceRepository = mockk<BalanceRepository>()
        every { balanceRepository.findAccountById(accountId) } returns Mono.just(account)

        val controller = BalanceController(balanceRepository)
        val webTestClient = WebTestClient.bindToController(controller).build()

        webTestClient.get()
            .uri("/balance/{accountId}", accountId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.balance.currency").isEqualTo("USD")
            .jsonPath("$.balance.amount").isEqualTo(500.75)
    }
}