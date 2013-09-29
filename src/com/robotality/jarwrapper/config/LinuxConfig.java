package com.robotality.jarwrapper.config;

public class LinuxConfig {
	
	public String jrePath32;
	public String jrePath64;
	
	public String outputPath;
	
	public void defaults() {
		jrePath32 = "linux/jre/32";
		jrePath64 = "linux/jre/64";
		
		outputPath = "output/linux/";
	}
	
}
