# jdb
jdb
jcmd {pid} help
- jdb
- java起动加上 vm参数-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5008
- jdb -attach 5008
- stop at class:line
- catch java.lang.NullPointerException 在异常抛出时打断点
- dump eval 打印变量
- cont继续执行
# 内存泄漏
## jstat -gcutil [pid] [时间间隔]
一些术语的中文解释：
- S0C：年轻代中第一个survivor（幸存区）的容量 (字节)
- S1C：年轻代中第二个survivor（幸存区）的容量 (字节)
- S0U：年轻代中第一个survivor（幸存区）目前已使用空间 (字节)
- S1U：年轻代中第二个survivor（幸存区）目前已使用空间 (字节)
- EC：年轻代中Eden（伊甸园）的容量 (字节)
- EU：年轻代中Eden（伊甸园）目前已使用空间 (字节)
- OC：Old代的容量 (字节)
- OU：Old代目前已使用空间 (字节)
- MC:
- MU:
- CCSC:
- CCSU:
- YGC：从应用程序启动到采样时年轻代中gc次数
- YGCT：从应用程序启动到采样时年轻代中gc所用时间(s)
- FGC：从应用程序启动到采样时old代(全gc)gc次数
- FGCT：从应用程序启动到采样时old代(全gc)gc所用时间(s)
- CGC:
- CGCT:
- GCT:



- PC：Perm(持久代)的容量 (字节)
- PU：Perm(持久代)目前已使用空间 (字节)
- GCT：从应用程序启动到采样时gc用的总时间(s)
- NGCMN：年轻代(young)中初始化(最小)的大小 (字节)
- NGCMX：年轻代(young)的最大容量 (字节)
- NGC：年轻代(young)中当前的容量 (字节)
- OGCMN：old代中初始化(最小)的大小 (字节)
- OGCMX：old代的最大容量 (字节)
- OGC：old代当前新生成的容量 (字节)
- PGCMN：perm代中初始化(最小)的大小 (字节)
- PGCMX：perm代的最大容量 (字节)
- PGC：perm代当前新生成的容量 (字节)
- S0：年轻代中第一个survivor（幸存区）已使用的占当前容量百分比
- S1：年轻代中第二个survivor（幸存区）已使用的占当前容量百分比
- E：年轻代中Eden（伊甸园）已使用的占当前容量百分比
- O：old代已使用的占当前容量百分比
- P：perm代已使用的占当前容量百分比
- S0CMX：年轻代中第一个survivor（幸存区）的最大容量 (字节)
- S1CMX ：年轻代中第二个survivor（幸存区）的最大容量 (字节)
- ECMX：年轻代中Eden（伊甸园）的最大容量 (字节)
- DSS：当前需要survivor（幸存区）的容量 (字节)（Eden区已满）
- TT： 持有次数限制
- MTT ： 最大持有次数限制