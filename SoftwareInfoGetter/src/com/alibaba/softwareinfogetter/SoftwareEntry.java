package com.alibaba.softwareinfogetter;

public class SoftwareEntry {
	public String name;
	public String version;
	
	public SoftwareEntry(String name, String version) {
		this.name = name;
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "软件名称："+name+(version == null ? "" : "  软件版本："+version);
	}
	
}
