@echo off
setlocal
title DS Assignment 3 By SOK Pongsametrey - Compiler

echo Compilation all classes


set OLDDIR=%CD%
echo %OLDDIR%

set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%OLDDIR%
set CLASSPATH=%CLASSPATH%;%OLDDIR%\bin
set CLASSPATH=%CLASSPATH%;%CLASSPATH%
export %CLASSPATH%
cd src\main\java

echo Compiling class from %CD%
echo . compiling all common classes
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\common\*.java
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\common\util\FileUtils.java
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\common\util\ZipHelper.java
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\common\dao\*.java

echo . compiling all shared classes
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\shared\*.java 
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\shared\vo\*.java 

echo . compiling all server classes
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\server\dao\*.java
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\server\*.java

echo . compiling all client classes
javac  -classpath %CLASSPATH% -d %OLDDIR%\bin edu\rupp\search\words\client\*.java

cd %OLDDIR%

cd bin
set SERVER_CLASSES=
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\common\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\common\util\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\common\dao\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\shared\vo\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\shared\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\server\dao\*.class
set SERVER_CLASSES=%SERVER_CLASSES% edu\rupp\search\words\server\*.class

set CLIENT_CLASSES=
set CLIENT_CLASSES=%CLIENT_CLASSES% edu\rupp\search\words\shared\vo\*.class
set CLIENT_CLASSES=%CLIENT_CLASSES% edu\rupp\search\words\shared\*.class
set CLIENT_CLASSES=%CLIENT_CLASSES% edu\rupp\search\words\client\*.class

echo . Packaging all classes into DSEWordFinderServer.jar and DSEWordFinderClient.jar

echo #SERVER: %SERVER_CLASSES%
jar cfm ..\lib\DSEWordFinderServer.jar %OLDDIR%\src\main\resources\Manifest-server.txt %SERVER_CLASSES%

echo #SERVER: %CLIENT_CLASSES%
jar cfm ..\lib\DSEWordFinderClient.jar %OLDDIR%\src\main\resources\Manifest-client.txt %CLIENT_CLASSES%

cd ..

echo .... Compilation done!
Pause
endlocal