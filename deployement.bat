@echo off
REM Deployment script: build and start containers in detached mode
setlocal
set "DOCKER_BUILDKIT=1"
set "COMPOSE_DOCKER_CLI_BUILD=1"

echo Building and starting services...
docker compose up -d --build

if %ERRORLEVEL% neq 0 (
  echo Docker compose failed with exit code %ERRORLEVEL%.
  exit /b %ERRORLEVEL%
)

echo Deployment complete.
endlocal
