@ECHO OFF
SETLOCAL
where gradle >nul 2>nul
IF %ERRORLEVEL% EQU 0 (
  gradle %*
) ELSE (
  ECHO Gradle is not installed. Please install Gradle 8.6 or newer.
  EXIT /B 1
)
