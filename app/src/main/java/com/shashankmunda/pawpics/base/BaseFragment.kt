package com.shashankmunda.pawpics.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding,VM: BaseViewModel>: Fragment() {

    open var sharedViewModel = false

    protected lateinit var mViewModel: VM
    protected abstract fun getViewModelClass(): Class<VM>

    private var _binding: VB? = null
    protected abstract fun getViewBinding(): VB
    protected val binding: VB
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    abstract fun observeData()

    abstract fun initViews()

    private fun init(){
        _binding = getViewBinding()
        mViewModel = if(sharedViewModel){
            ViewModelProvider(requireActivity()).get(
                getViewModelClass()
            )
        }
        else {
            ViewModelProvider(this).get(getViewModelClass())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}