package com.yalta.utils

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

data class UniversalRecyclerItem(
    val data: Any,
    @LayoutRes val layoutId: Int,
    val variableId: Int
) {
    fun bind(binding: ViewDataBinding) {
        binding.setVariable(variableId, data)
    }
}
