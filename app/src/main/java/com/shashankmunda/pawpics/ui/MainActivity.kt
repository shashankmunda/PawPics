package com.shashankmunda.pawpics.ui

import android.os.Bundle
import com.example.pawpics.databinding.ActivityMainBinding
import com.shashankmunda.pawpics.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun initViews() {
    }
}