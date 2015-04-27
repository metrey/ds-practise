Welcome to MTR FTP Server/Client Program
By Mr. SOK Pongsametrey, RUPP, MITE, 2011
Contact: metreysk@gmail.com / http://osify.com

Requires at least JDK 1.5

Available commands: 1: Save, 2: Update, 3: Delete, 4: FindAll, 5: FindById, 6: FindByName, 7: FindByCourse, 0: Quit
Student management

To use the application, please following as following steps:
1. Compile source code with batch file: compile-java-classes.bat
2. Start server and follow the instruction by execute batch file: start-server.bat
3. Start client access: client.bat
    By default, the batch client.bat configured to connect to server on IP: 127.0.0.1, PORT 1099
    So if server want to run on different RMI port, please configure the  RMI_PORT in the batch file at line:
    set  RMI_PORT=1099

    You can start client as many as you want.