cat>/etc/docker/daemon.json <<END
{
  "registry-mirrors": [
    "https://registry.docker-cn.com",
    "https://mirror.baidubce.com",
    "https://hub-mirror.c.163.com",
    "https://ustc-edu-cn.mirror.aliyuncs.com",
    "https://ghcr.io",
  ]
}
END


export http_proxy="http://192.168.214.1:7890"
export https_proxy="http://192.168.214.1:7890"
export no_proxy="localhost,127.0.0.1,::1"