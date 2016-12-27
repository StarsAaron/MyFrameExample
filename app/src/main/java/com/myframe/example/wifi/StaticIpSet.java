package com.myframe.example.wifi;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;

//静态IP设置类
public class StaticIpSet {
	// 设备的环境
	Context mContext;

	String ipAddress = "192.168.1.148";
	int preLength = 24;
	String getWay = "192.168.1.1";
	String dns1 = "192.168.1.1";

	public StaticIpSet(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public StaticIpSet(Context mContext, String ipAddress, int preLength,
			String getWay, String dns1) {
		super();
		this.mContext = mContext;
		this.ipAddress = ipAddress;
		this.preLength = preLength;
		this.getWay = getWay;
		this.dns1 = dns1;
	}

	// 配置wifi的静态ip
	public void confingStaticIp(WifiConfiguration wifiConfig) {
		// 如果是android2.x版本的话
		if (android.os.Build.VERSION.SDK_INT < 11) {
			ContentResolver ctRes = mContext.getContentResolver();
			Settings.System
					.putInt(ctRes, Settings.System.WIFI_USE_STATIC_IP, 1);
			Settings.System.putString(ctRes, Settings.System.WIFI_STATIC_IP,
					ipAddress);
			Settings.System.putString(ctRes,
					Settings.System.WIFI_STATIC_NETMASK, "255.255.255.0");
			Settings.System.putString(ctRes,
					Settings.System.WIFI_STATIC_GATEWAY, getWay);
			Settings.System.putString(ctRes, Settings.System.WIFI_STATIC_DNS1,
					dns1);
			Settings.System.putString(ctRes, Settings.System.WIFI_STATIC_DNS2,
					"61.134.1.9");
		}
		// 如果是android3.x版本及以上的话
		else {
			try {
				setIpType("STATIC", wifiConfig);
				setIpAddress(InetAddress.getByName(ipAddress), preLength,
						wifiConfig);
				setGateway(InetAddress.getByName(getWay), wifiConfig);
				setDNS(InetAddress.getByName(dns1), wifiConfig);
				Log.i("123", "静态ip设置成功！");
				Toast.makeText(mContext, "静态ip设置成功！", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("123", "静态ip设置失败！");
				Toast.makeText(mContext, "静态ip设置失败！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 设置DNS
	 * 
	 * @param dns
	 *            域名
	 * @param wifiConfig
	 *            操作的wifi配置对象
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 */
	private void setDNS(InetAddress dns, WifiConfiguration wifiConfig)
			throws Exception {
		// 获得wifiConfig中linkProperties连接属性集合中的值
		Object linkProperties = getFieldValue(wifiConfig, "linkProperties");
		if (linkProperties == null) {
			return;
		}
		ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(
				linkProperties, "mDnses");
		mDnses.clear(); // 清除原有DNS设置（如果只想增加，不想清除，此句可省略）
		mDnses.add(dns); // 增加新的DNS
	}

	/**
	 * 设置网关
	 * 
	 * @param gateWay
	 *            网关
	 * @param wifiConfig
	 *            操作的wifi配置对象
	 */
	private void setGateway(InetAddress gateWay, WifiConfiguration wifiConfig)
			throws Exception {
		// 获得wifiConfig中linkProperties连接属性集合中的值
		Object linkProperties = getFieldValue(wifiConfig, "linkProperties");
		if (linkProperties == null) {
			return;
		}
		// android4.x版本
		if (android.os.Build.VERSION.SDK_INT >= 14) {

			// 获得了路由信息类
			Class<?> routeInfoClass = Class.forName("android.net.RouteInfo");
			// 获得路由信息类的一个构造器，参数是网关
			Constructor<?> routeInfoConstructor = routeInfoClass
					.getConstructor(new Class[] { InetAddress.class });
			// 生成指定网关的路由信息类对象
			Object routeInfo = routeInfoConstructor.newInstance(gateWay);
			ArrayList<Object> routes = (ArrayList<Object>) getDeclaredField(
					linkProperties, "mRoutes");
			routes.clear();
			routes.add(routeInfo);
		}
		// android3.x版本
		else {
			ArrayList<InetAddress> gateWays = (ArrayList<InetAddress>) getDeclaredField(
					linkProperties, "mGateWays");
			gateWays.clear();
			gateWays.add(gateWay);
		}
	}

	/**
	 * 设置IP地址
	 * 
	 * @param ipAddress
	 *            IP地址
	 * @param preLength
	 *            前缀长度
	 * @param wifiConfig
	 *            操作的wifi配置对象
	 */
	private void setIpAddress(InetAddress ipAddress, int preLength,
			WifiConfiguration wifiConfig) throws Exception {
		// 获得wifiConfig中linkProperties连接属性集合中的值
		Object linkProperties = getFieldValue(wifiConfig, "linkProperties");
		if (linkProperties == null) {
			return;
		}
		// 获得一个LinkAddress链接地址类
		Class<?> linkAddressClass = Class.forName("android.net.LinkAddress");
		// 获得LinkAddress的一个构造器,参数为ip地址和前缀长度
		Constructor<?> linkAddressConstrcutor = linkAddressClass
				.getConstructor(new Class[] { InetAddress.class, int.class });
		// 通过该构造器获得一个LinkAddress对象
		Object linkAddress = linkAddressConstrcutor.newInstance(ipAddress,
				preLength);
		// 获得linkProperties连接属性集合中linkAddresses连接地址集合中的值
		ArrayList<Object> linkAddresses = (ArrayList<Object>) getDeclaredField(
				linkProperties, "mLinkAddresses");
		// 清空linkAddresses
		linkAddresses.clear();
		// 添加用户设置的linkAddress链接地址。
		linkAddresses.add(linkAddress);
	}

	/**
	 * 获得某对象中指定区域中的值。该区域是被声明过的。
	 * 
	 * @param obj
	 *            对象
	 * @param name
	 *            区域名
	 * @return 返回该对象中该区域中的值
	 * @throws Exception
	 */
	private Object getDeclaredField(Object obj, String name) throws Exception {
		// 获obj类中指定区域名为name的区域
		Field f = obj.getClass().getDeclaredField(name);
		// 设置该区域可以被访问
		f.setAccessible(true);
		return f.get(obj);
	}

	/**
	 * 获得某对象中指定区域中的值。根据对象，和区域名。
	 * 
	 * @param obj
	 *            对象
	 * @param name
	 *            区域名
	 * @return 返回该对象中名为name区域中的值
	 * @throws Exception
	 */
	private Object getFieldValue(Object obj, String name) throws Exception {
		// 获得obj的类，再获得区域名为name的区域
		Field f = obj.getClass().getField(name);
		// 返回obj中f区域中的值
		return f.get(obj);
	}

	/**
	 * 设置IP地址类型
	 * 
	 * @param ipType
	 *            IP地址类型
	 * @param wifiConfig
	 *            操作的wifi配置对象
	 * @throws Exception
	 */
	private void setIpType(String ipType, WifiConfiguration wifiConfig)
			throws Exception {
		Field f = wifiConfig.getClass().getField("ipAssignment");
		f.set(wifiConfig, Enum.valueOf((Class<Enum>) f.getType(), ipType));
	}

}
