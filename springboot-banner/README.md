# docker build
```shell script
docker build -t banner:1 .
```

# docker run
示例
```shell script
docker run -itd --rm -v /etc/hosts:/etc/hosts -p 10083:10083 -p 5005:5005 banner:1
```
