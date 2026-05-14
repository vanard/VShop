// Unit tests for AuthUseCase validation and flow behavior

package com.vanard.domain

import com.vanard.common.UIState
import com.vanard.domain.model.Address
import com.vanard.domain.model.User
import com.vanard.domain.model.auth.AuthResponse
import com.vanard.domain.model.auth.LoginRequest
import com.vanard.domain.model.auth.SignUpRequest
import com.vanard.domain.repository.AuthRepository
import com.vanard.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthUseCaseTest {

    // Simple fake repository that returns a successful response for both login and sign up.
    private class FakeAuthRepository : AuthRepository {
        private val dummyUser = User(
            id = "dummy-id",
            email = "demo@vshop.com",
            firstName = "Demo",
            lastName = "User",
            phone = "+1234567890",
            address = Address(
                street = "123 Main St",
                city = "New York",
                zipCode = "10001",
                country = "USA"
            )
        )
        private val dummyToken = "test-token"

        override suspend fun login(request: LoginRequest) = flow {
            emit(UIState.Success(AuthResponse(dummyUser, dummyToken)))
        }

        override suspend fun signUp(request: SignUpRequest) = flow {
            emit(UIState.Success(AuthResponse(dummyUser, dummyToken)))
        }

        override suspend fun logout() {}

        override fun getCurrentUser() = flow { emit(UIState.Success<User?>(null)) }

        override suspend fun isUserLoggedIn() = false

        override suspend fun saveUserSession(user: User, token: String) {}

        override suspend fun clearUserSession() {}
    }

    private val useCase = AuthUseCase(FakeAuthRepository())

    @Test
    fun `login with invalid email returns UIState Error`() = runBlocking {
        val emissions = useCase.login("", "validPass").toList()
        assertTrue(emissions.isNotEmpty())
        val first = emissions.first()
        assertTrue(first is UIState.Error)
        if (first is UIState.Error) {
            assertEquals("Email cannot be empty", first.errorMessage)
        }
    }

    @Test
    fun `login with valid credentials returns UIState Success`() = runBlocking {
        val emissions = useCase.login("demo@vshop.com", "demo123").toList()
        assertTrue(emissions.isNotEmpty())
        val first = emissions.first()
        assertTrue(first is UIState.Success)
        if (first is UIState.Success) {
            val response = first.data
            assertEquals("demo@vshop.com", response.user.email)
            // token comes from fake repo
        }
    }

    @Test
    fun `sign up with invalid password returns UIState Error`() = runBlocking {
        val emissions = useCase.signUp(
            email = "newuser@example.com",
            password = "123", // too short
            firstName = "New",
            lastName = "User",
            phone = null
        ).toList()
        assertTrue(emissions.isNotEmpty())
        val first = emissions.first()
        assertTrue(first is UIState.Error)
        if (first is UIState.Error) {
            assertEquals("Password must be at least 6 characters", first.errorMessage)
        }
    }

    @Test
    fun `sign up with valid data returns UIState Success`() = runBlocking {
        val emissions = useCase.signUp(
            email = "newuser@example.com",
            password = "securePass",
            firstName = "New",
            lastName = "User",
            phone = "1234567890"
        ).toList()
        assertTrue(emissions.isNotEmpty())
        val first = emissions.first()
        assertTrue(first is UIState.Success)
        if (first is UIState.Success) {
            val response = first.data
            assertEquals("demo@vshop.com", response.user.email)
        }
    }
}
