package com.alibaba.softwareinfogetter.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SoftInfoGetter {
	private static final String[] REGISTRYPATHS = {
			"SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall", 
			"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall"};  
	
	

	public static void main(String[] args) {
		
		List<String> softwareInfo = getSoftwareInfo("腾讯QQ", SearchMode.EQUALS);
		System.out.println(softwareInfo);
		
	}
	
	
	public static List<String> getSoftwareInfo(String name, SearchMode searchMode) {
		List<String> results = new ArrayList<>();
		if (name == null || "".equals(name)) {
			return results;
		}
		for (String path : REGISTRYPATHS) {
			List<String> registryEntry = null;
			try {
				registryEntry = WinRegistry.readStringSubKeys(WinRegistry.HKEY_LOCAL_MACHINE, path);
			} catch (IllegalAccessException e1) {
				System.err.println("无访问权限:"+path);
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(registryEntry != null) {
				for (String registryInfo : registryEntry) {
					if(registryInfo.equals(name)) {
						results.add(registryInfo);
					}else if (SearchMode.LIKE.equals(searchMode) && registryInfo.contains(name)) {
						results.add(registryInfo);
					}
					String readString = null;
					try {
						readString = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, path+"\\"+registryInfo, "DisplayName");
					} catch (IllegalAccessException e) {
						System.err.println("无访问权限:"+path+"\\"+registryInfo);
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (readString != null && !"".equals(readString)) {
						if (readString.equals(name)) {
							results.add(readString);
						}else if (SearchMode.LIKE.equals(searchMode) && readString.contains(name)) {
							results.add(readString);
						}
					}
				}
				
			}
		}
		
		return results;
		
	}
	
	enum SearchMode{
		EQUALS,
		LIKE;
	}

}
