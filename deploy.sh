#!/usr/bin/env bash

: '
aws configure
aws configure sso
'
#echo "Commit message: "
#read -r commitMessage;
#if [ -z "$varname" ]; then
#    commitMessage="Commit "$(date +'%Y.%m.%d %H:%M')
#fi
#git add .
#git commit -m "$commitMessage"
#git push

#cd software/ || exit
#mvn clean install --quiet
#cd StaticSite/ || exit
#npm run --silent build
#cd ../../

cd infrastructure/ || exit
cdk synth
cdk bootstrap
cdk deploy --all --require-approval never
