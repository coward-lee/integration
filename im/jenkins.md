1. 主服务器build，将运行分配给其他节点

## jenkins 问题记录
touch: cannot touch '/var/jenkins_home/copy_reference_file.log': Permission denied
Can not write to /var/jenkins_home/copy_reference_file.log. Wrong volume permissions?     
解决     
docker run -u 0    
在run 后面加上 -u 0

