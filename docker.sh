
## zookeeper
docker run --name zookeeper1 --link some-zookeeper:zookeeper -d application-that-uses-zookeeper
docker run -p 2181:2181 --name zookeeper --restart always -d zookeeper
etc/mysql/conf.d/
## mysql
docker run -d --name mysql -p 3306:3306  -v /usr/local/mysql/data:/var/lib/mysql/ -e MYSQL_ROOT_PASSWORD=666666 mysql

