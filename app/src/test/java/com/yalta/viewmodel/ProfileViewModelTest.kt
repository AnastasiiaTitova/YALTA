package com.yalta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.services.LogoutService
import com.yalta.services.UserService
import common.Driver
import common.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private val testUser = Mockito.mock(UserService::class.java)
    private val testLogout = Mockito.mock(LogoutService::class.java)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testUser.getUser()).thenReturn(User(1, "user", "", Driver))
        viewModel = ProfileViewModel(testUser, testLogout, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun goodLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testLogout.logout()).thenReturn(true)
        viewModel.logout()
        assertTrue(viewModel.loggedOut.value!!)
    }

    @Test
    fun badLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testLogout.logout()).thenReturn(false)
        viewModel.logout()
        assertFalse(viewModel.loggedOut.value!!)
    }

    @Test
    fun changePasswordTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.changePassword()
        assertTrue(viewModel.changePassword.value!!)
    }

}
