package com.robotality.jarwrapper.config;

public class WindowsConfig {
	public String outputPath;
	public String jrePath;
	
	public String iconPath;
	
	public String copyright;
	public String companyName;
	
	public String winVersion;
	
	public void defaults() {
		jrePath = "win/jre";
		iconPath = "win|icon.ico";
		outputPath = "output/win/";
		copyright = "(c) 2013 Company";
		companyName = "Company Name";
		winVersion = "0.0.0.1";
	}
}
