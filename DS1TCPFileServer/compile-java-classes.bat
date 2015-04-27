@echo off
setlocal
title DS Assignment 1 By SOK Pongsametrey - Compiler

echo Compilation all classes

set OLDDIR=%CD%
echo %OLDDIR%

cd src\main\java

echo Compiling class from %CD%

javac -d %OLDDIR%\bin edu\rupp\filesystem\common\*.java

javac -d %OLDDIR%\bin edu\rupp\filesystem\server\FTPFileServer.java

javac -d %OLDDIR%\bin edu\rupp\filesystem\client\FTPFileClient.java

cd %OLDDIR%

cd bin

jar cfm FTPFileServer.jar %OLDDIR%\src\main\resources\Manifest-server.txt edu\rupp\filesystem\common\*.class edu\rupp\filesystem\server\*.class 
jar cfm FTPFileClient.jar %OLDDIR%\src\main\resources\Manifest-client.txt edu\rupp\filesystem\common\*.class edu\rupp\filesystem\client\*.class 

cd ..

echo Compilation done!
Pause
endlocal