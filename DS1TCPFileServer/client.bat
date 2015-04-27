@echo off
setlocal
title DS Assignment 1 By SOK Pongsametrey - Client

set classpath=.
set classpath=%classpath%;bin

REM ================ USER EDITING AREA =================
set IP_SERVER=127.0.0.1
REM ================ END USER EDITING AREA =================
echo Start FTP File Client %IP_SERVER%

java -jar bin\FTPFileClient.jar %IP_SERVER% %*

echo Client terminated!
Pause
endlocal