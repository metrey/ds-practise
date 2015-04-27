@echo off
setlocal
title DS Assignment 2 By SOK Pongsametrey - Client

REM set classpath=.
REM set classpath=%classpath%;bin
REM set classpath=%classpath%;lib

REM ================ USER EDITING AREA =================
set RMI_HOST=127.0.0.1
set RMI_PORT=1099
REM ================ END USER EDITING AREA =================
echo Start RMI Student Client %RMI_HOST%:%RMI_PORT%

java -jar lib\RMIStudentClient.jar %RMI_HOST% %RMI_PORT% %*

echo Client terminated!
Pause
endlocal