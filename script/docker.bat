set "DOCKER_BUILDKIT=1" && set "COMPOSE_DOCKER_CLI_BUILD=1" && docker compose build app

docker compose up --no-deps --build db-seed