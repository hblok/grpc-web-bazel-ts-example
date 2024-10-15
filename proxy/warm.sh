#!/bin/bash

THIS_DIR="$(readlink -f $(dirname $0))"

cd $THIS_DIR

docker compose pull
docker compose build

