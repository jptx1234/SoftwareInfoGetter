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
		return "������ƣ�"+name+(version == null ? "" : "  ����汾��"+version);
	}
	
}
