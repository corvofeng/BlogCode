#!/bin/bash

arr=('version-1' 'version-2' 'version-3' 'version-39' 'version-13')

# 先使用sed, 将version-<num>中的num提取出来, 转换为<num>@version-<num>的形式, 再使用sort对@前面的值排序
data=($(for a in "${arr[@]}"; do echo "$a" | sed 's/.*version-\([0-9]*\)/\1@&/'; done | sort -t @ -k 1 -nr))


echo ${data[@]}
