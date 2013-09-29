package com.robotality.jarwrapper.config;

public class OSXConfig {

	public String jrePath;
	public String resourcePath;
	
	public String outputPath;
	
	public String iconPath;
	public String packageName;
	
	public void defaults() {
		jrePath = "mac/jre";
		iconPath = "mac/icon.icns";
		resourcePath = "mac/resources/";
		outputPath = "output/mac/";
		packageName = "org.app.default";
	}
}
