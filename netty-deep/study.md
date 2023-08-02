# nio 基础概念
1. selector  对应一个线程
2. channel  一个selector 会有多个channel 注册到selector/程序，他是双向的可以用于输入也可以用于输出
3. buffer  内存块，底层是一个数组，这个是程序操作的对象，
<pre>
         |————buffer —— channel |      
程序      |————buffer —— channel |    selector
         |————buffer —— channel |    
</pre>

# netty 基础组件
1. channel
2. selector
3. eventLoop
4. byteBuf
5. handler



