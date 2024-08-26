#!/usr/bin/env bash

git add .
echo "Commit message: "
read -r commitMessage;
if [ -z "$varname" ]; then
    commitMessage="Commit "$(date +'%Y.%m.%d %H:%M')
fi
git commit -m "$commitMessage"
git push
cd software/ || exit
mvn clean install --quiet
cd ../infrastructure/ || exit
#cdk synth
#cdk bootstrap
cdk deploy --require-approval never
