docker run --name zookeeper1 --link some-zookeeper:zookeeper -d application-that-uses-zookeeper



docker run -p 2181:2181 --name zookeeper --restart always -d zookeeper