@echo off

:: *******************************************************
::    SCRIPT TO START JUNIT
::    Chris Collier 11/1/2006
:: *******************************************************

SETLOCAL

:: *******************************************************
::    VARIABLES THAT MAY CHANGE PER DEVELOPER MACHINE
:: *******************************************************

SET CC_JAVA_HOME=C:\java\j2sdk1.4.2_05
SET CC2_HOME=C:\projects\ccc\apps\CC2
SET COMMON_LIB_HOME=C:\projects\ccc\3rdpty\common-lib

SET NET_SHARED_HOME=N:\builds\shared\latest_trunk\bin\DEBUG
SET LOCAL_SHARED_HOME=C:\projects\ccc\apps\shared

:: *******************************************************
::    GLOBAL VARIABLES FOR THIS SCRIPT
:: *******************************************************

SET ARGS=%*
SET USE_SYSTEM_JAVAHOME=FALSE
SET USE_LOCAL_SHARED=FALSE
SET USE_SWINGUI=FALSE
SET RUNNER=junit.textui.TestRunner

SET TCNEXT=FALSE

:: *******************************************************
::    START OF MAIN SCRIPT LOGIC
:: *******************************************************

IF NOT EXIST %CC_JAVA_HOME% (
    SET JH=%JAVA_HOME%
    SET USE_SYSTEM_JAVAHOME=TRUE
) ELSE (
    SET JH=%CC_JAVA_HOME%
)

IF NOT EXIST %JH% (
    ECHO.
    ECHO ********** ERROR: No Java JDK could be found. Please set the CC_JAVA_HOME variable at the top of this file. 
    ECHO.
    GOTO :EOF
)

IF %USE_SYSTEM_JAVAHOME%==TRUE (
    ECHO.
    ECHO ********** WARNING: Project-specific JAVA_HOME path not set: using system JAVA_HOME: %JAVA_HOME%
    ECHO.    
)

call :getopt %*


IF %USE_SWINGUI%==TRUE (
    set RUNNER=junit.swingui.TestRunner
) ELSE (
    IF NOT DEFINED TEST_CLASS GOTO :usage
)
echo %RUNNER%

:: *** INITIALIZE COMMAND STRING VARIABLES ***
SET CMD=%JH%\bin\java.exe


:: *** SET UP CLASSPATH ***
SET CLASSPATH=%CC2_HOME%\test\junit\scripts
SET CLASSPATH=%CLASSPATH%;%CC2_HOME%\classes
SET CLASSPATH=%CLASSPATH%;%CC2_HOME%\config

CALL :addpath %COMMON_LIB_HOME%\pool\ext2\*.jar
CALL :addpath %COMMON_LIB_HOME%\container\ext2\*.jar
CALL :addpath %COMMON_LIB_HOME%\testing\*.jar

:: *** SET UP LOCATION FOR CCC SHARED LIBRARIES ***
IF NOT .%USE_LOCAL_SHARED% == .TRUE goto networkshared
set CLASSPATH=%CLASSPATH%;%LOCAL_SHARED_HOME%\src\public\classes
set CLASSPATH=%CLASSPATH%;%LOCAL_SHARED_HOME%\shared\src\data\classes
set CLASSPATH=%CLASSPATH%;%LOCAL_SHARED_HOME%\shared\src\base\classes
set CLASSPATH=%CLASSPATH%;%LOCAL_SHARED_HOME%\shared\src\internal\classes
goto endshared

:networkshared
CALL :addpath %NET_SHARED_HOME%\*.jar
:endshared

set CONFIG_DIR=%CC2_HOME%\config

echo %CLASSPATH%


:: *******************************************************
::    JDBC CONNECTION SETUP
:: *******************************************************

set TEST_MODE=junit

rem ***** For certain variables, allow existing environment variables to override
IF "%TEST_DB_URL%"=="" set TEST_DB_URL=jdbc:oracle:thin:@dev2db1:1531:CCCD2
IF "%TEST_DB_USERNAME%"=="" set TEST_DB_USERNAME=CC_USER
IF "%TEST_DB_PASSWORD%"=="" set TEST_DB_PASSWORD=CC_USER
IF "%TEST_SCHEMA_OWNER%"=="" set TEST_SCHEMA_OWNER=CCINTEG

IF "%SHARED_REG_DB_URL%"=="" set SHARED_REG_DB_URL=jdbc:oracle:thin:@dev2db1:1530:OAD2
IF "%SHARED_REG_DB_USERNAME%"=="" set SHARED_REG_DB_USERNAME=CUST
IF "%SHARED_REG_DB_PASSWORD%"=="" set SHARED_REG_DB_PASSWORD=CUST

