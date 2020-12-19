package com.yalta.viewmodel

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yalta.CoroutineTestRule
import com.yalta.repositories.*
import common.Driver
import common.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private val testUser = Mockito.mock(RealUserRepo::class.java)
    private val testLogout = Mockito.mock(RealLogoutRepo::class.java)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testUser.getUser()).thenReturn(GotUser(User(1, "user", "", Driver)))
        viewModel = ProfileViewModel(testUser, testLogout, coroutinesTestRule.testDispatcher)
    }

    @Test
    fun goodLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testLogout.logout()).thenReturn(LoggedOut())
        viewModel.logout()
        assertTrue(viewModel.loggedOut.value!!)
    }

    @Test
    fun badLogoutTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(testLogout.logout()).thenReturn(FailedResponse(Reason.BAD_CODE))
        viewModel.logout()
        assertFalse(viewModel.loggedOut.value!!)
    }

    @Test
    fun changePasswordTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.changePassword()
        assertTrue(viewModel.changePassword.value!!)
    }

}
