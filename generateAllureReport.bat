@echo off
echo Allure Report Generator

:: Get the current directory
set CURRENT_DIR=%cd%

:: Check if allure-results directory exists
if not exist "%CURRENT_DIR%\allure-results" (
    echo Error: allure-results directory not found!
    echo Please run your tests first to generate results.
    pause
    exit /b 1
)

:: Check if allure command is available
where allure >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Error: Allure command-line tool not found in PATH!
    echo Please install Allure command-line tool and add it to your PATH.
    pause
    exit /b 1
)

:: Generate the report
echo Generating Allure Report...
call allure generate "%CURRENT_DIR%\allure-results" --clean -o "%CURRENT_DIR%\allure-report"

:: Open the report
echo Opening Allure Report...
call allure open "%CURRENT_DIR%\allure-report"

echo Report process complete!
pause