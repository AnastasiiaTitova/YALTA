package com.yalta.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yalta.R
import com.yalta.activities.LoginActivity
import com.yalta.databinding.FragmentProfileBinding
import com.yalta.services.SessionService
import com.yalta.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.logout.setOnClickListener {
            SessionService.discardSession()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.changeValue.setOnClickListener {
            viewModel.changeValue()
        }

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this

        viewModel.userName.observe(viewLifecycleOwner, Observer<String> { newName ->
            binding.name = newName
        })
        viewModel.role.observe(viewLifecycleOwner, Observer<String> { newRole ->
            binding.role = newRole
        })

        return binding.root
    }
}