IF "%TEST_REG_PKG_URL%"=="" set TEST_REG_PKG_URL=jdbc:oracle:thin:@dev2db1:1530:OAD2
IF "%TEST_REG_PKG_USERNAME%"=="" set TEST_REG_PKG_USERNAME=CUST
IF "%TEST_REG_PKG_PASSWORD%"=="" set TEST_REG_PKG_PASSWORD=CUST
IF "%TEST_DATA_DIR%"=="" set TEST_DATA_DIR=..\data



:: *******************************************************
::    BUILD COMMAND LINE STRING
:: *******************************************************

rem ***** Note that we always start in debug mode on port 4000.
set CMD=%CMD% -ojvm -XXdebug,port4000,detached,quiet


rem set CMD=%CMD% -classpath %CLASSPATH%
set CMD=%CMD% -Dconfig.dir=%CONFIG_DIR%

set CMD=%CMD% -Dcc2.test.mode=%TEST_MODE%
set CMD=%CMD% -Dcc2.test.db.url=%TEST_DB_URL%
set CMD=%CMD% -Dcc2.test.db.username=%TEST_DB_USERNAME%
set CMD=%CMD% -Dcc2.test.db.password=%TEST_DB_PASSWORD%
set CMD=%CMD% -Dcc2.test.reg.url=%TEST_REG_PKG_URL%
set CMD=%CMD% -Dcc2.test.reg.username=%TEST_REG_PKG_USERNAME%
set CMD=%CMD% -Dcc2.test.reg.password=%TEST_REG_PKG_PASSWORD%
set CMD=%CMD% -Dcc2.test.data=%TEST_DATA_DIR%
set CMD=%CMD% -Dcc2.schema.owner=%TEST_SCHEMA_OWNER%

set CMD=%CMD% -Dshared.test.mode=%TEST_MODE%
set CMD=%CMD% -Dshared.test.reg.url=%SHARED_REG_DB_URL%
set CMD=%CMD% -Dshared.test.reg.username=%SHARED_REG_DB_USERNAME%
set CMD=%CMD% -Dshared.test.reg.password=%SHARED_REG_DB_PASSWORD%

set CMD=%CMD% %RUNNER%
set CMD=%CMD% %TEST_CLASS%

echo %CMD%

echo Starting JUnit.  Remote debugging enabled on port 4000...
echo TEST_DB_URL=%TEST_DB_URL%
echo TEST_DB_USERNAME=%TEST_DB_USERNAME%
echo TEST_DB_PASSWORD=%TEST_DB_PASSWORD%
echo TEST_SCHEMA_OWNER=%TEST_SCHEMA_OWNER%
echo USE_LOCAL_SHARED=%USE_LOCAL_SHARED%

%CMD%

rem Echo configuration when closing JUnit dialog since log messages often overrun the window.
echo TEST_DB_URL=%TEST_DB_URL%
echo TEST_DB_USERNAME=%TEST_DB_USERNAME%
echo TEST_DB_PASSWORD=%TEST_DB_PASSWORD%
echo TEST_SCHEMA_OWNER=%TEST_SCHEMA_OWNER%
echo USE_LOCAL_SHARED=%USE_LOCAL_SHARED%

GOTO END

:: *******************************************************
::    END MAIN LOGIC
:: *******************************************************


:: *******************************************************
::    "FUNCTION" DEFINITIONS
:: *******************************************************

:getopt
FOR %%A IN (%*) DO CALL :makeopt %%A
GOTO :EOF

:makeopt
IF %TCNEXT%==TRUE (
    SET TEST_CLASS=%1
    SET TCNEXT=FALSE
)
IF %1==-swingui SET USE_SWINGUI=TRUE
IF %1==-localshared SET USE_LOCAL_SHARED=TRUE
IF %1==-testclass SET TCNEXT=TRUE
GOTO :EOF

:addpath
FOR %%F IN (%1) DO CALL :pathcat %%F
GOTO :EOF

:pathcat
SET CLASSPATH=%CLASSPATH%;%1
GOTO :EOF

:usage
IF NOT DEFINED TEST_CLASS (
    ECHO ********** ERROR: You must supply the name of a class to run if running the text UI
    ECHO.
)
ECHO Usage:
ECHO startJUnit.bat [-localshared] [-swingui] -testclass TestClassName
ECHO.
ECHO Where:
ECHO   -localshared runs against local shared services libraries instead of latest_trunk available on the network
ECHO   -swingui invokes the Swing TestRunner UI (otherwise text UI is used)
ECHO.  TestClassName is the test class to run if invoking textui
ECHO.

GOTO END

:: *******************************************************
::    END FUNCTION DEFINITIONS
:: *******************************************************

:END

ENDLOCAL

@ECHO ON
