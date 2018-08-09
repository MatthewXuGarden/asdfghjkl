package com.carel.supervisor.base.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkInfoList {
	private List<NetworkInfo> list = new ArrayList<NetworkInfo>();
	private static NetworkInfoList me = new NetworkInfoList();

	public NetworkInfoList() {
		getIP();
	}

	public static NetworkInfoList getInstance() {
		return me;
	}

	private void add(String name, String displayName, String ip) {
		NetworkInfo info = new NetworkInfo();
		info.setName(name);
		info.setDisplayName(displayName);
		info.setIp(ip);
		list.add(info);
	}

	public int size() {
		return list.size();
	}

	public NetworkInfo getNetworkInfo(int i) {
		return (NetworkInfo) list.get(i);
	}

	public NetworkInfo getNetworkInfoByName(String name) {
		NetworkInfo info = null;

		for (int i = 0; i < size(); i++) {
			info = getNetworkInfo(i);

			if (info.getName().toUpperCase().startsWith(name.toUpperCase())) {
				return info;
			}
		}
		return null;
	}

	private void getIP() {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
//				System.out.println("DisplayName:" + ni.getDisplayName());
//				System.out.println("Name:" + ni.getName());
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
//					System.out.println("IP:"+ ips.nextElement().getHostAddress());
					add(ni.getName(),ni.getDisplayName(),ips.nextElement().getHostAddress());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		System.out.println(NetworkInfoList.getInstance().getNetworkInfo(1).getIp());
	}
}
