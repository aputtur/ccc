@echo off

set logfile=%1

FINDSTR /I "ORA-" %logfile% > NUL
if "%ERRORLEVEL%"=="0" GOTO FAILURE
FINDSTR /I "SP2-" %logfile% > NUL
if "%ERRORLEVEL%"=="0" GOTO FAILURE
FINDSTR /I "DRG-" %logfile% > NUL
if "%ERRORLEVEL%"=="0" GOTO FAILURE
FINDSTR /I "PLS-" %logfile% > NUL
if "%ERRORLEVEL%"=="0" GOTO FAILURE
FINDSTR /I "WARNING:" %logfile% > NUL
if "%ERRORLEVEL%"=="0" GOTO FAILURE
FINDSTR /I /C:"USER REQUESTED INTERRUPT" %logfile% > NUL
if "%ERRORLEVEL%"=="0" GOTO FAILURE

GOTO SUCCESS



:FAILURE
ECHO.
ECHO.
ECHO.WARNING: Deployment finished with ERRORS. See %logfile% for details.
ECHO.
ECHO.
GOTO DONE


:SUCCESS
ECHO.
ECHO.
ECHO.Deployment finished, no errors detected. See %logfile% for details.
ECHO.
ECHO.

:DONE