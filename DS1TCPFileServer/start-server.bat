@echo off
setlocal
title DS Assignment 1 By SOK Pongsametrey - Server

set classpath=.
set classpath=%classpath%;bin

echo Start FTP File Server

java -jar bin\FTPFileServer.jar

echo Server terminated!
Pause
endlocal