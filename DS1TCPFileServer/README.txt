Welcome to MTR FTP Server/Client Program
By Mr. SOK Pongsametrey, RUPP, MITE, 2011
Contact: metreysk@gmail.com / http://osify.com

Requires at least JDK 1.5

Available FTP commands:
. get <filename> : Get a file from server
. put <filepath> : Put a file from local PC to server
. list : List all files in server
. help : Information to help client user

Remark: Limitation on filename/filepath, no space be able to provide at the moment

To use the application, please following as following steps:
1. Compile source code with batch file: compile-java-classes.bat
2. Start server and follow the instruction by execute batch file: start-server.bat
3. Start client access: client.bat
    By default, the batch client.bat configured to connect to server on IP: 127.0.0.1
    So if server hosted in another IP, please configure the IP in the batch file at line:
    set IP_SERVER=127.0.0.1

    You can start client as many as you want.