package com.robotality.jarwrapper.build;

import static com.esotericsoftware.scar.Build.build;
import static com.esotericsoftware.scar.Build.oneJAR;
import static com.esotericsoftware.scar.Scar.paths;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.scar.Build;
import com.esotericsoftware.scar.Project;
import com.esotericsoftware.scar.Scar;
import com.esotericsoftware.wildcard.Paths;
import com.robotality.jarwrapper.JarWrapper;

public class JarWrapperBuild {
	public static void main(String[] args) throws IOException{
		Project project = Build.project(".");
		
		project.set("resources", null);
		project.set("target", "./target");
		project.set("main", "com.robotality.jarwrapper.JarWrapper");
        //project.set("classpath", "/libs|scar-1.09.jar|jsonbeans-0.5.jar");
        
		build(project);
		oneJAR(project);
		
		String onejar = project.path("$target$/onejar");
		
		// Output ane executable jar to the output folder
		String outputPath = project.path("$target$/../dist/" + JarWrapper.version);
		paths(outputPath).delete();
		
		String jar = outputPath + "/JarWrapper-"+JarWrapper.version+".jar";
		Scar.jar(jar, paths(onejar), "com.robotality.jarwrapper.JarWrapper", new Paths());
		
		paths(".", wrapperResources.toArray(new String[0])).copyTo(outputPath);
		
		new File(outputPath + "/mac/jre").mkdir();
		new File(outputPath + "/linux/jre/32").mkdirs();
		new File(outputPath + "/linux/jre/64").mkdirs();
		new File(outputPath + "/win/jre/").mkdir();
		new File(outputPath + "/win/launch4j/").mkdir();
		
		Scar.zip(paths(outputPath), outputPath + "/JarWrapper-"+JarWrapper.version+".zip");
	}
	
	static private ArrayList<String> wrapperResources = new ArrayList<String>();
    static {
    	wrapperResources.add("config/**");
    	wrapperResources.add("mac/icon.icns");
    	wrapperResources.add("mac/resources/**");
    	wrapperResources.add("linux/Exec.sh");
    	wrapperResources.add("win/icon.ico");
    	wrapperResources.add("win/jarwrapper.launch4j.xml");
    }
}
