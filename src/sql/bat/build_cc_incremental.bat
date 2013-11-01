@echo off
rem
rem build_cc_incremental.bat
rem

if "%1"=="" goto USAGE
if "%2"=="" goto USAGE






if EXIST "../build/build_properties_%1.sql" (
   set envkey=%1
) ELSE (
   echo.
   echo. Cant find the environment properties file for env key %1
   echo. was looking for../build/build_properties_%1.sql
   echo.
   goto USAGE
)

if EXIST "../RELEASE_DELTA/%2.sql" (
	set deltascript=%2
) ELSE (
   echo.
   echo. Cant find the script for upgrade symbol %2
   echo.
   goto FINISHED
)


if NOT EXIST "N:\builds\db\cc\logs" (
	echo.
	echo. Can't find the N:\builds\db\cc\logs directory
	echo.
	echo. This path must exist to run this script
	echo.
)

echo.
echo.
echo. This script will increment the cc schema from 
echo. one release to the next
echo.
echo.
echo.

set /P continue=Are you Sure^?

if /I "%continue%" NEQ "Y" (
   goto FINISHED
)


sqlplus  /nolog @./run_cc_script.sql ../RELEASE_DELTA/%deltascript%.sql %envkey% cc_user 2>&1 | mtee ..\ddl.log

sqlplus  /nolog @./run_cc_script.sql ./recreate_cc_logic_only_ddl.sql %envkey% cc_user 2>&1 | mtee/+ ..\ddl.log


set hh=%TIME:~0,1%

if "%hh%"==" " (
	set hh=0%TIME:~1,1%
) else (
	set hh=%TIME:~0,2%
)

set id=%DATE:~4,2%%DATE:~7,2%%DATE:~10,4%-%hh%%time:~3,2%%time:~6,2%

copy ..\ddl.log N:\builds\db\cc\logs\%envkey%-ddl-incremental-%id%.log

call check_errors.bat ..\ddl.log

goto FINISHED

:USAGE
echo.usage: build_cc_incremental.bat ^<enviroment key^> ^<upgrade symbol^> 
echo.
echo.environment key must be one of:
setlocal EnableExtensions EnableDelayedExpansion     
for %%x in (..\build\build_properties_*.sql) do (
	set key=%%x
	set key=!key:~26!
	set key=!key:~0,-4!
	echo !key!
)

echo. 
echo. The environment key is used to identify which property file to use during
echo. execution.
echo.
echo. See the ../build/build_properties_xxx.sql files
echo.
echo. The ^<upgrade symbol^> is used by the script to identify which 
echo. sql script makes the ddl changes for this release.
echo. The upgrade symbol specifies the current version number and the new version.
echo. For Example, VER_0082_TO_VER_0083 would upgrade the cc schema from version 0082 to version 0083. 
echo. It would do this by executing the RELEASE_DELTA\VER_0082_TO_VER_0083.sql script
echo.
echo.


:FINISHED







