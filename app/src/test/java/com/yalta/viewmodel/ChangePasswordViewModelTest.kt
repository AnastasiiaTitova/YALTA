package com.yalta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ChangePasswordViewModelTest {
    private lateinit var viewModel: ChangePasswordViewModel
    private val test = Mockito.mock(RealPasswordRepo::class.java)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = ChangePasswordViewModel(test, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun goodChangePasswordTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.changePassword("OK")).thenReturn(PasswordChanged())
        viewModel.firstPassword.value = "OK"
        viewModel.secondPassword.value = "OK"
        viewModel.changePassword()
        assertFalse(viewModel.showPasswordError.value!!)
        assertTrue(viewModel.closeActivity.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
    }

    @Test
    fun changePasswordNullTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.firstPassword.value = "OK"
        viewModel.changePassword()
        assertTrue(viewModel.showPasswordError.value!!)
        assertFalse(viewModel.closeActivity.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
    }

    @Test
    fun changePasswordDifferentTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.firstPassword.value = "OK"
        viewModel.secondPassword.value = "NotOK"
        viewModel.changePassword()
        assertTrue(viewModel.showPasswordError.value!!)
        assertFalse(viewModel.closeActivity.value!!)
        assertFalse(viewModel.showConnectionError.value!!)
    }

    @Test
    fun failedConnectionTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(test.changePassword("OK")).thenReturn(FailedResponse(Reason.FAILED_CONNECTION))
        viewModel.firstPassword.value = "OK"
        viewModel.secondPassword.value = "OK"
        viewModel.changePassword()
        assertFalse(viewModel.showPasswordError.value!!)
        assertFalse(viewModel.closeActivity.value!!)
        assertTrue(viewModel.showConnectionError.value!!)
    }
}
