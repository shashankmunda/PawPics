package com.shashankmunda.pawpics.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.toUri
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.shashankmunda.pawpics.R
import com.shashankmunda.pawpics.ThemeStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class NavigationDrawerItem(
  @field:StringRes val resId: Int,
  val icon: ImageVector,
  val selected: Boolean,
  val onClick: (Context, CoroutineScope) -> Unit
)

@Singleton
class NavigationDrawerItems @Inject constructor(val firebaseAnalytics: FirebaseAnalytics,val themeStorage: ThemeStorage){

  val items = listOf(
    NavigationDrawerItem(R.string.invite_friend, Icons.Default.Share, false, { context, scope ->
      val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
          Intent.EXTRA_TEXT,
          context.getString(R.string.check_out_pawpics, context.packageName)
        )
        type = "text/plain"
      }
      context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_via)))
    }),
    NavigationDrawerItem(R.string.rate_us, Icons.Default.StarRate, false, { context,scope ->
      val intent = Intent(Intent.ACTION_VIEW).apply{
        data =
          "https://play.google.com/store/apps/details?id=com.shashankmunda.pawpics".toUri()
        setPackage("com.android.vending")
      }
      try {
        context.startActivity(intent)
      }
      catch(e: ActivityNotFoundException){
        firebaseAnalytics.logEvent("couldn't rate app") {
          param("reason", e.message ?: "unknown")
        }
      }
    }),
    NavigationDrawerItem(if(themeStorage.isDarkModeApplied() == true) R.string.switch_dark_mode else R.string.switch_dark_mode, Icons.Default.BrightnessMedium, false, { context,scope->
      scope.launch {
        if (themeStorage.isDarkModeApplied() == true) {
          themeStorage.setDarkModeApplied(false)
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
          themeStorage.setDarkModeApplied(true)
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
      }
    }),
    NavigationDrawerItem(R.string.privacy_policy, Icons.Default.PrivacyTip, false,{ context,scope ->
      context.startActivity(
        Intent(
          Intent.ACTION_VIEW,
          "https://shashankmunda.github.io/PawPics/privacy-policy".toUri()
        )
      )
    }),
    NavigationDrawerItem(R.string.share_feedback, Icons.Default.Share, false, { context,scope ->
      val intent = Intent(Intent.ACTION_SENDTO).apply {
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_subject))
        putExtra(
          Intent.EXTRA_TEXT,
          context.getString(R.string.feedback_body)
        )
        data = "mailto:shashankdec2000coder@gmail.com".toUri()
      }
      context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_feedback)))
    })
  )

}

