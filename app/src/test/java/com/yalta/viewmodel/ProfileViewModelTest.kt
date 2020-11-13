package com.yalta.viewmodel

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = ProfileViewModel()
    }

    private fun ensureDefaults() {
        assertEquals(viewModel.userName.value, "root")
        assertEquals(viewModel.role.value, "driver")
    }

    @Test
    fun testDefaults() {
        ensureDefaults()
    }

    @Test
    fun changeValueOnce() {
        ensureDefaults()
        viewModel.changeValue()
        assertEquals(viewModel.userName.value, "root")
        assertEquals(viewModel.role.value, "admin")
    }

    @Test
    fun changeValueTwice() {
        ensureDefaults()
        viewModel.changeValue()
        viewModel.changeValue()
        ensureDefaults()
    }
}
