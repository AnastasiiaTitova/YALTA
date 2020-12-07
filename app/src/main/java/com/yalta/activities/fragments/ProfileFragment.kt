package com.yalta.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yalta.R
import com.yalta.activities.ChangePasswordActivity
import com.yalta.activities.LoginActivity
import com.yalta.databinding.FragmentProfileBinding
import com.yalta.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.viewModel?.loggedOut?.observe(viewLifecycleOwner, { loggedOut ->
            if (loggedOut) {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        })

        binding.viewModel?.changePassword?.observe(viewLifecycleOwner, { changePassword ->
            if (changePassword) {
                startActivity(Intent(activity, ChangePasswordActivity::class.java))
            }
        })

        return binding.root
    }
}
