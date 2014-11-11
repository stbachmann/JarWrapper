##JarWrapper

JarWrapper is a little utility to bundle Java jars with their own JRE, making them executable natively. The tool currently bundles executables for Mac OS X, but Windows and Linux support is planned!

###Quick Tutorial
Download the latest JarWrapper from here and unzip to a directory on your harddrive: [http://robotality.com/jarwrapper/download/JarWrapper-latest.zip](http://robotality.com/jarwrapper/download/JarWrapper-latest.zip)

The folder structure should look something like this:
```
JarWrapper/
|-- config/
|   `-- config.json
|-- linux/
|   `-- Exec.sh
|-- mac/
|   |-- resources/
|   |   `-- ...
|   `-- icon.icns
|-- win/
|   |-- icon.ico
|   `-- jarwrapper.launch4j.xml
`-- JarWrapper.jar
```

####Adding the JREs 
In order to use the JarWrapper you need to supply the JREs for each platform to the tool. Unfortunately I can't distribute them with the tool.

You can get the JREs from here: https://github.com/alexkasko/openjdk-unofficial-builds

For Windows you'll want the 32-bit. Mac has 32 and 64-bit combined. For Linux you'll want both a 32 and a 64-bit JRE. You can usually strip the JRE down quite a bit by removing files not needed. I'm hoping to create a little utility soon that takes away some of the pain of doing this manually.

Put the JREs into the OS folders. So each OS folder should have a JRE in it at the end. (`mac/jre/`, `linux/jre/32/`, `linux/jre/64/`, `win/jre/`)

####Adding launch4j
Next step is to download launch4j (choose the correct OS from which you are running the wrapper from): [http://sourceforge.net/projects/launch4j/files/launch4j-3/3.1.0-beta2/](http://sourceforge.net/projects/launch4j/files/launch4j-3/3.1.0-beta2/)

Extract launch4j to the `win/launch4j/` folder. 

####Customising the config.json file
The `config/config.json` file is where you customise your specific applications details. The file should look something like this:
```
{
    "executableArgument": "executable.jar",
    "appName": "Default App",
    "executableJarPath": "jar|executable.jar",
    "zipOutput": true,
    "osxConfig": {
        "jrePath": "mac/jre",
    	"outputPath": "output/mac/",
    	"macVersion": "0.01",
    	"packageName": "org.app.default",
    	"resourcePath": "mac/resources/",
    	"iconPath": "mac|icon.icns"
    },
    
    "linuxConfig": {
    	"jrePath64": "linux/jre/64",
    	"jrePath32": "linux/jre/32",
    	"outputPath": "output/linux/"
    },
    "winConfig": {
    	"jrePath": "win/jre",
    	"outputPath": "output/win/",
    	"copyright": "(c) 2013 Company",
    	"companyName": "Company Name",
    	"winVersion": "0.0.0.1",
    	"iconPath": "win|icon.ico"
    },
    "additionalResources": [
    	"resources/content/"
    ]
}
```
This is a standard json file with `key:value` pairs. Most of the keys should explain themselves. Customise this file to match your application and you'll be good to go. For completeness sake, quick explenations for the individual keys:

*ADD KEY:VALUES TABLE*

####Bundling your jars
After you've customised your `config.json` we are ready to bundle the jar. From your cmd-line terminal, switch to the JarWrappers folder and run:

`java -jar JarWrapper.jar` 

The wrapper should do the rest. You'll find your bundled executables in the output folder split up by OS. If you picked the zip options you'll also find the zipped files next to it.
