package com.AutomatedRelayControl;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection implements InterfaceConnectivity{
	
	ConnectivityManager connMgr;
	NetworkInfo networkInfo;
	
	public NetworkConnection(ConnectivityManager connMgr, NetworkInfo networkInfo) {
		this.connMgr = connMgr;
		this.networkInfo = networkInfo;
		
	}
	
	public boolean isConnected(){
    	networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) 
    	    	return true;
    	    else
    	    	return false;	
    }
}