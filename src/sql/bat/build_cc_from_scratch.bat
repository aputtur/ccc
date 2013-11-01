@echo off
rem
rem build_cc_from_scratch.bat
rem

if "%1"=="" goto USAGE

rem Delete next line containing "goto FINISHED" in order to execute this script.
echo.
echo. Note: As a protective measure, this script must be edited in order to execute.
echo.
::goto FINISHED

if EXIST "../build/build_properties_%1.sql" (
   set envkey=%1
) ELSE (
   echo.
   echo. Cant find the environment properties file for env key %2
   echo. was looking for file ../build/build_properties_%1.sql
   echo.
   goto USAGE
)











if NOT EXIST "N:\builds\db\cc\logs" (
	echo.
	echo. Can't find the N:\builds\db\cc\logs directory
	echo.
	echo. This path must exist to run this script
	echo.
	goto USAGE
)

echo.
echo.
echo. Warning: This script will DROP ALL OBJECTS in the cc schema !!
echo.
echo.                  All Data Will Be lost
echo.
echo.

set /P continue=Are you Sure^?

if /I "%continue%" NEQ "Y" (
   goto FINISHED
)


sqlplus  /nolog @./run_cc_script.sql ./recreate_cc_ddl.sql %envkey% cc_user 2>&1 | mtee ..\ddl.log


set hh=%TIME:~0,1%

if "%hh%"==" " (
	set hh=0%TIME:~1,1%
) else (
	set hh=%TIME:~0,2%
)

set id=%DATE:~4,2%%DATE:~7,2%%DATE:~10,4%-%hh%%time:~3,2%%time:~6,2%

copy ..\ddl.log N:\builds\db\cc\logs\%envkey%-ddl-scratch-%id%.log

call check_errors.bat ..\ddl.log

goto FINISHED

:USAGE
echo.usage: build_cc_from_scratch.bat ^<enviroment key^>
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


:FINISHED







