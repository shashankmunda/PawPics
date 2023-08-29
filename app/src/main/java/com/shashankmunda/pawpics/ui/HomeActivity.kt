package com.shashankmunda.pawpics.ui

import com.shashankmunda.pawpics.databinding.ActivityHomeBinding
import com.shashankmunda.pawpics.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)
    override fun initViews() {
    }
}