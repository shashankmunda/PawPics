package com.shashankmunda.pawpics.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.shashankmunda.pawpics.BuildConfig
import com.shashankmunda.pawpics.ThemeStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var themeStorage: ThemeStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        setContent {
            AppTheme {
                SetupNavigation(themeStorage)
            }
        }
    }

    @Composable
    fun SetupNavigation(themeStorage: ThemeStorage) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
                        Column {
                            Box(modifier= Modifier.background(color = MaterialTheme.colorScheme.primary).fillMaxWidth().padding(top = 0.dp)) {
                                Text(
                                    "PawPics",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            HorizontalDivider()
                            NavigationDrawerItem(
                                label = { Text("Invite Friend") },
                                icon = {
                                    Icon(Icons.Default.Share, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Check out PawPics! It's the best app for cat lovers!: https://play.google.com/store/apps/details?id=com.shashankmunda.pawpics"
                                        )
                                        type = "text/plain"
                                    }
                                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Rate Us") },
                                icon = {
                                    Icon(Icons.Default.StarRate, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                  val intent = Intent(Intent.ACTION_VIEW).apply{
                                    data = Uri.parse("https://play.google.com/store/apps/details?id=com.shashankmunda.pawpics")
                                    setPackage("com.android.vending")
                                  }
                                  try {
                                    startActivity(intent)
                                  }
                                  catch(e: ActivityNotFoundException){
                                    firebaseAnalytics.logEvent("couldn't rate app") {
                                      param("reason", e.message ?: "unknown")
                                    }
                                  }
                                }
                            )
                            NavigationDrawerItem(
                                label = { if(themeStorage.isDarkModeApplied() == true) Text("Switch to Light Mode") else Text("Switch to Dark Mode") },
                                icon = {
                                    Icon(Icons.Default.BrightnessMedium, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        if (themeStorage.isDarkModeApplied() == true) {
                                            themeStorage.setDarkModeApplied(false)
                                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                        } else {
                                            themeStorage.setDarkModeApplied(true)
                                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                        }
                                    }
                                }
                            )
                            // NavigationDrawerItem(
                            //   label = {Text("Favorites")},
                            //   icon = {
                            //     Icon(Icons.Default.Favorite, contentDescription = null)
                            //   },
                            //   selected = false,
                            //   onClick = {
                            //     navController.navigate(Favorites)
                            //     scope.launch {
                            //       drawerState.close()
                            //     }
                            //   }
                            // )
                            NavigationDrawerItem(
                                label = { Text("Privacy Policy") },
                                icon = {
                                    Icon(Icons.Default.PrivacyTip, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                          "https://shashankmunda.github.io/PawPics/privacy-policy".toUri()
                                        )
                                    )
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Share Feedback") },
                                icon = {
                                    Icon(Icons.Default.Feedback, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        putExtra(Intent.EXTRA_SUBJECT, "Feedback for CatApp")
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "I wanted to share the following feedback:\n"
                                        )
                                        data = "mailto:shashankdec2000coder@gmail.com".toUri()
                                    }
                                  startActivity(Intent.createChooser(intent, "Share Feedback"))
                                })
                        }
                        Text(
                            "Made With ❤\uFE0F in India \nVersion: ${BuildConfig.VERSION_NAME}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }, drawerState = drawerState
        ) {
            CatApp(themeStorage, drawerState, navController)
        }
    }
}


@Serializable data object HomeFeed
@Serializable data class CatDetail(val id: String, val ext: String)
@Serializable data object FiltersSearch
@Serializable data object Favorites