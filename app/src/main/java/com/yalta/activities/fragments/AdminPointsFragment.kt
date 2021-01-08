package com.yalta.activities.fragments

import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.yalta.R
import com.yalta.activities.AddPointActivity
import com.yalta.databinding.FragmentAdminPointsBinding
import com.yalta.di.YaltaApplication
import com.yalta.utils.ViewUtils.hideKeyboard
import com.yalta.viewModel.AdminPointsViewModel
import kotlinx.android.synthetic.main.fragment_admin_points.*
import javax.inject.Inject

class AdminPointsFragment : Fragment() {
    @Inject lateinit var viewModel: AdminPointsViewModel

    lateinit var binding: FragmentAdminPointsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_points, container, false)

        YaltaApplication.appComponent.inject(this)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, HORIZONTAL))
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_red_light)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pointSearchField.clearFocus()
        recyclerView.requestFocus()

        viewModel.pointsUpdated.observeForever { pointsUpdated ->
            if (pointsUpdated) {
                swipeContainer.isRefreshing = false
            }
        }

        swipeContainer.setOnRefreshListener {
            viewModel.updatePoints()
        }

        pointSearchField.addTextChangedListener {
            binding.viewModel?.filterPoints()
        }

        pointSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                pointSearchField.clearFocus()
                hideKeyboard()
                true
            } else {
                false
            }
        }

        floatingAddPointButton.setOnClickListener {
            startActivity(Intent(activity, AddPointActivity::class.java))
        }
    }
}
