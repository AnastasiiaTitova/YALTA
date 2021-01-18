package com.yalta.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.services.AuthService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.*

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val test = Mockito.mock(AuthService::class.java)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = LoginViewModel(test, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun goodLogin() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("OK", "OK")).thenReturn(Optional.of(true))

        viewModel.user.value = "OK"
        viewModel.password.value = "OK"
        viewModel.login()
        assertFalse(viewModel.showLoginError.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
        assertTrue(viewModel.loggedIn.value!!)
    }

    @Test
    fun badLogin() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("notOK", "notOK")).thenReturn(Optional.of(false))

        viewModel.user.value = "notOK"
        viewModel.password.value = "notOK"
        viewModel.login()
        assertTrue(viewModel.showLoginError.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
        assertFalse(viewModel.loggedIn.value!!)
    }

    @Test
    fun connectionProblem() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("OK", "OK")).thenReturn(Optional.empty())

        viewModel.user.value = "OK"
        viewModel.password.value = "OK"
        viewModel.login()
        assertFalse(viewModel.showLoginError.value!!)
        assertTrue(viewModel.showConnectionError.value!!)
        assertFalse(viewModel.loggedIn.value!!)
    }
}
