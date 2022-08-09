package com.shashankmunda.pawpics.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageFragmentBinding

class CatImageFragment: Fragment(R.layout.cat_image_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=CatImageFragmentBinding.bind(view)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cat_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save ->{
                TODO("implement save functionality")
            }
            R.id.share ->{
                TODO("implement share functionality")
            }
            R.id.set_wallpaper->{
                TODO("implement set wallpaper")
            }
            else -> false
        }
    }
}