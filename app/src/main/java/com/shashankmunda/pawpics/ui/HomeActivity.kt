package com.shashankmunda.pawpics.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.shashankmunda.pawpics.ThemeStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var themeStorage: ThemeStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        setContent {
            CatApp(themeStorage)
        }
    }
}

@Serializable data object HomeFeed
@Serializable data class CatDetail(val id: String, val ext: String)
@Serializable data object FiltersSearch