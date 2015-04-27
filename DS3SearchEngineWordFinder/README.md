Welcome to MTR Search Engine Word Finder Server/Client Program
By Mr. SOK Pongsametrey, Thou Bunhann, Thy Poty, Cheap Eng RUPP, MITE, 2011
Contact: metreysk@gmail.com / http://osify.com

Requires at least JDK 1.6
Packaging: Apache ANT 1.6 or latest

# Build
. To compile / packaging use: Apache ANT 1.6 up
  ant release
  
. Go to dist folder, see bat file for configuration  

Ant Build:

  ant all

# Setup for testing
## Server(s)
Server database (All servers point to this folder): 
  C:\tmp\input
  C:\tmp\backup
  
See: start-server.bat

REM ================ USER EDITING AREA =================
set RMI_PORT=1099
set INPUT_DIR="C:\tmp\input"
set BACKUP_DIR="C:\tmp\backup"
REM ================ END USER EDITING AREA =============  
  
## Client(s) 

See: client.bat
REM ================ USER EDITING AREA =================
set RMI_HOST=127.0.0.1;172.16.15.40
set RMI_PORT=1099
REM ================ END USER EDITING AREA =================
