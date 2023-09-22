package com.pp.network.utils

import android.Manifest.permission
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.pp.network.routerservice.NetWorkAppImpl

enum class NetworkType {
    NETWORK_ETHERNET,
    NETWORK_WIFI,
    NETWORK_5G,
    NETWORK_4G,
    NETWORK_3G,
    NETWORK_2G,
    NETWORK_UNKNOWN,
    NETWORK_NO
}

@RequiresPermission(permission.ACCESS_NETWORK_STATE)
private fun isEthernet(): Boolean {
    NetWorkAppImpl.getApp().getSystemService(Context.CONNECTIVITY_SERVICE).let {
        if (null == it) return false
        it as ConnectivityManager
    }.run {
        val info = getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) ?: return false
        val state = info.state ?: return false
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING
    }
}

@RequiresPermission(permission.ACCESS_NETWORK_STATE)
private fun getActiveNetworkInfo(): NetworkInfo? {
    NetWorkAppImpl.getApp().getSystemService(Context.CONNECTIVITY_SERVICE).let {
        if (null == it) return null
        it as ConnectivityManager
    }.apply {
        return activeNetworkInfo
    }
}

fun getNetworkType(): NetworkType {
    if (isEthernet()) {
        return NetworkType.NETWORK_ETHERNET
    }
    val info: NetworkInfo? = getActiveNetworkInfo()
    if (null == info || !info.isAvailable) return NetworkType.NETWORK_NO
    return if (info.type == ConnectivityManager.TYPE_WIFI) {
        NetworkType.NETWORK_WIFI
    } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
        when (info.subtype) {
            TelephonyManager.NETWORK_TYPE_GSM,
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN,
            -> NetworkType.NETWORK_2G
            TelephonyManager.NETWORK_TYPE_TD_SCDMA,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP,
            -> NetworkType.NETWORK_3G
            TelephonyManager.NETWORK_TYPE_IWLAN,
            TelephonyManager.NETWORK_TYPE_LTE,
            -> NetworkType.NETWORK_4G
            TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NETWORK_5G
            else -> {
                val subtypeName = info.subtypeName
                if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                    || subtypeName.equals("WCDMA", ignoreCase = true)
                    || subtypeName.equals("CDMA2000", ignoreCase = true)
                ) {
                    NetworkType.NETWORK_3G
                } else {
                    NetworkType.NETWORK_UNKNOWN
                }
            }
        }
    } else {
        NetworkType.NETWORK_UNKNOWN
    }
}

fun registerNetStatesChangedListener(
    lifecycleOwner: LifecycleOwner?,
    observer: Observer<NetworkType>,
) {
    NetStateChangeBroadcastReceiver.registerReceiver(lifecycleOwner, observer)
}

fun unRegisterNetStatesChangedListener(observer: Observer<NetworkType>) {
    NetStateChangeBroadcastReceiver.unRegisterReceiver(observer)
}

private object NetStateChangeBroadcastReceiver : BroadcastReceiver() {
    private const val TAG = "NetBroadcastReceiver"
    private val netState = MutableLiveData<NetworkType>()

    /**
     * 注册网络状态监听
     */
    fun registerReceiver(lifecycleOwner: LifecycleOwner?, observer: Observer<NetworkType>) {
        if (!netState.hasObservers()) {
            NetWorkAppImpl.getApp().registerReceiver(
                NetStateChangeBroadcastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (null == lifecycleOwner) {
            netState.observeForever(observer)
        } else {
            netState.observe(lifecycleOwner, observer)
        }
    }

    fun unRegisterReceiver(observer: Observer<NetworkType>) {
        netState.removeObserver(observer)
        if (!netState.hasObservers()) {
            NetWorkAppImpl.getApp().unregisterReceiver(NetStateChangeBroadcastReceiver)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
//        Log.e(TAG, "action: $action")
        if (ConnectivityManager.CONNECTIVITY_ACTION != intent?.action) return

        val networkType: NetworkType = getNetworkType()
        if (netState.value == networkType) return

        netState.value = networkType
    }

}
