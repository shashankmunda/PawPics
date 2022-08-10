package com.shashankmunda.pawpics.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageFragmentBinding
import com.shashankmunda.pawpics.ui.MainActivity

class CatImageFragment: Fragment(R.layout.cat_image_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=CatImageFragmentBinding.bind(view)
        setUpToolbar(binding)
        //(requireActivity() as MainActivity).setupActionBar(binding.catImageToolbar)
    }

    private fun setUpToolbar(binding: CatImageFragmentBinding) {
        binding.catImageToolbar.apply {
            inflateMenu(R.menu.cat_menu)
            setOnMenuItemClickListener(catMenuListener)
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private val catMenuListener = Toolbar.OnMenuItemClickListener { item ->
        when(item.itemId){
            R.id.save -> {
                true
            }
            R.id.share -> {
                true
            }
            R.id.set_wallpaper -> {
                true
            }
            else -> false
        }
    }
}