#!/bin/bash
set -e

JAVA=/usr/lib/jvm/java-17-openjdk-amd64

mkdir -p out
"$JAVA/bin/javac" -d out App.java

mkdir -p dist
"$JAVA/bin/jar" --create --file=dist/App.jar --main-class=App -C out .

rm -rf App

"$JAVA/bin/jpackage" \
  --input dist \
  --name App \
  --main-jar App.jar \
  --main-class App \
  --add-modules java.base \
  --type app-image
