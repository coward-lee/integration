# 问题（解决的需求）

1. 降鲜
2. 访问频率的存储的空间的节约
3. 解决突然的稀疏频率访问
4. 解决长期下来的访问频率累计问题

# 整体类概览
## 淘汰算法

1. Caffeine
主要的对话build类
2. Cache/AsyncCache 

### Cache 顶级父类, 同步加载，这个会在同一个线程进行loading 数据


![cache](../img/caffeine_cache_super_interface.png)

1. LoadingCache
   由缓存自动加载数据的缓存，也就是我们只需要传入一个key 不需要传入 loader，  
   这个loader 可以在最后build 的时候进行指定,  
   代码如下，省略其他配置
   ```java
   class Test {
      void demo_test() {
          Cache<Object, Object> build = Caffeine.newBuilder()
                  .build(new CacheLoader<Object, Object>() {
                      @Override
                      public @Nullable Object load(@NonNull Object key) throws Exception {
                          return key + "value";
                      }
                  });
      }
   }
   ```
2. LocalManualCache
   主动加载数据的，也就是在 Cache#get(KEY,Function)，这个方法在

### AsyncCache
能看出来这这个的loader 主要与前面的cache主要是一些执行的时候需要带一个executor以及返回值是一个CompletableFuture的异步结果
![async_cache](../img/caffeube_async_cache_super_class.png)
   ```java
   class Test {
       void demo_test_with_async()  {
           AsyncCache<Object, Object> build = Caffeine.newBuilder().buildAsync(new AsyncCacheLoader<>() {
               @Override
               public @NonNull CompletableFuture<Object> asyncLoad(@NonNull Object key, @NonNull Executor executor) {
                   return CompletableFuture.supplyAsync(() -> {
                       return key + "async loading";
                   });
               }
           });
       }
   }
   ```
# 1. 数据存储
Caffeine Cache使用一种高效的数据结构来存储缓存数据。
它的数据结构主要是一个基于哈希表和链表的数据结构，其中哈希表用于快速查找缓存项，
链表用于维护缓存项的访问顺序。这种结构使得数据查找操作非常高效。
1. 用什么存储数据
   ConcurrentHashMap
2. 用什么存储过期时间
final Buffer<Node<K, V>> readBuffer; // 这似乎是一个错的
3. 用什么存储读写信息
final MpscGrowableArrayQueue<Runnable> writeBuffer;
mpsc 这是一个重点
4. 用什么存储访问频率
5. 他的三个区域分别代表什么

度缓冲区
典型的缓存锁定每个操作，以安全地对 访问队列中的条目进行重新排序。


# caffeine 降鲜机制
解决LRU 在长期运行之后无法将频率过高，但是未来可能不会访问的元素问题
逻辑如下
1. 刚开始运行到一定次数（假定是n）后将所有元素的计数除以2
2. 第二次运行到  n + n/2 （因为第一步是对所有计数除2，所以在这个时候所有的计数加起来就是n了）
3. 第三次运行到  n + n/2 + n/2 
4. 以此类   
基于 caffeine 论文的证明来看当前次数越大那么真实的命中率期望（也就是缓存命中率）越接近不进行降鲜的命中率，当趋近于无穷的时候那么他们相等
caffeine 代码如下(我们重点看reset方法)：
带来的问题 
   1. 对于访问次数超出15的数据怎么处理，为什么这么处理
```java
final class FrequencySketch<E> {
   @SuppressWarnings("ShortCircuitBoolean")
   public void increment(E e) {
      if (isNotInitialized()) {
         return;
      }

      int[] index = new int[8];
      int blockHash = spread(e.hashCode());
      int counterHash = rehash(blockHash);
      int block = (blockHash & blockMask) << 3;
      for (int i = 0; i < 4; i++) {
         int h = counterHash >>> (i << 3);
         index[i] = (h >>> 1) & 15;
         int offset = h & 1;
         index[i + 4] = block + offset + (i << 1);
      }
      boolean added =
              incrementAt(index[4], index[0])
                      | incrementAt(index[5], index[1])
                      | incrementAt(index[6], index[2])
                      | incrementAt(index[7], index[3]);

      if (added && (++size == sampleSize)) {
         reset();
      }
   }

   /** Reduces every counter by half of its original value. */
   void reset() {
      int count = 0;
      for (int i = 0; i < table.length; i++) {
         count += Long.bitCount(table[i] & ONE_MASK);
         table[i] = (table[i] >>> 1) & RESET_MASK;
      }
      size = (size - (count >>> 2)) >>> 1;
   }
}
```

