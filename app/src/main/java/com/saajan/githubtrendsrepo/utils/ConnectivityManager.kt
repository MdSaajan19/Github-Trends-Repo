package com.saajan.githubtrendsrepo.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.saajan.githubtrendsrepo.R

object ConnectivityManager {

    private var dialog: Dialog? = null

    fun isInternetAvailable(activity: Activity, fragment: Fragment?, requestCode: Int): Boolean {
        if (!isNetworkAvailable(activity))
            showInternetDialog(activity, fragment, requestCode)

        return isNetworkAvailable(activity)
    }

    fun isNetworkAvailable(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT < 23) {
            val ni = cm.activeNetworkInfo

            if (ni == null) false
            else
                ni.isConnected && (ni.type == ConnectivityManager.TYPE_MOBILE || ni.type == ConnectivityManager.TYPE_WIFI)

        } else {
            val n = cm.activeNetwork
            val nc = cm.getNetworkCapabilities(n)
            if (nc == null) false
            else
                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }

    private fun showInternetDialog(activity: Activity, fragment: Fragment?, requestCode: Int) {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()

        val ad = AlertDialog.Builder(activity)
        ad.setTitle(activity.getString(R.string.no_connection))
        ad.setMessage(activity.getString(R.string.turn_on_internet))
        //        ad.setCancelable(false);
        ad.setNegativeButton(activity.getString(R.string.mobile_data)) { _, _ ->
            val i = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
            if (fragment == null)
                activity.startActivityForResult(i, requestCode)
            else
                fragment.startActivityForResult(i, requestCode)
        }
        ad.setPositiveButton(activity.getString(R.string.Wifi)) { _, _ ->
            val i = Intent(Settings.ACTION_WIFI_SETTINGS)
            if (fragment == null)
                activity.startActivityForResult(i, requestCode)
            else
                fragment.startActivityForResult(i, requestCode)
        }
        dialog = ad.show()
    }
}