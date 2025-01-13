docker buildx build -f /home/lee/mycat/Dockerfile -t mycat:latest /home/lee/mycat/mycat

docker run \
  --name mycat \
  --net=host \
  -v /home/lee/mycat/mycat/conf:/usr/local/mycat/conf \
  -v /home/lee/mycat/mycat/logs:/usr/local/mycat/logs \
  -d \
  mycat:latest


docker run \
  --name mycat \
  --net=host \
  -v /home/lee/mycat/mycat/conf:/app/conf \
  -v /home/lee/mycat/mycat/logs:/app/logs \
  -d \
  mycat:latest