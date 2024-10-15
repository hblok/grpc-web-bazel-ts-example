#!/bin/bash

function show_link() {
    sleep 3

    echo -e "\n\n"
    echo ". . . . . . . . . . . . . . . . . . . . . ."
    echo
    echo "Frontend running on http://localhost:12000"
    echo
    echo ". . . . . . . . . . . . . . . . . . . . . ."
}

bazel build ...
./proxy/warm.sh

./web/start_static_web_server.sh &
./server/start_grpc_server.sh &
show_link &
./proxy/start_proxy.sh
