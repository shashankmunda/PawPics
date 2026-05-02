package com.shashankmunda.pawpics.ui

import android.content.ActivityNotFoundException
import android.content.Intent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.shashankmunda.pawpics.BuildConfig
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.ThemeStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var themeStorage: ThemeStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
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
                            Box(modifier= Modifier
                              .background(color = MaterialTheme.colorScheme.primary)
                              .fillMaxWidth()
                              .padding(top = 0.dp)) {
                                Text(
                                  stringResource(R.string.app_name),
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            HorizontalDivider()
                            NavigationDrawerItem(
                                label = { Text(stringResource(R.string.invite_friend)) },
                                icon = {
                                    Icon(Icons.Default.Share, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                          getString(R.string.check_out_pawpics, packageName)
                                        )
                                        type = "text/plain"
                                    }
                                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(stringResource(R.string.rate_us)) },
                                icon = {
                                    Icon(Icons.Default.StarRate, contentDescription = null)
                                },
                                selected = false,
                                onClick = {
                                  val intent = Intent(Intent.ACTION_VIEW).apply{
                                    data =
                                      "https://play.google.com/store/apps/details?id=com.shashankmunda.pawpics".toUri()
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
                                label = { if(themeStorage.isDarkModeApplied() == true) Text(stringResource(R.string.switch_light_mode)) else Text(stringResource(R.string.switch_dark_mode)) },
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
                            NavigationDrawerItem(
                                label = { Text(stringResource(R.string.privacy_policy)) },
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
                                label = { Text(stringResource(R.string.share_feedback)) },
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
                                  startActivity(Intent.createChooser(intent, getString(R.string.share_feedback)))
                                })
                        }
                        Text(
                            getString(R.string.made_in_india, BuildConfig.VERSION_NAME),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }, drawerState = drawerState
        ) {
            CatApp(drawerState, navController)
        }
    }
}


@Serializable data object HomeFeed
@Serializable data class CatDetail(val id: String, val ext: String)
@Serializable data object FiltersSearch