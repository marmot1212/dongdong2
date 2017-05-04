package com.example.administrator.vegetarians824.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断是否连接上网络的一个工具类
 * @author zxh
 *
 */
public class ConnectionNetUtils {
	
	//获取当前手机到底有没有网
	public static boolean isConnectionNet(Context context){
	ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);	
	//获取网络状态对象
	NetworkInfo netInfo=manager.getActiveNetworkInfo();
	//判断当前网络是否连接(netInfo 有可能为空)如果为空则返回false否则返回网络已连接
	if (netInfo==null) {
		return false;
	}
	
		return netInfo.isConnected();
		
		
	}
}
