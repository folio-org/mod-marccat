#!/usr/bin/env bash

git submodule init
git submodule update

npm install

rm -rf generated

mkdir generated
mkdir generated/html
mkdir generated/md

node_modules/raml2html/bin/raml2html -i ../ramls/marccat.raml -o generated/html/marccat.html

node_modules/raml2md/bin/raml2md -i ../ramls/marccat.raml -o generated/md/marccat.md
