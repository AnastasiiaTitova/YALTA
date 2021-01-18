package com.yalta.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.services.AuthService
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
    private val testAuth = Mockito.mock(AuthService::class.java)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testAuth.getUser()).thenReturn(User(1, "user", "", Driver))
        viewModel = ProfileViewModel(testAuth, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun goodLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testAuth.logout()).thenReturn(true)
        viewModel.logout()
        assertTrue(viewModel.loggedOut.value!!)
    }

    @Test
    fun badLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testAuth.logout()).thenReturn(false)
        viewModel.logout()
        assertFalse(viewModel.loggedOut.value!!)
    }

    @Test
    fun changePasswordTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.changePassword()
        assertTrue(viewModel.changePassword.value!!)
    }

}
