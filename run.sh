#!/bin/bash
# -----------------------------------------------------------------------------
# Script Name: run.sh
# Description: Run Spring Boot backend (Gradle) with H2 TCP server and optional frontend
# Author: Ransford Addai
# Date: 2025-09-17
# -----------------------------------------------------------------------------

kill -9 $(lsof -i:9000)

export $(cat .env | xargs)

./gradlew --stop
./gradlew bootRun
