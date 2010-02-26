#!/bin/sh

# TAU LIST FILE
pushd OT/cfg/jcmt
ls tau*.dat > tau.list
popd

# VERSION FILES
DATE=`date '+%Y%m%d'`
VERSION=`git log | head -n 1 | cut -f2 -d ' '`
echo "$DATE [$VERSION]" > OT/cfg/jcmt/versionFile
echo "$DATE [$VERSION]" > OT/cfg/ukirt/versionFile
