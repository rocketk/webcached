@echo off
echo.
echo.
pause
echo.

cd %~dp0
cd..

call mvn clean install -Dmaven.test.skip=true

cd bin
pause