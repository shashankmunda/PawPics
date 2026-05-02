package com.shashankmunda.pawpics.util

import android.content.Context
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase

class ReferrerManager @Inject constructor(@ApplicationContext val context: Context) {
  @Inject lateinit var firebaseAnalytics : FirebaseAnalytics

  private val prefs = context.getSharedPreferences("referrer_prefs", Context.MODE_PRIVATE)
  private val KEY_REFERRER_FETCHED = "referrer_fetched"
  private val KEY_REFERRER_DATA = "referrer_data"

  fun fetchReferrerIfNeeded() {
    // Check if we've already fetched the referrer
    if (prefs.getBoolean(KEY_REFERRER_FETCHED, false)) {
      return
    }

    val referrerClient = InstallReferrerClient.newBuilder(context).build()
    referrerClient.startConnection(object : InstallReferrerStateListener {
      override fun onInstallReferrerSetupFinished(responseCode: Int) {
        when (responseCode) {
          InstallReferrerClient.InstallReferrerResponse.OK -> {
            try {
              val response = referrerClient.installReferrer
              val referrerUrl = response.installReferrer
              val clickTime = response.referrerClickTimestampSeconds
              val installTime = response.installBeginTimestampSeconds
              // Save the referrer data
              prefs.edit {
                putBoolean(KEY_REFERRER_FETCHED, true)
                putString(KEY_REFERRER_DATA, referrerUrl)
              }
              // Send to your analytics/backend
              firebaseAnalytics.logEvent("install_referrer") {
                param("referrer", referrerUrl)
                param("click_timestamp", clickTime)
                param("install_timestamp", installTime)
              }
            } finally {
              referrerClient.endConnection()
            }
          }

          InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
            firebaseAnalytics.logEvent("install_referrer_feature_not_supported") {
              param("referrer", "feature_not_supported")
            }
          }

          InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
            firebaseAnalytics.logEvent("service_unavailable") {
              param("referrer", "service_unavailable")
            }
          }
        }
      }

      override fun onInstallReferrerServiceDisconnected() {
        // Connection lost, can retry on next app start
      }
    })
  }

}