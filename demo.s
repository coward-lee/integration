docker run --name mysql -v  /home/lee/mysql/conf/:/etc/mysql/ -v /home/lee/mysql/data:/var/lib/mysql -p 3306:3306  -e MYSQL_ROOT_PASSWORD=666666 -d mysql



docker run --net=host --name mysql1 -v  /home/lee/mysql1/conf/:/etc/mysql/ -v /home/lee/mysql1/data:/var/lib/mysql  -e MYSQL_ROOT_PASSWORD=666666 -d mysql:8.0.34
docker run --net=host --name mysql2 -v  /home/lee/mysql2/conf/:/etc/mysql/ -v /home/lee/mysql2/data:/var/lib/mysql  -e MYSQL_ROOT_PASSWORD=666666 -d mysql:8.0.34
docker run --net=host --name mysql3 -v  /home/lee/mysql3/conf/:/etc/mysql/ -v /home/lee/mysql3/data:/var/lib/mysql  -e MYSQL_ROOT_PASSWORD=666666 -d mysql:8.0.34
docker run --net=host --name mysql4 -v  /home/lee/mysql4/conf/:/etc/mysql/ -v /home/lee/mysql4/data:/var/lib/mysql  -e MYSQL_ROOT_PASSWORD=666666 -d mysql:8.0.34