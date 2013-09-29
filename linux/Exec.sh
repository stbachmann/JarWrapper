#! /bin/sh
cd "`dirname \"$0\"`"
./jre/bin/java -server -Xmx512M -jar #EXEC_JAR# "${@}"