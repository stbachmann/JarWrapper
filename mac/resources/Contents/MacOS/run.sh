#!/bin/bash

APP="`dirname \"$0\"`" # run.sh -> MacOS
APP="`dirname \"${APP}\"`" # MacOS -> Contents
APP="`dirname \"${APP}\"`" # Contents -> Xxx.app
cd "`dirname \"${APP}\"`" # cd to Xxx.app parent dir
APP="`basename \"${APP}\"`"

if [[ "`sysctl hw.cpu64bit_capable`" == "hw.cpu64bit_capable: 0" ]]; then
    BUNDLE_JAVA="java"
    syslog -s -l warning JarWrappper: 32-bit OS detected, using system Java.
else
    BUNDLE_JAVA="./jre/bin/java"
fi

cd "${APP}/Contents/MacOS/"

"${BUNDLE_JAVA}" \
    -server \
    -Xdock:name="#APP_NAME#" \
    -Xdock:icon="../Resources/icon.icns" \
    -Dapple.laf.useScreenMenuBar=true \
    -Dcom.apple.macos.useScreenMenuBar=true \
    -Dapple.awt.showGrowBox=false \
    -Dfile.encoding=UTF-8 \
    ${EXTRAARGS} \
    -Xmx512m \
    -jar "#EXEC_JAR#" \
    "$@" \
    2>&1

exit 0