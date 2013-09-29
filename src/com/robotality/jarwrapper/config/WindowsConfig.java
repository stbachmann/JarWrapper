package com.robotality.jarwrapper.config;

public class WindowsConfig {
	public String outputPath;
	public String jrePath;
	
	public String iconPath;
	
	public void defaults() {
		jrePath = "win/jre";
		iconPath = "win/icon.ico";
		outputPath = "output/win/";
	}
}
