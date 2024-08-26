#!/usr/bin/env bash

git add .
echo "Commit message: "
read varname
git commit -m "$varname"
git push
#cd software/ || exit
#mvn clean install --quiet
#cd ../infrastructure/ || exit
#cdk synth
#cdk bootstrap
#cdk deploy --require-approval never
