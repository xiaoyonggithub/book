

#### elasticSearch的应用场景

* 海量数据的分析引擎
* 站内搜索引擎
* 数据仓库





wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.4.0.zip

curl -O http://download.[Oracle](https://www.linuxidc.com/topicnews.aspx?tid=12).com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-x64.tar.gz 



vim config/elasticsearch.yml

http.cors.enable: true

http:cors.allow-origin: "*"

cluster.name: wlai

node.name: master

node.master: true

network.host: 127.0.0.1





elasticsearch -d

npm run start 



ps -ef | grep `pwd`

kill 5531



```json
{
  "name" : "8WY_5_y",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "tDtZWSnZQa2hXmI9did1_A",
  "version" : {
    "number" : "6.4.0",
    "build_flavor" : "default",
    "build_type" : "zip",
    "build_hash" : "595516e",
    "build_date" : "2018-08-17T23:18:47.308994Z",
    "build_snapshot" : false,
    "lucene_version" : "7.4.0",
    "minimum_wire_compatibility_version" : "5.6.0",
    "minimum_index_compatibility_version" : "5.0.0"
  },
  "tagline" : "You Know, for Search"
}
```

mkdir es_slave

cp elasticsearch.tar.gz es_slave

tar -zxvf elasticsearch



cluster.name: wali

node.name: slave1

network.host: 127.0.0.1

http.port: 9201

找到master

discovery.zen.ping.unicast.hosts: ["127.0.0.1"]





基本概念

集群和节点

索引：含有相同属性的文档集合

类型：索引可以定义一个或多个类型，文档必须属于一个类型

文档：文档是可以被基引的基本数据单位

分片：每个所以都有多个分片，每个分片是一个Lucene索引

备份：拷贝一个分片就完成了分片的备份



API的基本格式：http://<ip>:<port>/<索引>/<类型>/<文档id>

常用的HTTP动词：GET/PUT/POST/DELETE

创建索引

* 非结构化的创建 
*  结构化的创建



插入数据

* 指定文档id插入
* 自动产生文档id插入



wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.1.1.zip 

sha1sum elasticsearch-5.1.1.zip   #sha1sum 或 shasum 生成 SHA 摘要信息

unzip elasticsearch-5.1.1.zip 

cd elasticsearch-5.1.1/

