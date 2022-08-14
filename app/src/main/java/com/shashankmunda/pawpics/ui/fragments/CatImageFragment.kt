package com.shashankmunda.pawpics.ui.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import com.example.pawpics.R
import com.example.pawpics.databinding.CatImageFragmentBinding
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.ui.CatViewModel
import com.shashankmunda.pawpics.util.Result
import com.shashankmunda.pawpics.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatImageFragment: Fragment(R.layout.cat_image_fragment) {
    private lateinit var catImageId: String
    private val catViewModel:CatViewModel by activityViewModels()
    private lateinit var catImageView:AppCompatImageView
    private val displayMetrics: DisplayMetrics by lazy {
        requireContext().resources.displayMetrics
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=CatImageFragmentBinding.bind(view)
        catImageView=binding.catFullImageView
        setUpToolbar(binding)
        catImageId=arguments?.getString("image_id")!!
        catViewModel.currCatStatus.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    displayCatImage(result.data!!,binding)
                }
                is Result.Error -> {
                    hideProgressBar(binding)
                    Toast.makeText(context,result.message,Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressBar(binding)
                }
            }
        }
        if(catViewModel.isCatSpecsAvailable(catImageId))
        {
            catViewModel.getCatSpecs(catImageId)?.let { displayCatImage(it,binding) }
        }
        else catViewModel.fetchCatSpecs(lifecycleScope,catImageId)
    }

    private fun hideProgressBar(binding: CatImageFragmentBinding) {
        binding.loadingProgressBar.visibility=View.GONE
    }

    private fun showProgressBar(binding: CatImageFragmentBinding) {
        binding.loadingProgressBar.visibility=View.VISIBLE
    }

    private fun displayCatImage(cat: Cat, binding:CatImageFragmentBinding) {
        catImageView.layoutParams.height= (1.0f*displayMetrics.widthPixels*cat.height!!).toInt()/cat.width!!
        catImageView.load(cat.url){
            placeholder(Utils.provideShimmerDrawable(requireContext()))
            allowHardware(false)
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.ENABLED)
            listener(
                onSuccess = { _, _ ->
                    hideProgressBar(binding)
                },
                onError = {_,_ ->
                    hideProgressBar(binding)
                }
            )
        }
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