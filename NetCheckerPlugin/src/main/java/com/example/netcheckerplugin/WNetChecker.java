package com.example.netcheckerplugin;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class WNetChecker {

    private static WNetChecker _instance;
    private static Context _context;

    MyPhoneStateListener MyListener;

    TelephonyManager mTelephonyManager;
    ConnectivityManager cm;
    NetworkInfo nInfo;
    int netStrength;
    List<CellInfo> all;

    WifiManager wifiManager ;
    int numberOfLevels = 5;
    WifiInfo wifiInfo;
    int level ;

    public WNetChecker(Context context) {
        _context=context;
        cm = (ConnectivityManager) _context.getSystemService(_context.CONNECTIVITY_SERVICE);
        nInfo = cm.getActiveNetworkInfo();
        mTelephonyManager = (TelephonyManager)
                _context.getSystemService(Context.TELEPHONY_SERVICE);
        MyListener = new MyPhoneStateListener();
        mTelephonyManager.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
         wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);

    }

    public static WNetChecker Instance(Context context) {
        if (_instance == null) {


            _instance = new WNetChecker(context);

        }
        return _instance;
    }
    @SuppressLint("MissingPermission")
    public int GetNetWorkState() {

        int netWorkType;
        try{
            netWorkType = mTelephonyManager.getNetworkType();

        }catch (Exception e){
          netWorkType=1;
        //    Log.e("Error:",e.toString());
        }

      //  Log.e("NetworkType", " "+netWorkType);
        if(wifiManager.getWifiState()!=WifiManager.WIFI_STATE_DISABLED){
            return 5;
        }
        if(cm.getActiveNetworkInfo()==null){
            return  0;
        }


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

            case TelephonyManager.NETWORK_TYPE_NR:
                return 4;

    }
        return 0;
    }

    public int getRSSI() {

         //mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

       // String strength = MyListener.getStrength();
        if(wifiManager==null){
            return 0;
        }

        if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED) {


        wifiInfo = wifiManager.getConnectionInfo();
        level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
         return  level;
        }else if(cm.getActiveNetworkInfo()!=null){
           // TelephonyManager telephonyManager =        (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

            return  MyListener.getStrength();
        }else{
            return  0;
        }



    }

    class MyPhoneStateListener extends PhoneStateListener {

        public int singalStrengths;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int asu = signalStrength.getGsmSignalStrength();
          //  Log.e("PhonListner","check"+asu);
            singalStrengths = -113 + 2 * asu;
        }

        public int getStrength() {
            return singalStrengths;
        }
    }

}
