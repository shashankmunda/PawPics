package com.shashankmunda.pawpics.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shashankmunda.pawpics.ThemeStorage
import com.shashankmunda.pawpics.ui.details.CatImageScreen
import com.shashankmunda.pawpics.ui.feed.HomeFeedScreen
import com.shashankmunda.pawpics.ui.filter.SearchFiltersScreen

@Composable
fun CatApp(themeStorage: ThemeStorage) {
  val navController = rememberNavController()
  CatNavHost(navController = navController, themeStorage = themeStorage)
}

@Composable
fun CatNavHost(navController: NavHostController, themeStorage: ThemeStorage) {
  NavHost(navController = navController, startDestination = HomeFeed) {
    composable<HomeFeed> {
      HomeFeedScreen(themesStorage = themeStorage, onViewFilters = {
        navController.navigate(FiltersSearch)
      }, onClick = { it ->
        navController.navigate(CatDetail(it.id, it.url))
    })
    }
    composable<CatDetail> { backstackEntry ->
      val catDetailRoute = backstackEntry.toRoute<CatDetail>()
      CatImageScreen(
        onBackPressed = {
          navController.popBackStack()
        },
        imageId = catDetailRoute.id,
        fileExt =catDetailRoute.ext
      )
    }
    composable<FiltersSearch> {
      SearchFiltersScreen(onBackPressed = {
        navController.popBackStack()
      })
    }
  }
}