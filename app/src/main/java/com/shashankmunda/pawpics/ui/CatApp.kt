package com.shashankmunda.pawpics.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.shashankmunda.pawpics.ThemeStorage
import com.shashankmunda.pawpics.ui.details.CatImageScreen
import com.shashankmunda.pawpics.ui.feed.HomeFeedScreen
import com.shashankmunda.pawpics.ui.feed.HomeFeedViewModel
import com.shashankmunda.pawpics.ui.filter.SearchFiltersScreen
import com.shashankmunda.pawpics.util.getExtensionFromUrl

@Composable
fun CatApp(themeStorage: ThemeStorage, drawerState: DrawerState, navController: NavHostController) {
  CatNavHost(navController = navController, drawerState = drawerState)
}

@Composable
fun CatNavHost(
  navController: NavHostController,
  drawerState: DrawerState
) {
  NavHost(navController = navController, startDestination = HomeFeed) {
    composable<HomeFeed>(enterTransition = {
      slideInHorizontally(
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        initialOffsetX = { fullWidth ->
          -fullWidth
        })
    }, exitTransition = {
      slideOutHorizontally(
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
      ) { fullWidth ->
        fullWidth
      }
    }) {
      val viewModel = hiltViewModel<HomeFeedViewModel>()
      HomeFeedScreen(
        viewModel,
        onViewFilters = {
          navController.navigate(FiltersSearch)
        },
        onClick = { it ->
          navController.navigate(CatDetail(it.id, getExtensionFromUrl(it.url)!!))
        },
        drawerState = drawerState
      )
    }
    composable<CatDetail>(enterTransition = {
      slideInHorizontally(
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        initialOffsetX = { fullWidth ->
          fullWidth
        })
    }, exitTransition = {
      slideOutHorizontally(
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth ->
          fullWidth
        })
    }
    ) { backstackEntry ->
      val catDetailRoute = backstackEntry.toRoute<CatDetail>()
      CatImageScreen(
        onBackPressed = {
          navController.popBackStack()
        },
        imageId = catDetailRoute.id,
        fileExt = catDetailRoute.ext,
      )
    }
    composable<FiltersSearch>(enterTransition = {
      slideInHorizontally(
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        initialOffsetX = { fullWidth ->
          fullWidth
        })
    }, exitTransition = {
      slideOutHorizontally(
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth ->
          fullWidth
        })
    }
    ) { backStackEntry ->
      val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(HomeFeed)
      }
      val parentViewModel = hiltViewModel<HomeFeedViewModel>(parentEntry)
      SearchFiltersScreen(
        parentViewModel,
        onBackPressed = {
          navController.popBackStack()
        })
    }
  }
}