
1. 什么样的架构
2. brocker 和 name server
3. 一共有那些角色
4. 怎么实现高可用的，
   1. 客户端需要实现高可用的嘛
   2. 消费者需要实现高可用吗
   3. brocker 怎么实现高可用
   4. brocker 挂了会怎样
   5. name server 挂了会怎样
   6. producer？
   7. consumer?
5. 他与 Kafka的区别是什么
6. 他有那些消息消费形式
7. 消息是怎么存储的
8. 怎么实现定时任务
9. 他的IO实现有哪些优化
10. 消息的索引是怎么建立的
11. RocketMQ 为什么不建议在生产环境中使用自动创建Topic这个功能
12. RocketMQ 路由信息是什么，为什么需要它
13. RocketMQ 中的queueId的作用
14. RocketMQ 中 tags 是作用是什么
15. RocketMQ 的负载均衡的实现，Borker的负载均衡和Consumer的负载均衡的实现
16. RocketMQ HA 的实现
17. RocketMQ 的主从（master/slave）实现
18. RocketMQ 存储的如何实现高性能的
19. RocketMQ 中的一条Msg消息是如何存在在文件中的（以什么样的格式）
20. RocketMQ 顺序消费消息的实现
21. RocketMQ 事物消息的实现
22. RocketMQ 消息消费失败的处理方式
23. RocketMQ 的序列化
24. RocketMQ 如何记录消息消费的位置(offset)，在重启之后继续消费
25. RocketMQ 如何通过MessageId查询一个消息
26. RocketMQ Client(Producer&Consumer) 与 Borker 的通信方式
27. RocketMQ consumerQueue 的作用
28. RocketMQ rebalance 的作用
29. RocketMQ 在消息消费失败之后，把消息发送到 Broker 之后，是如何存储的，MessageId 会变吗，存储的位置会变吗
30. RocketMQ 中事物消息 RMQ_SYS_TRANS_HALF_TOPIC 在事物失败之后，会被从磁盘删除吗，还是只是标记了删除
31. RocketMQ 事物消息的回查是如何触发的（查询事物消息的状态）
32. RocketMQ Consumer 是如何知道有消息可以消费了，是 Consumer Pull 还是 Borker Push
33. RocketMQ Consumer 消费过慢会怎么样
34. RocketMQ 批量消息的使用场景
35. RocketMQ 与 kafka 的对比，场景用kafka，什么场景用 RocketMQ。以及各自的优缺点。
36. RocketMQ 消费失败的次数超过最大次数（默认是16次）会对消息做什么特殊的处理

   rocketmq 二十三问
    https://mp.weixin.qq.com/s?__biz=MzkyMzU5Mzk1NQ==&mid=2247506722&idx=1&sn=16bcd1ef2a1dd72f017f22c715ee9b9f&source=41#wechat_redirect

37. 为什么要使用消息队列呢？
38. 为什么要选择RocketMQ?
39. RocketMQ有什么优缺点？
40. 消息队列有哪些消息模型？
41. 那RocketMQ的消息模型呢？
42. 消息的消费模式了解吗？
43. RoctetMQ基本架构了解吗？
44. 如何保证消息的可用性/可靠性/不丢失呢？
45. 如何处理消息重复的问题呢？
46. 怎么处理消息积压？
47. 顺序消息如何实现？
48. 如何实现消息过滤？
49. 延时消息了解吗？
50. 怎么实现分布式消息事务的？半消息？
51. 死信队列知道吗？
52. 如何保证RocketMQ的高可用？
53. 说一下RocketMQ的整体工作流程？
54. 为什么RocketMQ不使用Zookeeper作为注册中心呢？
55. 说说RocketMQ怎么对文件进行读写的？
56. 消息刷盘怎么实现的呢？
57. 能说下 RocketMQ 的负载均衡是如何实现的？
58. RocketMQ消息长轮询了解吗？
