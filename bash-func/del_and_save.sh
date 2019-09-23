#!/bin/bash

arr=('str1' 'str3' 'str1' 'str1' 'str1' 'str2' 'str3')

mkdir -pv tmp

for i in "${!arr[@]}"; do
    echo "${arr[$i]}" > tmp/$i.txt
done


md5sum tmp/* | \
sort | \
awk 'BEGIN{lasthash = ""} $1 == lasthash {print $2} {lasthash = $1}' |\
xargs rm -v
