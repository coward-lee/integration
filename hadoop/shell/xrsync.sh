#!/bin/bash

# 1. 参数判断

if [ $# -lt 1 ]; then
   echo not enough arg!
    exit
fi

# 2, 遍历集群
for host in hadoop102 hadoop103 hadoop104
do
  echo ===============  $host ================================

  # 3. 遍历目录，挨个发送
  for file in $@
  do
    #4 判断文件是否存在
    if [ -e $file ]
     then
       # 5. 获取父目录
       pdir=$(cd -P $(dirname $file); pwd)

       # 6. 获取当前文件名称
       fname=$(basename $file)
       ssh $host "mkdir -p $pdir "
       rsync -av $pdir/$fnpwdame $host:$pdir
    else
      echo $file does not exist!
    fi
  done
done



