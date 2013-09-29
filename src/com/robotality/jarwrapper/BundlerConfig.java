package com.robotality.jarwrapper;

import java.util.ArrayList;

import com.robotality.jarwrapper.config.OSXConfig;
import com.robotality.jarwrapper.config.WindowsConfig;

public class BundlerConfig {
	public String appName;
	public String executableJarPath;
	public String executableArgument;
	
	public OSXConfig osxConfig;
	public WindowsConfig winConfig;
	
	public ArrayList<String> additionalResources;
	
	public void defaults(){
		appName = "Default App";
		executableJarPath = "jar|executable.jar";
		executableArgument = "executable.jar";
		
		osxConfig = new OSXConfig();
		osxConfig.defaults();
		
		winConfig = new WindowsConfig();
		winConfig.defaults();
		
		additionalResources = new ArrayList<String>();
		additionalResources.add("resources/content/");
	}
}
