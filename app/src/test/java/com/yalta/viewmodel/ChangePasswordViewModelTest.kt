package com.yalta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakePasswordRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChangePasswordViewModelTest {
    private lateinit var viewModel: ChangePasswordViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = ChangePasswordViewModel(FakePasswordRepo(), coroutinesTestRule.testDispatcher)
    }

    @Test
    fun good_change_password_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.firstPassword.value = "OK"
        viewModel.secondPassword.value = "OK"
        viewModel.changePassword()
        assertFalse(viewModel.showError.value!!)
        assertTrue(viewModel.closeActivity.value!!)
    }

    @Test
    fun change_password_null_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.firstPassword.value = "OK"
        viewModel.changePassword()
        assertTrue(viewModel.showError.value!!)
        assertFalse(viewModel.closeActivity.value!!)
    }

    @Test
    fun change_password_different_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.firstPassword.value = "OK"
        viewModel.secondPassword.value = "NotOK"
        viewModel.changePassword()
        assertTrue(viewModel.showError.value!!)
        assertFalse(viewModel.closeActivity.value!!)
    }
}
