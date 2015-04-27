@echo off
setlocal
title DS Assignment 3 By SOK Pongsametrey, THOU Bunhann, THY Poty, Cheap Eng - Client

REM set classpath=.
REM set classpath=%classpath%;bin
REM set classpath=%classpath%;lib

REM ================ USER EDITING AREA =================
set RMI_HOST=127.0.0.1;172.16.15.40
set RMI_PORT=1099
REM ================ END USER EDITING AREA =================
echo Start RMI Search Engine %RMI_HOST%:%RMI_PORT%

java -jar lib\DS3SEWordFinderClient.jar %RMI_HOST% %RMI_PORT% %*

echo Client terminated!
Pause
endlocal