#!/usr/bin/env bash

cd software/ || exit
mvn clean install --quiet
#cd ../infrastructure/ || exit
#cdk synth
#cdk bootstrap
cdk deploy --require-approval never
