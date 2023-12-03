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

# 2. 数据访问
当你尝试访问缓存中的数据时，Caffeine Cache首先会查找数据是否已经缓存。
如果数据存在，它会更新数据项的访问顺序，以确保最常用的数据项位于链表的前部，以便快速访问。
这有助于提高缓存的命中率。

# 3. 缓存清理
Caffeine Cache实现了一种淘汰策略，
以在内存不足时释放部分缓存项， 以腾出空间供新数据。
这个策略基于缓存的大小和权重进行控制。
权重是一个用于表示缓存项相对大小的值。
当内存不足时，Caffeine Cache会尝试淘汰权重较大的缓存项，以腾出足够的内存空间。

# 4. 过期策略
Caffeine Cache支持多种过期策略，
可以根据需求进行配置。
你可以定义数据项的过期时间，或者基于访问次数来设置数据项的过期。
Caffeine Cache还支持手动触发数据项的过期。
过期策略的目的是确保缓存中的数据始终保持新鲜和有效。

# 5. 并发控制：
Caffeine Cache使用一些并发控制技术，如无锁操作，
来确保多个线程可以安全地访问和操作缓存。
这有助于提高Caffeine Cache的性能和可伸缩性。
