package com.robotality.jarwrapper;

import static com.esotericsoftware.scar.Scar.paths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;

public class JarWrapper {
	public static String version = "0.1";
	
	public static Arguments arguments;
	public static BundlerConfig config;
	
	public static boolean isWindows = false;
	public static boolean isMacOSX = false;
	public static boolean isLinux = false;
	
	public static void main(String[] args) throws IOException{
		arguments = new Arguments(args);
		Json json = new Json();
		json.setOutputType(OutputType.json);
		
		determineOS();
		
		System.out.println("Jar Wrapper Version " + version);
		System.out.println("*********");
		
		/*
		BundlerConfig config = new BundlerConfig();
		config.defaults();
		String configFile = json.prettyPrint(config);
		saveConfigFile(configFile);
		*/
	
		System.out.print("Loading config file ... ");
		
		File configFile;
		
		if(arguments.has("-config")){
			configFile = new File(arguments.get("-config"));
		}
		else {
			configFile = new File("config/config.json");
		}
		
		if(!configFile.exists()){
			System.out.println("Can't find the supplied config file, or the default file.");
			System.exit(0);
		}
		else {
			String configContent = readFile(configFile);
			config = json.fromJson(BundlerConfig.class, configContent);
		}
		
		if(config == null){
			System.out.println("Something went wrong while loading the config file. Abort.");
			System.exit(0);
		}
		else
			System.out.println("Success.");
		
		if(config.osxConfig != null){
			System.out.print("Wrapping OSX executable ... ");
			wrapOSX();
			System.out.println("Success.");
		}
		if(config.winConfig != null){
			System.out.println("Wrapping Windows executable ... ");
			wrapWin();
			System.out.println("Windows ... Success.");
		}
		
		System.out.println("Finished.");
	}

	public static void wrapOSX() throws IOException {
		String outputPath = config.osxConfig.outputPath + config.appName + ".app";
		File outputFolder = new File(outputPath);
		if(outputFolder.exists())
			paths(outputPath).delete();
		
		if(!new File(outputPath).mkdirs()){
			if(!outputFolder.exists()){
				System.out.println("Could not create output folders. Abort.");
				System.exit(0);
			}
		}
		
		paths(config.osxConfig.resourcePath, "**").copyTo(outputPath);
		paths(config.osxConfig.iconPath).copyTo(outputPath + "/Contents/Resources");
		
		paths(config.osxConfig.jrePath, "**").copyTo(outputPath + "/Contents/MacOS/jre");
		paths(config.executableJarPath).copyTo(outputPath + "/Contents/MacOS");
		
		// pList replacements
		File pListFile = new File(outputPath + "/Contents/Info.plist");
		String pListFileString = readFile(pListFile);
		pListFileString = pListFileString.replace("#APP_NAME#", config.appName);
		pListFileString = pListFileString.replace("#VERSION#", config.osxConfig.macVersion);
		
		writeToFile(pListFileString, pListFile);
		
		// run.sh replacements
		File runShFile = new File(outputPath + "/Contents/MacOS/run.sh");
		String runShFileString = readFile(runShFile);
		runShFileString = runShFileString.replace("#EXEC_JAR#", "./" + config.executableArgument);
		runShFileString = runShFileString.replace("#APP_NAME#", config.appName);

		writeToFile(runShFileString, runShFile);
		
		// copy additional resources
		for(int i=0; i<config.additionalResources.size(); i++){
			paths(config.additionalResources.get(i), "**").copyTo(outputPath + "/Contents/MacOS/");
		}
		
		// Fix executable stuff (grumble)
		new File(outputPath + "/Contents/MacOS/jre/bin/java").setExecutable(true);
		new File(outputPath + "/Contents/MacOS/run.sh").setExecutable(true);
		new File(outputPath + "/Contents/MacOS/jre/ASSEMBLY_EXCEPTION").setExecutable(true);
		new File(outputPath + "/Contents/MacOS/jre/LICENSE").setExecutable(true);
		new File(outputPath + "/Contents/MacOS/jre/THIRD_PARTY_README").setExecutable(true);
	}

	private static void wrapWin() throws IOException {
		String outputPath = config.winConfig.outputPath + config.appName;
		File outputFolder = new File(outputPath);
		if(outputFolder.exists())
			paths(outputPath).delete();
		
		if(!new File(outputPath).mkdirs()){
			if(!outputFolder.exists()){
				System.out.println("Could not create output folders. Abort.");
				System.exit(0);
			}
		}
		
		paths("win|jarwrapper.launch4j.xml").copyTo(outputPath);
		paths(config.executableJarPath).copyTo(outputPath + "/content");
		paths(config.winConfig.jrePath, "**").copyTo(outputPath + "/jre");
		paths(config.winConfig.iconPath).copyTo(outputPath + "/temp");
		
		// Update the launch4j config for this exe
		File launch4jconfig = new File(outputPath + "/jarwrapper.launch4j.xml");
		String launch4jString = readFile(launch4jconfig);
		launch4jString = launch4jString.replace("#EXEC_JAR#", "content/" + config.executableArgument);
		launch4jString = launch4jString.replace("#OUTPUT_PATH#", outputFolder.getAbsolutePath() + "/" + config.appName + ".exe");
		launch4jString = launch4jString.replace("#VERSION#", config.winConfig.winVersion);
		launch4jString = launch4jString.replace("#APP_NAME#", config.appName);
		launch4jString = launch4jString.replace("#COPYRIGHT#", config.winConfig.copyright);
		launch4jString = launch4jString.replace("#ICON#", "temp/"+config.winConfig.iconPath.substring(config.winConfig.iconPath.indexOf("|")+1));
		launch4jString = launch4jString.replace("#COMPANY_NAME#", config.winConfig.companyName);
		launch4jString = launch4jString.replace("#APP_EXE#", config.appName + ".exe");
		writeToFile(launch4jString, launch4jconfig);
		
		// Each OS will need it's own execution of launch4j
		if(isMacOSX){
			String configPath = "./"  + outputPath + "/jarwrapper.launch4j.xml";
			Runtime.getRuntime().exec("win/launch4j/launch4j " + configPath);
			
			String[] commands = {"./win/launch4j/launch4j", configPath};

			Process proc = Runtime.getRuntime().exec(commands);
			
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			String s;
	        // read any errors from the attempted command
	        while ((s = stdError.readLine()) != null) {
	            System.out.println(s);
	        }
		}
		
		// clean-up
		paths(outputPath, "jarwrapper.launch4j.xml").delete();
		paths(outputPath + "/temp").delete();
		new File(outputPath + "/temp").delete();
		
		// copy additional resources
		for(int i=0; i<config.additionalResources.size(); i++){
			paths(config.additionalResources.get(i), "**").copyTo(outputPath + "/content");
		}
	}

	private static void saveConfigFile(String configFile) throws IOException {
		File file = new File("config/config.json");
		
		writeToFile(configFile, file);
	}
	
	private static String readFile(File file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");

	    while(( line = reader.readLine() ) != null) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
	private static void writeToFile(String string, File file) throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		
		output.write(string);
		
		output.close();
	}

	private static void determineOS() {
		String os = System.getProperty("os.name");
		
		if(os.startsWith("Windows"))
			isWindows = true;
		else if(os.startsWith("Mac"))
			isMacOSX = true;
		else if(os.startsWith("Linux"))
			isLinux = true;
	}
}
