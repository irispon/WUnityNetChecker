package com.example;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.unity3d.player.UnityPlayer;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class WNetChecker extends PhoneStateListener {

    private static WNetChecker _instance;
    private static Activity _context;
    TelephonyManager mTelephonyManager;
    ConnectivityManager cm;
    NetworkInfo nInfo;
    int netStrength;
    public void WNetChecker()
    {
         cm = (ConnectivityManager)_context.getSystemService(_context.CONNECTIVITY_SERVICE);
         nInfo = cm.getActiveNetworkInfo();
        mTelephonyManager = (TelephonyManager)
                _context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static WNetChecker Instance()
    {
        if(_instance==null){
            _instance = new WNetChecker();
            _context = UnityPlayer.currentActivity;
        }
        return  _instance;
    }
    public float GetNetWorkStrength()
    {

        return netStrength;
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        netStrength = signalStrength.getGsmSignalStrength();
        netStrength = (2 * netStrength) - 113; // -> dBm
    }

    public int GetNetWorkState()
    {
        int netWorkType = mTelephonyManager.getNetworkType();
        switch (netWorkType)
        {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: {
                //Toast.makeText(getApplicationContext(), "Connection Available is 2G",Toast.LENGTH_SHORT).show();
                return 1;
            }
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP: {
//                Toast.makeText(getApplicationContext(), "Connection Available is 3G",
//                        Toast.LENGTH_SHORT).show();
                return 2;
            }
            case TelephonyManager.NETWORK_TYPE_LTE: {
//                Toast.makeText(getApplicationContext(), "Connection Available is 4G",
//                        Toast.LENGTH_SHORT).show();
                return 3;
        }

    }
        return 0;
    }
}
