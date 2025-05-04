package com.shashankmunda.pawpics.ui

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.shashankmunda.pawpics.databinding.ActivityHomeBinding
import com.shashankmunda.pawpics.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)
    override fun initViews() {
        firebaseAnalytics = Firebase.analytics
    }
}