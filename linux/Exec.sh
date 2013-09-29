#! /bin/sh
cd "`dirname \"$0\"`"
cd "./content/"
../jre/bin/java -server -Xmx512M -jar #EXEC_JAR# "${@}"