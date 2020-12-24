package com.yalta.activities.fragments

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.yalta.R
import com.yalta.databinding.FragmentAdminBrowseBinding
import com.yalta.viewmodel.AdminBrowseViewModel

class AdminBrowseFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this).get(AdminBrowseViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAdminBrowseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_browse, container, false)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, HORIZONTAL))

        return binding.root
    }
}
