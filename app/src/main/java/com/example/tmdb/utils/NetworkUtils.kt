package com.example.tmdb.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


object NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
       return connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
    }
}