package com.yalta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.repositories.FailedResponse
import com.yalta.repositories.RealLoginRepo
import com.yalta.repositories.Reason
import com.yalta.repositories.SuccessfulLogin
import common.Driver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val test = Mockito.mock(RealLoginRepo::class.java)

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
        Mockito.`when`(test.login("OK", "OK")).thenReturn(SuccessfulLogin("token", Driver))

        viewModel.user.value = "OK"
        viewModel.password.value = "OK"
        viewModel.login()
        assertFalse(viewModel.showLoginError.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
        assertTrue(viewModel.loggedIn.value!!)
    }

    @Test
    fun badLogin() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("notOK", "notOK")).thenReturn(FailedResponse(Reason.BAD_CODE))

        viewModel.user.value = "notOK"
        viewModel.password.value = "notOK"
        viewModel.login()
        assertTrue(viewModel.showLoginError.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
        assertFalse(viewModel.loggedIn.value!!)
    }

    @Test
    fun connectionProblem() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.login("OK", "OK")).thenReturn(FailedResponse(Reason.FAILED_CONNECTION))

        viewModel.user.value = "OK"
        viewModel.password.value = "OK"
        viewModel.login()
        assertFalse(viewModel.showLoginError.value!!)
        assertTrue(viewModel.showConnectionError.value!!)
        assertFalse(viewModel.loggedIn.value!!)
    }
}
