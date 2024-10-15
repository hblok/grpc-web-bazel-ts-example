#!/usr/bin/bash

echo -e "\n\n"

#echo "### env"
#env | sort
#echo "======="

echo "### tree"
pwd
tree ..
find -type d
echo "======="

echo -e "\n"

cd web
touch favicon.ico
python3 -m http.server 12011