如上的代码可是 每次当到达15之后
优势
 访问频率统计所消耗的内存明显降低（理论上的89% 出资tinyLFU 论文）

## ReadBuffer 读缓冲
实现类 BoundedBuffer, 也是在 mpsc buffer的基础上进行实现和扩张出来，同时允许写入的丢失事务
- A striped, non-blocking, bounded buffer.
- A multiple-producer / single-consumer buffer.
![](../img/caffeine_mpsc_buffer.png)
- 作用以及优化点
1. 这个其实也是一个典型削峰的行为，因为多个线程同时写入，势必会导致写入速度容易大于消费速度，所以使用这个来优化
2. 由于是属于读buffer 的优化，没有必要保证一定写入，如果没有写入buffer，那么就马上对当前的node执行onAccess方式（更新节点过期时间访问频率等）
3. 同时为了优化写入速度，实现类（BoundedBuffer），内部使用的stripedBuffer也就是将buffer 分成了好几个buffer 依次来增加并发性
<pre>
 A multiple-producer / single-consumer buffer that rejects new elements if it is full or
 fails spuriously due to contention. Unlike a queue and stack, a buffer does not guarantee an
 ordering of elements in either FIFO or LIFO order.
 <p>
 Beware that it is the responsibility of the caller to ensure that a consumer has exclusive read
 access to the buffer. This implementation does <em>not</em> include fail-fast behavior to guard
 against incorrect consumer usage.

A circular ring buffer stores the elements being transferred by the producers to the consumer.
The monotonically increasing count of reads and writes allow indexing sequentially to the next
element location based upon a power-of-two sizing.

The producers race to read the counts, check if there is available capacity, and if so then try
once to CAS to the next write count. If the increment is successful then the producer lazily
publishes the element. The producer does not retry or block when unsuccessful due to a failed
CAS or the buffer being full.

The consumer reads the counts and takes the available elements. The clearing of the elements
and the next read count are lazily set.

This implementation is striped to further increase concurrency by rehashing and dynamically
adding new buffers when contention is detected, up to an internal maximum. When rehashing in
order to discover an available buffer, the producer may retry adding its element to determine
whether it found a satisfactory buffer or if resizing is necessary.

一个用于在生产者和消费者之间传递元素的并发、无锁的循环环形缓冲区的实现.
一种数据结构，呈环形，用于将元素从生产者传递到消费者。
读取和写入的计数不断递增。这使得可以基于二的幂次方的大小进行顺序索引。?
多个生产者争用读取计数，检查是否有可用容量，如果有，就尝试一次 CAS 操作来增加下一个写入计数。如果成功，生产者就会惰性地发布元素。如果 CAS 失败或缓冲区已满，生产者不会重试或阻塞。
消费者读取计数并获取可用元素。元素的清除和下一个读取计数的更新是惰性进行的。
增加并发性的分片: **该实现进行了分片以增加并发性**。这涉及在检测到争用时重新散列并动态添加新的缓冲区，最多添加到内部最大值。
</pre>

## write buffer

回放操作，不允许写入丢失。
- 概述解释   
This is a shaded copy of MpscGrowableArrayQueue provided by JCTools  from version 2.0.   
这个是从jctool 复制过来代码
- 作用
用于回放操作，他的队列存储的都是一个runnable ，这个是时候，在afterWrite之后被加入到buffer
在 maintenance/clear 的时候（一个消费者执行）,将buffer里面task弹出来并run
- 性能优化
1. cache line buffer 填充
2. 多级队列，再扩容的时候直接直接分配新的数组，并将新的数据通过链表的形式放到老数组的末尾，



# 参考
https://tig.red/caffeine.html