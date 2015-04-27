@echo off
setlocal
title DS Assignment 3 By SOK Pongsametrey, THOU Bunhann, THY Poty, Cheap Eng - Server

REM ================ USER EDITING AREA =================
set RMI_PORT=1099
set INPUT_DIR="C:\tmp\input"
set BACKUP_DIR="C:\tmp\backup"
REM ================ END USER EDITING AREA =============

echo Start RMI Search Engine Server 127.0.0.1:%RMI_PORT%

java -jar lib\DS3SEWordFinderServer.jar %RMI_PORT% %INPUT_DIR% %BACKUP_DIR%

echo Search Engine Server terminated!
Pause
endlocal