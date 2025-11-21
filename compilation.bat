@echo off
REM Compilation script: build images for frontend and backend using BuildKit
setlocal
set "DOCKER_BUILDKIT=1"
set "COMPOSE_DOCKER_CLI_BUILD=1"

echo Building images (frontend and backend)...
docker compose build --parallel

if %ERRORLEVEL% neq 0 (
  echo Build failed with exit code %ERRORLEVEL%.
  exit /b %ERRORLEVEL%
)

echo Build finished successfully.
endlocal
