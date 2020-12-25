package com.yalta.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("items")
fun setRecyclerViewItems(
    recyclerView: RecyclerView,
    items: List<UniversalRecyclerItem>?
) {
    var adapter = (recyclerView.adapter as? UniversalRecyclerViewAdapter)
    if (adapter == null) {
        adapter = UniversalRecyclerViewAdapter()
        recyclerView.adapter = adapter
    }

    adapter.updateData(items.orEmpty())
}
