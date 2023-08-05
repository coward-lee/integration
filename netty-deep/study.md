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
1. netty 抽象出两组线程池，master group 专门负责收客户端链接，worker 专门负责网络读写
2. BossGroup和WorkerGroup 类型都是NioEventLoopGroup
3. NioEventLoopGroup 相当于一个事件循环组，这个组中包含出个时间循环，每个时间循环是NioEventLoop
4. NioEventLoop表示一个不断循环的执行处理任务的线程, NioEventLoop都有一个selector，用于监听绑定在其上的Socket网络通讯
5. NioEventLoop可以有多个线程，既可以有多个 NioEventLoop
6. 每个master NioEventLoop 执行的步骤有3步
   1. 轮询处理accept事件
   2. 处理accept时间，与client建立链接，生成NioSocketChannel，并将其注册到某个worker 的NioEventLoop上的selector
   3. 处理任务队列的任务，及runAllTasks
7. 每个worker NioEventLoop 循环执行的步骤
   1. 轮询read， write事件，
   2. 处理i/o事件，即read，write事件，在对应NioSocketChannel处理
   3. 处理任务队列的任务，及runAllTasks
8. 每个Worker NioEventLoop处理业务时会使用到pipeline，pipeline就是handler的双向链表




