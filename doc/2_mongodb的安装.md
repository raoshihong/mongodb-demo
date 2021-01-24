1.下载

[https://www.mongodb.com/try/download/community](https://www.mongodb.com/try/download/community)

2.linux下的安装

解压缩上面下载的安装包

```plain
tar -xvf mongodb-linux-x86_64-rhel70-4.4.3.tgz
```
移动到指定到目录下

```plain
mv mongodb-linux-x86_64-rhel70-4.4.3 /usr/local/mongodb
```
创建存放数据的目录和日志的目录

```plain
# 数据存储目录
mkdir -p /data/mongodb/single/data/db
#日志存储目录
mkdir -p /data/mongodb/single/log
```
新建配置文件

```plain
vim /data/mongodb/single/mongod.conf
```
配置文件内容如下：注意文件采用yaml的方式，所以需要格式化下

```plain
systemLog:
#MongoDB发送所有日志输出的目标指定文件
destination: file
#mongod或mongs应向其发送所有诊断日志记录信息的日志文件的路径
path: "/data/mongodb/single/log/mongod.log"
#当mongos或mongod实例重新启动时,mongos或mongod会将新条目附加到现有日志文件的末尾
logAppend: true
storage:
#mongod实例存储其数据的目录.storage.dbPath设置仅适用于mongod
dbPath: "/data/mongodb/single/data/db"
journal:
#启用或禁用持久性日志以确保数据文件保持有效和可恢复
enabled: true
processManagement:
#启用在后台运行mongos或mongod进程的守护进程模式
fork: true
net:
#服务实例绑定的IP,默认是localhost
bindIp: localhost,192.168.8.101
#bindIp
#绑定的端口,默认是27017
port: 27017
```
启动mongdb

```plain
/usr/local/mongodb/bin/mongod -f /data/mongodb/single/mongod.conf
```
![图片](https://uploader.shimo.im/f/wlh6DfnBE19Y25D8.png!thumbnail?fileGuid=jqYjdYqCyRQcyKTp)

查看mongodb是否成功启动

```plain
ps -ef | grep mongod
```
![图片](https://uploader.shimo.im/f/MBghrCicpu5AlIuA.png!thumbnail?fileGuid=jqYjdYqCyRQcyKTp)

或者通过连接mongodb来检查是否成功启动

![图片](https://uploader.shimo.im/f/J78TFm56YU3lxfbf.png!thumbnail?fileGuid=jqYjdYqCyRQcyKTp)

```plain
 /usr/local/mongodb/bin/mongo
```
或者指定host
```plain
/usr/local/mongodb/bin/mongo --host=192.168.8.101 --port=27017
```
![图片](https://uploader.shimo.im/f/EhSCMFOmERkk6W9t.png!thumbnail?fileGuid=jqYjdYqCyRQcyKTp)

或者采用mongodb的可视化工具compass来远程连接,[https://www.mongodb.com/try/download/compass?jmp=docs](https://www.mongodb.com/try/download/compass?jmp=docs)

![图片](https://uploader.shimo.im/f/y9efU3R0FMnGBO2a.png!thumbnail?fileGuid=jqYjdYqCyRQcyKTp)

如果无法远程连接，则可能是防火墙的问题，需要关闭防火墙





