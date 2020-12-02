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

    private fun ensureDefaults() {
        assertEquals("root", viewModel.userName.value)
        assertEquals("driver", viewModel.role.value)
    }

    @Test
    fun testDefaults() {
        ensureDefaults()
    }

    @Test
    fun changeValueOnce() = coroutinesTestRule.testDispatcher.runBlockingTest {
        ensureDefaults()
        viewModel.changeValue()
        assertEquals("root", viewModel.userName.value)
        assertEquals("admin", viewModel.role.value)
    }

    @Test
    fun changeValueTwice() = coroutinesTestRule.testDispatcher.runBlockingTest {
        ensureDefaults()
        viewModel.changeValue()
        viewModel.changeValue()
        ensureDefaults()
    }

    @Test
    fun logoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.logout()
        assertTrue(viewModel.loggedOut.value!!)
    }
}
