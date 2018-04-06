#!/bin/bash

rm -rf bin
mkdir bin
javac -d bin -sourcepath src/main/java -cp libs/jackson-all-1.9.0.jar src/main/java/com/stackstate/Main.java