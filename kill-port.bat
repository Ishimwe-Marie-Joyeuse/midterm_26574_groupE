@echo off
REM Kill process using port (default: 8083). Usage: kill-port.bat [port]
set PORT=%1
if "%PORT%"=="" set PORT=8083

echo Finding process on port %PORT%...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%PORT%" ^| findstr "LISTENING"') do (
    echo Killing PID %%a
    taskkill /PID %%a /F
    goto :done
)
echo No process found on port %PORT%
:done
echo Port %PORT% is now free.
