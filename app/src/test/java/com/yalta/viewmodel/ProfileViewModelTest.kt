package com.yalta.viewmodel

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.repositories.FakeLogoutRepo
import com.yalta.repositories.FakeUserRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        viewModel = ProfileViewModel(FakeUserRepo(), FakeLogoutRepo(), coroutinesTestRule.testDispatcher)
    }

    @Test
    fun logout_test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.logout()
        assertTrue(viewModel.loggedOut.value!!)
    }
}
