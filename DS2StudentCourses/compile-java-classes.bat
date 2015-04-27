@echo off
setlocal
title DS Assignment 2 By SOK Pongsametrey - Compiler

echo Compilation all classes

set OLDDIR=%CD%
echo %OLDDIR%

cd src\main\java

echo Compiling class from %CD%
echo . compiling all common classes
javac -d %OLDDIR%\bin edu\rupp\rmi\common\dao\*.java

echo . compiling all shared classes
javac -d %OLDDIR%\bin edu\rupp\rmi\students\shared\*.java 
javac -d %OLDDIR%\bin edu\rupp\rmi\students\shared\vo\*.java 

echo . compiling all server classes
javac -d %OLDDIR%\bin edu\rupp\rmi\students\server\dao\*.java
javac -d %OLDDIR%\bin edu\rupp\rmi\students\server\*.java

echo . compiling all client classes
javac -d %OLDDIR%\bin edu\rupp\rmi\students\client\*.java

cd %OLDDIR%

cd bin
set SERVER_CLASSES=
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\rmi\common\dao\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\rmi\students\shared\vo\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\rmi\students\shared\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\rmi\students\server\dao\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\rmi\students\server\*.class

set CLIENT_CLASSES=
set CLIENT_CLASSES=%CLIENT_CLASSES% edu\rupp\rmi\students\shared\vo\*.class
set CLIENT_CLASSES=%CLIENT_CLASSES% edu\rupp\rmi\students\shared\*.class
set CLIENT_CLASSES=%CLIENT_CLASSES% edu\rupp\rmi\students\client\*.class

echo . Packaging all classes into RMIStudentServer.jar and RMIStudentClient.jar

echo #SERVER: %SERVER_CLASSES%
jar cfm ..\lib\RMIStudentServer.jar %OLDDIR%\src\main\resources\Manifest-server.txt %SERVER_CLASSES%

echo #SERVER: %CLIENT_CLASSES%
jar cfm ..\lib\RMIStudentClient.jar %OLDDIR%\src\main\resources\Manifest-client.txt %CLIENT_CLASSES%

cd ..

echo .... Compilation done!
Pause
endlocal