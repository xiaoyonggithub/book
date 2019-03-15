## 一、`Tomcat`配置

### 1.1.`maxPostSize `

```xml
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               maxPostSize="0"
               redirectPort="8443" />
```

`maxPostSize="0"`：会导致post可传递的大小限制为0 （在某些版本中会有此问题）。

`maxPostSize="-1"`：表示不限制post参数。