#!bin/bash

if [ $# -lt 1 ]
then
  echo "no args input"
  exit;
fi

case $1 in
"start")
  echo " ====================启动 hadoop集群====================="

  echo " ====================启动 dfs集群====================="
  ssh hadoop102 "bash /root/software/hadoop-3.1.3/sbin/start-dfs.sh"
  echo " ====================启动 yarn====================="
  ssh hadoop103 "bash /root/software/hadoop-3.1.3/sbin/start-yarn.sh"
  echo " ====================启动 historyserver====================="
  ssh hadoop102 "bash /root/software/hadoop-3.1.3/bin/mapred --daemon start historyserver"
;;
"stop")
  echo " ====================关闭 hadoop集群====================="

  echo " ====================关闭 historyserver====================="
  ssh hadoop102 "bash /root/software/hadoop-3.1.3/bin/mapred --daemon stop historyserver"
  echo " ====================关闭 yarn====================="
  ssh hadoop103 "bash /root/software/hadoop-3.1.3/sbin/stop-yarn.sh"
  echo " ====================关闭 dfs集群====================="
  ssh hadoop102 "bash /root/software/hadoop-3.1.3/sbin/stop-dfs.sh"
;;
*)
   echo "input error; valid args are:{ start, stop}"
;;
esac