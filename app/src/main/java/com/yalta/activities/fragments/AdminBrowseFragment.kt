package com.yalta.activities.fragments

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.yalta.R
import com.yalta.databinding.FragmentAdminBrowseBinding
import com.yalta.utils.ViewUtils.hideKeyboard
import com.yalta.viewmodel.AdminBrowseViewModel
import kotlinx.android.synthetic.main.fragment_admin_browse.*

class AdminBrowseFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this).get(AdminBrowseViewModel::class.java) }
    lateinit var binding: FragmentAdminBrowseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_browse, container, false)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, HORIZONTAL))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pointSearch.clearFocus()
        recyclerView.requestFocus()

        pointSearch.addTextChangedListener {
            binding.viewmodel?.filterPoints()
        }

        pointSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                pointSearch.clearFocus()
                recyclerView.requestFocus()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }
}
