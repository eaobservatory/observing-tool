#!/bin/sh

for file in lib/jsky3/*.jar
do
    newfile=../jsky/jskycat/target/jskycat-3.0-assembly.dir/lib/`basename $file`
    newfilenl=../jsky/jskycat/target/jskycat-3.0-assembly.dir/`basename $file`
    if [[ -e $newfile ]]
    then
        if [[ `md5sum < $newfile` !=  `md5sum < $file` ]]
        then
            echo Updating: $file
            cp $newfile $file
        else
            echo Already up to date: $file
        fi
    elif [[ -e $newfilenl ]]
    then
        if [[ `md5sum < $newfilenl` != `md5sum < $file` ]]
        then
            echo Updating: $file
            cp $newfilenl $file
        else
            echo Already up to date: $file
        fi
    else
        echo Does not exist: $newfile
    fi
done
