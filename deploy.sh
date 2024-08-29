#!/usr/bin/env bash

#echo "Commit message: "
#read -r commitMessage;
#if [ -z "$varname" ]; then
#    commitMessage="Commit "$(date +'%Y.%m.%d %H:%M')
#fi
#git add .
#git commit -m "$commitMessage"
#git push
cd software/ || exit
mvn clean install --quiet
#cd ../infrastructure/ || exit
#cdk synth
#cdk bootstrap
#cdk deploy --all --require-approval never
