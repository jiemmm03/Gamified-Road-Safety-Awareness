@echo off
title RoadSafe AI - Instant Mobile App Share Server
echo ===================================================
echo   RoadSafe AI - Instant App Share ^& Live Admin Server
echo ===================================================
echo.

cd /d "%~dp0"

echo [1/4] Copying latest compiled APK into server folder...
if exist "..\app\build\outputs\apk\debug\app-debug.apk" (
    copy /y "..\app\build\outputs\apk\debug\app-debug.apk" "app-debug.apk" >nul
    echo   [OK] Latest APK synced: app-debug.apk
) else if exist "app-debug.apk" (
    echo   [OK] Using existing app-debug.apk in server folder.
) else (
    echo   [WARNING] No APK found! Build your app first in Android Studio.
    echo   Expected at: ..\app\build\outputs\apk\debug\app-debug.apk
)

echo.
echo [2/4] Detecting Local Wi-Fi IP Address...

:: Try multiple methods to find local IP
set LOCAL_IP=

:: Method 1: ipconfig (most reliable on Windows)
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /i "IPv4" ^| findstr /v "127.0.0" ^| findstr /v "169.254"') do (
    set LOCAL_IP=%%a
    goto :ip_found
)

:: Method 2: route table
for /f "tokens=4" %%a in ('route print ^| findstr "0.0.0.0" ^| findstr /v "0.0.0.0.*0.0.0.0"') do (
    set LOCAL_IP=%%a
    goto :ip_found
)

:ip_found
:: Trim leading space
for /f "tokens=* delims= " %%b in ("%LOCAL_IP%") do set LOCAL_IP=%%b

if "%LOCAL_IP%"=="" (
    set LOCAL_IP=localhost
    echo   [WARNING] Could not detect Wi-Fi IP. Using localhost.
    echo   Make sure you are connected to Wi-Fi.
) else (
    echo   [OK] Your Local IP: %LOCAL_IP%
)

set SERVER_PORT=8080
set DOWNLOAD_URL=http://%LOCAL_IP%:%SERVER_PORT%/download.html
set ADMIN_URL=http://%LOCAL_IP%:%SERVER_PORT%/index.html
set APK_URL=http://%LOCAL_IP%:%SERVER_PORT%/app-debug.apk

echo.
echo [3/4] Server URLs:
echo.
echo   =====================================================
echo   DOWNLOAD PAGE (share this / scan QR):
echo     %DOWNLOAD_URL%
echo.
echo   DIRECT APK DOWNLOAD:
echo     %APK_URL%
echo.
echo   ADMIN COMMAND CENTER:
echo     %ADMIN_URL%
echo   =====================================================
echo.

echo [4/4] Opening Download Page ^& Starting Web Server on Port %SERVER_PORT%...
start "" "%DOWNLOAD_URL%"

echo.
echo ===================================================
echo   SERVER IS ACTIVE!
echo.
echo   1. Other phones on the same Wi-Fi can scan the QR
echo      or open: %DOWNLOAD_URL%
echo   2. Admin dashboard: %ADMIN_URL%
echo ===================================================
echo.
echo Press Ctrl+C anytime to stop the server.
echo.

python -m http.server %SERVER_PORT%
pause
