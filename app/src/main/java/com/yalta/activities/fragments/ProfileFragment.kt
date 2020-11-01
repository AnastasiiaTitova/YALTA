package com.yalta.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.yalta.R
import com.yalta.activities.LoginActivity
import com.yalta.services.SessionService

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val button: Button = view.findViewById(R.id.logout)
        button.setOnClickListener {
            SessionService.discardSession()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return view
    }
}
