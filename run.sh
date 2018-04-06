#!/bin/sh

if [ ! -d "bin" ]; then
    ##echo "bin directory does not exist, building project"
    ./build.sh
fi

if [ "$#" != 2 ]; then
    echo "Usage: ./run.sh <initial-graph.json file> <events.json file>"
    exit 1
fi

java -cp bin:libs/jackson-all-1.9.0.jar com.stackstate.Main "$1" "$2"