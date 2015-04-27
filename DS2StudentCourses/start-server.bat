@echo off
setlocal
title DS Assignment 2 By SOK Pongsametrey - Server

REM set classpath=.
REM set classpath=%classpath%;bin
REM set classpath=%classpath%;lib
REM set classpath=%classpath%;lib\sqlite-jdbc-3.7.2.jar

REM ================ USER EDITING AREA =================
set RMI_PORT=1099
REM ================ END USER EDITING AREA =================

echo Start RMI Student Server 127.0.0.1:%RMI_PORT%

java -jar lib\RMIStudentServer.jar %RMI_PORT%

echo Server terminated!
Pause
endlocal