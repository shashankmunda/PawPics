package com.shashankmunda.pawpics.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.shashankmunda.pawpics.BuildConfig
import com.shashankmunda.pawpics.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var navigationDrawerItems: NavigationDrawerItems
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SetupNavigation(navigationDrawerItems)
            }
        }
    }

    @Composable
    fun SetupNavigation(navigationDrawerItems: NavigationDrawerItems) {
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
                            navigationDrawerItems.items.forEach {
                                NavigationDrawerItem(
                                    label = { Text(stringResource(it.resId)) },
                                    icon = { Icon(it.icon, contentDescription = null)},
                                    shape = RectangleShape,
                                    selected = it.selected,
                                    onClick = {
                                        it.onClick(this@HomeActivity, scope)
                                    }
                                )
                            }
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