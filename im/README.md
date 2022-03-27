## 错误记录


1. 客户端发送了消息但是服务器端没有接收到
   其实是压根没有发送是我，写的write 没有flush;
# idea console 输出出现中文乱码问题
1. 改fileencoding
2. 添加idea64.exe.vmoptions
   中 -Dfile.encoding=UTF-8