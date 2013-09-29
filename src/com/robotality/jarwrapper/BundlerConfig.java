package com.robotality.jarwrapper;

import java.util.ArrayList;

import com.robotality.jarwrapper.config.OSXConfig;

public class BundlerConfig {
	public String appName;
	public String version;
	public String executableJarPath;
	public String executableArgument;
	
	public OSXConfig osxConfig;
	
	public ArrayList<String> additionalResources;
	
	public void defaults(){
		appName = "Default App";
		version = "0.01";
		executableJarPath = "jar/executable.jar";
		executableArgument = "executable.jar";
		
		osxConfig = new OSXConfig();
		osxConfig.defaults();
		
		additionalResources = new ArrayList<String>();
		additionalResources.add("resources/content/");
	}
}
