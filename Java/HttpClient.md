[]: http://hc.apache.org/httpcomponents-client-ga/



# 一、快速开始

## 1.1.引入jar

```xml
<!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.7</version>
</dependency>
```

## 1.2.`GET`请求

```java
CloseableHttpClient httpClient = HttpClients.createDefault();
//构建请求参数
ArrayList<NameValuePair> pairs = new ArrayList<>();
pairs.add(new BasicNameValuePair("name", "张三"));
//请求
URIBuilder builder = new URIBuilder("https://www.baidu.com");
builder.addParameters(pairs);
builder.setParameter("age", "19");
//get请求
HttpGet httpGet = new HttpGet(builder.build());
CloseableHttpResponse response = httpClient.execute(httpGet);
//请求成功
if (response != null && response.getStatusLine().getStatusCode() == 200) {
    HttpEntity entity = response.getEntity();
    //响应内容
    InputStream content = entity.getContent();
    System.out.println(content);
}
```



## 1.3.`POST`请求

```java
CloseableHttpClient httpClient = HttpClients.createDefault();
//post请求
HttpPost httpPost = new HttpPost("https://www.baidu.com");
//设置json格式的请求参数
StringEntity jsonEntity = new StringEntity("{}", ContentType.APPLICATION_JSON);
httpPost.setEntity(jsonEntity);
//httpPost.setEntity(new ByteArrayEntity("{}".getBytes()));
CloseableHttpResponse response = httpClient.execute(httpPost);
//请求成功
if (response != null && response.getStatusLine().getStatusCode() == 200) {
    HttpEntity entity = response.getEntity();
    InputStream content = entity.getContent();
    System.out.println(content);
}
```





