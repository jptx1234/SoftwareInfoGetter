package com.alibaba.softwareinfogetter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.softwareinfogetter.utils.WinRegistry;

public class SoftInfoGetter {
	private static final String[] REGISTRYPATHS = {
			"SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall", 
			"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall"};  
	
	

	public static void main(String[] args) {
		
		List<SoftwareEntry> softwareInfo = getSoftwareInfo(new String[]{"QQ"}, SearchMode.LIKE);
		for (SoftwareEntry softwareEntry : softwareInfo) {
			System.out.println(softwareEntry);
		}
		
	}
	
	/**
	 * 
	 * @param name 软件名
	 * @param searchMode 搜索模式
	 * @return 搜索到的软件名称
	 */
	public static List<SoftwareEntry> getSoftwareInfo(String[] names, SearchMode searchMode) {
		List<SoftwareEntry> results = new ArrayList<>();
		if (names == null || names.length == 0) {
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
			if(registryEntry == null) {
				continue;
			}
			for (String registryInfo : registryEntry) {
				String currentPath = path+"\\"+registryInfo;
				String readString = null;
				try {
					readString = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, currentPath, "DisplayName");
				} catch (IllegalAccessException e) {
					System.err.println("无访问权限:"+path+"\\"+registryInfo);
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (readString != null && !"".equals(readString)) {
					for(String name : names) {
						if (readString.equals(name)) {
							SoftwareEntry softwareEntry = generateSoftwareEntry(currentPath);
							results.add(softwareEntry);
						}else if (SearchMode.LIKE.equals(searchMode) && readString.contains(name)) {
							SoftwareEntry softwareEntry = generateSoftwareEntry(currentPath);
							results.add(softwareEntry);
						}
					}
				}
			}
		}
		
		return results;
	}
	
	
	private static SoftwareEntry generateSoftwareEntry(String regPath) {
		SoftwareEntry softwareEntry = null;
		try {
			String name = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, regPath, "DisplayName");
			String version = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, regPath, "DisplayVersion");
			softwareEntry = new SoftwareEntry(name, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return softwareEntry;
	}
	
	enum SearchMode{
		EQUALS,//搜索模式：完全匹配
		LIKE;//搜索模式：相似
	}
	
	

}
