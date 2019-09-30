#!/bin/bash

set -e

docker build -t fetchlin/backend:$TRAVIS_COMMIT .
docker tag fetchlin/backend:$TRAVIS_COMMIT fetchlin/backend:latest

docker push fetchlin/backend:latest