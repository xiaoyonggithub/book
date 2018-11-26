# 一、`setting.xml`

## 1.1.[mirror（镜像）](https://www.cnblogs.com/AlanLee/p/6198413.html)

mirror相当于一个拦截器，它会拦截maven对`remote repository`的相关请求，把请求里的`remote repository`地址，重定向到mirror里配置的地址 

`<mirror>`配置镜像地址,可以配置多个`<mirror>`节点，子节点的含义：

* id :镜像的唯一标识
* mirrorOf :设置镜像匹配的仓库

| `mirrorOf`                           | 描述                                            |
| ------------------------------------ | ----------------------------------------------- |
| `<mirrorOf>*</mirrorOf>`             | 匹配所有的远程仓库，此时pom.xml中定义的仓库失效 |
| `<mirrorOf>external:*</mirrorOf>`    | 匹配所有不在本机上的远程仓库                    |
| ` <mirrorOf>repo1,repo2</mirrorOf> ` | 匹配仓库repo1,repo2                             |
| ` <mirrorOf>*,!repo1</miiroOf> `     | 匹配所有远程仓库，repo1除外                     |

* name :相当于该镜像的描述
* url :镜像的地址

注意：镜像库不是一个分库的概念，分库：当xxx.jar在第一个mirror中不存在时，maven不会在第二个mirror中取查找，也不会在其他的mirror中查找；只有当mirror无法连接的时候，才会去找下一个，作用相当于备份或容灾；

`<mirror>`的查找顺序:根据字母的排序来查找

如有B、A、C顺序的mirror的节点，maven会在A中查找，若A无法连接才会去B查找

```xml
<mirrors>
     <mirror>
      <id>maven.oschina.net</id>
      <name>maven mirror in China</name>
      <url>http://maven.oschina.net/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
```

此时`<mirrorOf>`为`central`，表示配置的是中央仓库的镜像，任何对中央仓库的请求都会跳转至该镜像；

镜像一般与私服结合使用，用于代理其他的仓库，因为私服可以代理任意的外部公共库（包含中央仓库），可以将配置集中到私服上，这样对用户来说使用一个私服就相当于使用所有需要的外部库，简化maven本身的配置，此时私服就是所有仓库的镜像

```xml
<!--配置私服镜像-->
<mirrors> 
    <mirror>  
        <id>nexus</id>  
        <name>internal nexus repository</name>  
        <url>http://183.238.2.182:8081/nexus/content/groups/public/</url>  
        <mirrorOf>*</mirrorOf>  
    </mirror>  
</mirrors>
```









## 1.2.profile

`<profile>`可以配置不同的构建环境，可根据需要激活需要的环境

## 1.2.1.profile定义的位置

* 定义在pom.xml中，只针对对应的项目有效
* 定义在用户的${user.home}/setting.xml中，针对该用户下的项目有效
* 定义在conf/setting.xml中，针对所有项目有效

## 1.2.2.激活方式

* 方式一：

```xml
<profiles> 
    <profile> 
        <id>profileTest1</id> 
        <properties> 
            <hello>world</hello> 
        </properties> 
        <!-- 激活 -->
        <activation> 
            <!-- 没有指定其他profile为激活状态时，该profile就默认会被激活 -->
            <activeByDefault>true</activeByDefault> 
        </activation> 
    </profile> 

    <profile> 
        <id>profileTest2</id> 
        <properties> 
            <hello>andy</hello> 
        </properties> 
    </profile> 
</profiles> 
```

* 方式二：

```xml
<activeProfiles> 
    <activeProfile>profileTest1</activeProfile> 
    <activeProfile>profileTest2</activeProfile> 
</activeProfiles> 
```

激活了多个`<profile>`时，是根据`<profile>`定义的先后顺序进行覆盖取值，后面的覆盖前面的

* 方式三：

使用`-P`参数激活`<profile>`

```
mvn package –P profileTest1 
```

当使用activeByDefault或settings.xml中定义了处于激活的profile,但是当我们在进行某些操作的时候又不想它处于激活状态 

```xml
-- -P !profile表示在当前操作中该profile将不处于激活状态
mvn package –P !profileTest1 
```

查看当前激活的`profile`

```xml
mvn help:active-profiles
```

## 1.3.`overlays`

`overlays`将多个war包归并为一个war

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.sharp</groupId>
  <artifactId>mvc1</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>war</packaging>
  <name>mvc1</name>
  <dependencies>
  	<dependency>
  		<groupId>com.sharp</groupId>
  		<artifactId>mvc4</artifactId>
  		<version>0.0.1-SNAPSHOT</version>
  		<type>war</type>
  		<scope>runtime</scope>
  	</dependency>
  </dependencies>
  
  <build>
	<plugins>
		<plugin>
			<artifactId>maven-war-plugin</artifactId>
			<version>2.1.1</version>
			<configuration>
				<warSourceDirectory>src/main/WebContent</warSourceDirectory>
				<overlays>
					<overlay>
						<groupId>com.sharp</groupId>
						<artifactId>mvc4</artifactId>
						<excludes>
							<exclude>WEB-INF/lib/*</exclude>
							<exclude>WEB-INF/web.xml</exclude>
						</excludes>
					</overlay>
				</overlays>
			</configuration>
		</plugin>
	</plugins>
  </build>
</project>

```

mvc1为主项目，存在同名文件时会使用主项目（mvc1  ）的配置文件

## 1.4.阿里云镜像

```
<mirror>
    <id>nexus-aliyun</id>
	<mirrorOf>central</mirrorOf>
	<name>Nexus aliyun</name>
	<url>http://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
```

## 1.5.本地仓库

```
<localRepository>D:\maven_2017\</localRepository>
```

## 1.6.`properties(Maven属性)`

`Maven`总共有`6`类属性，内置属性、`POM`属性、自定义属性、`Settings`属性、`java`系统属性和环境变量属性

## 1.6.1.内置属性

`${basedir} `表示项目跟目录,即包含`pom.xml`文件的目录
`${version}` 表示项目版本

## 1.6.2.`POM`属性

用户可以使用该类属性引用`POM`文件中对应元素的值

- `${project.build.sourceDirectory}:`项目的主源码目录，默认为`src/main/java/`
- `${project.build.testSourceDirectory}:`项目的测试源码目录，默认为`src/test/java/`
- `${project.build.directory} ：` 项目构建输出目录，默认为`target/`
- `${project.outputDirectory} :` 项目主代码编译输出目录，默认为`target/classes/`
- `${project.testOutputDirectory}：`项目测试主代码输出目录，默认为`target/testclasses/`
- `${project.groupId}：`项目的`groupId`
- `${project.artifactId}：`项目的`artifactId`　
- `${project.version}：`项目的`version`,与`${version} `等价
- `${project.build.finalName}：`项目打包输出文件的名称，默认为`${project.artifactId}-${project.version}`

## 1.6.3.自定义属性

子模块可以使用父`POM.XML`中定义的属性

```xml
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
     <!-- 定义 spring版本号 -->
    <spring.version>4.0.2.RELEASE</spring.version>
    <junit.version>4.7</junit.version>
</properties>
```

## 1.6.4.`Settings`属性

与`POM`属性同理，用户使用以`settings.`开头的属性引用`settings.xml`文件中的`XML`元素的值。

## 1.6.5.`Java`系统属性

所有`java`系统属性都可以用`Maven`属性引用

- `${user.home}`指向了用户目录

## 1.6.6.环境变量属性

所有环境变量属性都可以使用以`env. `开头的`Maven`属性引用

- `${env.JAVA_HOME}`:`JAVA_HOME`环境变量的的值

## 1.7.依赖标签

`<modelVersion>:`指定了`POM`模型的版本，对于`Maven2`及`Maven3`来说，它只能是`4.0.0`

## 1.7.1.依赖类型

`<type>：`依赖的类型，对应于项目坐标定义的`packaging`，如`jar、war`

`<optional>:`可选依赖，标记依赖是否为可选

配置可选依赖的原因

- 节约磁盘、内存等空间；

- 避免`license`许可问题；

- 避免类路径问题

```xml
<dependency>  
  <groupId>sample.ProjectB</groupId>  
  <artifactId>Project-B</artifactId>  
  <version>1.0</version>  
  <scope>compile</scope>  
  <optional>true</optional>
</dependency>  
<!--假设以上配置是项目A的配置，即：Project-A -> Project-B。在编译项目A时，是可以正常通过的。 -->

<!--如果有一个新的项目X依赖A，即：Project-X -> Project-A。此时项目X就不会依赖项目B了。如果项目X用到了涉及项目B的功能，那么就需要在pom.xml中重新配置对项目B的依赖。-->
```

`<exclusions>:`用来排除传递性依赖

```xml
<dependency>  
  <groupId>sample.ProjectB</groupId>  
  <artifactId>Project-B</artifactId>  
  <version>1.0</version>  
  <scope>compile</scope>  
  <exclusions>  
    <exclusion>  <!-- declare the exclusion here -->  
      <groupId>sample.ProjectC</groupId>  
      <artifactId>Project-C</artifactId>  
    </exclusion>  
  </exclusions>   
</dependency>  
<!--当一个项目A依赖项目B，而项目B同时依赖项目C，如果项目A中因为各种原因不想引用项目C，在配置项目B的依赖时，可以排除对C的依赖 -->


<!--多依赖过滤，过滤掉所有的依赖-->
<dependency>
	<groupId>org.apache.hbase</groupId>
	<artifactId>hbase</artifactId>
	<version>0.94.17</version>
	<exclusions>
		<exclusion>
			<groupId>*</groupId>
			<artifactId>*</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```

## 1.7.2.`scope`依赖的范围

依赖范围用来控制依赖与这三种`classpath(编译classpath、测试classpath、运行classpath)`的关系

- `test:`参与测试相关的工作，包括测试代码的编译和执行，不会被打包`(只对于测试classpath有效)`
- `compile:`依赖项目需要参与当前项目的编译，还有后续的测试，运行周期`(对于编译、测试、运行三种classpath都有效)`
- `runntime:`表示被依赖项目无需参与项目的编译，不过后期的测试和运行周期需要其参与`(对于测试和运行classpath有效，但是编译主代码时无效)`
- `provided:`打包的时候可以不用打包进去`(对于编译和测试的classpath有效，但是在运行时无效)`
- `system:`系统依赖范围`(provided依赖范围完全一致)`,使用`system`范围的依赖时必须通过`systemPath`元素显式的指定依赖文件的路径

```xml
<dependency>
	<groupId>javax.sql</groupId>
	<artifactId>jdbc-stdext</artifactId>
	<version>2.0</version>
	<scope>system</scope>
	<systemPath>${java.home}/lib/rt.jar</systemPath><!--依赖环境变量-->
</dependency>

```

> 此类依赖不是通过`Maven`仓库解析，而且往往与本机系统绑定，可能造成构建的不可移植，因此需要谨慎使用

![1537183398707](E:\typora\images\1537183398707.png)

## 1.7.3.scope的依赖传递

![1537183416498](E:\typora\images\1537183416498.png)

## 1.7.4.依赖调解

传递性依赖是从那条依赖路径引入的，其引入原则

- 路径最近者优先`(第一原则)`
- 第一声明者优先`(第二原则)`

## 1.7.5.归并依赖（归类依赖）

有时我们引入的依赖，他们都来自统一个项目的不同模块，它们的版本号是一致的

```xml
<project>  
    <modelVersion>4.0.0</modelVersion>  
    <groupId>com.juven.mvnbook.account</groupId>  
    <artifactId>accout-email</artifactId>  
    <version>1.0.0-SNAPSHOT</version>  
    <properties>  
        <springframework.version>1.5.6</springframework.version>  
    </properties>  
    <dependencies>  
        <dependency>  
            <groupId>org.springframework</groupId>  
            <artifactId>spring-core</artifactId>  
            <version>${springframework.version}</version>  
        </dependency>   
        <dependency>  
            <groupId>org.springframework</groupId>  
            <artifactId>spring-beans</artifactId>  
            <version>${springframework.version}</version>  
        </dependency>         
    </dependencies>  
</project>
```

## 1.8.配置`http`代理

若需要通过安全认证的代理访问因特网，这是需要配置HTTP代理才能正确访问外部仓库

```xml
<proxies>  
    <proxy>  
        <id>my-proxy</id>  
        <active>true</active>  
        <protocol>http</protocol>  
        <host>218.14.227.197</host>  
        <port>3128</port>  
        <!--  
        <username>***</username>  
        <password>***</password>  
        <nonProxyHosts>  
          repository.mycom.com|*.google.com  
        </nonProxyHosts>  
      -->  
    </proxy>  
</proxies> 
```

- 若配置了多个`<proxy>`元素，默认第一个生效
- `<active>`为`true`表示激活
- 若代理服务器需要认证，则需要配置`<username>`和`<password>`
- `<nonProxyHost>`元素配置哪些主机不需要代理，可以使用`|`来分隔多个主机
  - 支持通配符方式，`*.google.com`表示所有以`google.com`结尾的域名访问都不要通过代理



# 二、配置测试和生产环境切换Profile

## 2.1.定义各个profile

```xml
<profiles>
    <profile>
        <!--设置环境的id-->
        <id>dev</id>
        <properties>
            <!--自定义标签-->
            <env>dev</env>
        </properties>
        <activation>
            <!--设置默认激活的profile-->
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>prd</id>
        <properties>
            <env>prd</env>
        </properties>
    </profile>
</profiles>
```

## 2.2.设置资源的加载

若不同`<resource>`下有相同的文件，加载最先出现的文件，后面出现的就不加载了

```xml
<build>
    <finalName>test</finalName>
    <resources>
        <!--加载指定profile下的文件到classes根目录下,同名文件先加载-->
        <resource>
            <directory>src/main/resources/${env}</directory>
            <!--指定包含文件的类型-->
            <!--<includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>-->
        </resource>
        <!--把除dev文件夹和prd文件夹的其他所有文件打包到classes根目录下 -->
        <resource>
            <directory>src/main/resources</directory>
            <!--排除dev和prd文件夹(包含子目录下)的所有文件-->
            <excludes>
                <exclude>dev/**/*</exclude>
                <exclude>prd/**/*</exclude>
            </excludes>
        </resource>
        <!--把java目录下的*.xml和*.properties文件打包到classes根目录下-->
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>*.xml</include>
                <include>*.properties</include>
            </includes>
        </resource>
    </resources>
</build>
```

## 2.3.执行打包

```
mvn clean package -P prd  #执行profile = prd
```

## 2.4.设置不执行测试用例

## 2.4.1.在执行命令时设置参数跳过

跳过单位测试用例的运行，但是会将测试用例类生成相应的class文件至target/test-class中

```xml
-DskipTests
```

跳过单元测试用例的编译和运行

```xml
mvn package -Dmaven.test.skip=true  
```

## 2.5.设置加载lib目录下的jar

maven项目有时会引用lib目录下的jar,此时使用mvn package打包lib目录下的jar就不能同步过去

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
        <source>1.7</source>
        <target>1.7</target>
        <encoding>UTF-8</encoding>
        <compilerArguments>
            <verbose />
            <bootclasspath>${env.JAVA_HOME}/jre/lib/rt.jar</bootclasspath>
            <extdirs>${project.basedir}/src/main/webapp/WEB-INF/lib</extdirs>
        </compilerArguments>
    </configuration>
</plugin>
```

在3.1版本以后maven-compiler-plugin将compilerArguments定为过时，此时应使用如下配置：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
        <source>1.7</source>
        <target>1.7</target>
        <encoding>UTF-8</encoding>
        <compilerArgs> 
            <arg>-verbose</arg>
            <arg>-Xlint:unchecked</arg>
            <arg>-Xlint:deprecation</arg>
            <arg>-bootclasspath</arg>
            <arg>${env.JAVA_HOME}/jre/lib/rt.jar</arg>
            <arg>-extdirs</arg> 
            <arg>${project.basedir}/src/main/webapp/WEB-INF/lib</arg>
        </compilerArgs> 
    </configuration>
</plugin>
```

# 三、maven项目目录结构分析

* `${project.basedir}`表示项目的根目录

## 3.1.Maven项目目录的约定

* `pom.xml`文件必须在项目根目录下
* `${project.bassedir}/src/main/java`：存放项目java文件
* `${project.bassedir}/src/main/resource`:存放项目资源文件
* `${project.bassedir}/src/test/java`:存放测试的java文件
* `${project.bassedir}/src/test/resource`:存放测试的资源文件
* `${project.bassedir}/target`:项目输出位置

# 四、常用设置

## 4.1.统一设置jar的版本

```xml
<properties>
    <maven.compiler.testTarget>1.8</maven.compiler.testTarget>
</properties>
```

##4.2.`maven`命令

1. `mvn clean` 清除`target`目录下的编译内容，会删除`taget/`目录
2. `mvn complie` 编译项目，将主代码编译至`target/classes`目录
3. `mvn package` 打包项目
4. `mvn test-complie` 编译测试
5. `mvn test`测试
6. `mvn site` 生成站点目录
7. `mvn install`  在本地仓库安装`jar`
8. `mvn jar:jar`只打`jar`包
9. `mvn -Dtest package`只打包不测试
10. `mvn dependency:tree`查看依赖树
11. `mvn dependency:list`列出项目最终解析到的依赖列表
12. `mvn help:effective-pom`查看项目完整的pom.xml文件，包括系统默认设置以及用户自定义设置
13. `mvn dependency:analyze`依赖分析命令

```
//mvn dependency:analyze执行的结果由两部分组成
//项目中使用到的，没有显式声明的依赖
 Used undeclared dependencies found:
    org.springframework.cloud:spring-cloud-netflix-eureka-server:jar:1.3.1.RELEASE:compile
    org.springframework.boot:spring-boot-test:jar:2.0.0.RELEASE:test
    org.springframework:spring-test:jar:5.0.4.RELEASE:test
    org.springframework.boot:spring-boot-autoconfigure:jar:2.0.0.RELEASE:compile
    junit:junit:jar:4.12:test
    org.springframework.boot:spring-boot:jar:2.0.0.RELEASE:compile
 //项目中没有使用的，但是显式声明了的依赖   
 Unused declared dependencies found:
    org.springframework.boot:spring-boot-starter:jar:2.0.0.RELEASE:compile
    org.springframework.boot:spring-boot-starter-test:jar:2.0.0.RELEASE:test
    org.springframework.cloud:spring-cloud-starter-eureka-server:jar:1.4.3.RELEASE:compile
```

14.`mvn dependency:tree`查看依赖树

```xml
com.yinhai:jyrc:war:0.0.1-SNAPSHOT
+- com.alibaba:druid:jar:1.0.17:compile
|  +- com.alibaba:jconsole:jar:1.8.0:system
|  \- com.alibaba:tools:jar:1.8.0:system
+- com.yinhai:ta3-core-common-ta3:jar:4.0.1-SNAPSHOT:compile
|  +- com.yinhai:ta3-core-common-api:jar:4.0.1-SNAPSHOT:compile
|  |  +- org.logback-extensions:logback-ext-spring:jar:0.1.4:compile
|  |  +- org.slf4j:jcl-over-slf4j:jar:1.7.18:compile
```

15.`mvn dependency:tree -Dverbose -Dincludes=<groupId>:<artifactId>`查看指定jar的依赖树

- `mvn dependency:tree -Dverbose -Dincludes=org.slf4j`查看所有`org.slf4j`组下的依赖
- `mvn dependency:tree -Dverbose -Dincludes=com.alibaba:druid`查看某一个jar的依赖



## 4.3.依赖关系说明

```
com.yinhai:jyrc:war:0.0.1-SNAPSHOT
+- com.alibaba:druid:jar:1.0.17:compile
|  +- com.alibaba:jconsole:jar:1.8.0:system
|  \- com.alibaba:tools:jar:1.8.0:system
+- com.yinhai:ta3-core-common-ta3:jar:4.0.1-SNAPSHOT:compile
|  +- com.yinhai:ta3-core-common-api:jar:4.0.1-SNAPSHOT:compile
|  |  +- org.logback-extensions:logback-ext-spring:jar:0.1.4:compile
|  |  +- org.slf4j:jcl-over-slf4j:jar:1.7.18:compile
```

`+-`符号表示该包后面还有其它依赖包
`\-`表示该包后面不再依赖其它`jar`包
最后写着`compile`的就是编译成功的
最后写着`omitted for duplicate`的就是有`jar`包被重复依赖了，但是`jar`包的版本是一样的。
最后写着`omitted for conflict with xxxx`的，说明和别的`jar`包版本冲突了，而该行的`jar`包不会被引入

## 4.4.多模块`packaging`

`Maven`项目的多模块`（multi-modules）`结构，由一个父模块和若干个子模块构成，搭建多模块项目，必须要有一个`packaging`为`pom`的根目录，同时以`<modules>`给出所有的子模块

```xml
<!--父模块的POM-->
<packaging>pom</packaging>  

<!--模块名是这些子项目的相对目录-->  
<modules>  
  <module>my-frontend-project</module>  
  <module>my-service-project</module>  
  <module>my-backend-project</module>  
</modules>  
```

在列出模块时，不需要自己考虑模块间依赖关系，即POM给出的模块排序并不重要。`Maven`将对模块进行拓扑排序，使得依赖关系始终在依赖模块之前构建。
聚合（多模块）则是为了方便一组项目进行统一的操作而作为一个大的整体。

## 4.5.`Parent `继承的关系

使得子`POM`可以获得 `parent `中的各项配置

```xml
//可继承的元素
groupId
version
description
url
inceptionYear
organization
licenses
developers
contributors
mailingLists
scm
issueManagement
ciManagement
properties
dependencyManagement
dependencies
repositories
pluginRepositories
build 
	plugin executions with matching ids
	plugin configuration
	etc.
reporting
profiles	
//不能继承的元素
artifactId
name
prerequisites
```

```xml
//relativePath元素不是必须的，指定后会优先从指定的位置查找父pom
<parent>
   <groupId>org.codehaus.mojo</groupId>
   <artifactId>my-parent</artifactId>
   <version>2.0</version>
   <relativePath>../my-parent</relativePath>
 </parent>

 <artifactId>my-project</artifactId>
```

父`POM`是为了抽取统一的配置信息和依赖版本控制，方便子`POM`直接引用，简化子`POM`的配置

## 4.6.`MAVEN`插件

`compiler`插件默认只支持编译`Java 1.3`

```xml
 <plugin>
	 <groupId>org.apache.maven.plugins</groupId>
     <artfactId>maven-compiler-plugin</artifactId>
     <!--故需指定编译的版本-->
     <configuration>
         <source>1.5</source>
         <target>1.5</target>
     </configuration>
 </plugin>

```

## 4.7.`MAVEN`的打包

带有`main`方法的类在打包完成后，main方法的信息不会添加到`manifest`中(打开`jar`文件中共的`META-INF/MANIFEST.MF`文件，将无法看到`Main-Class`一行)。为了生成可执行的`jar`文件，需要借助`maven-shade-plugin`

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>1.2.1</version>
    <excutions>
        <excution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.up366.feishu.feishu_v3.MainClass</mainClass>
                     </transformer>
                </transformers>
            </configuration>
        </excution>
    </excutions>
</plugin>

```

## 4.8.仓库

## 4.8.1.仓库的分类

![](E:\typora\images\5811881-bfb7c7534ef435aa.png)



## 4.8.2.远程仓库的配置

> 在setting.xml中配置仓库地址，多个就配置多个

```xml
<repositories>
    <repository>
        <!--唯一标识一个仓库-->
        <id>jboss</id>
        <name>JBoss Repository</name>
        <!--仓库地址-->
        <url>http://repository.jboss.com/maven2/</url>
        <!--表示开启JBoss仓库的发布版本下载支持-->
        <releases>
            <enabled>true</enabled>
            <!--配置Maven从远处仓库检查更新的频率，默认值daily -->
            <updatePolicy>daily</updatePolicy>
        </releases>
        <!--表示关闭JBoss仓库的快照版本的下载支持-->
        <snapshots>
            <enabled>false</enabled>
            <checksumPolicy>warn</checksumPolicy>
        </snapshots>
        <layout>default</layout>
    </repository>
</repositories>
```

```xml
<snapshots>
    <enabled>true</enabled>
    <updatePolicy>daily</updatePolicy>
    <checksumPolicy>ignore</checksumPolicy>
</snapshots>
```

`updatePolicy`用来配置`Maven`从远程仓库检查更新的频率

- `daily`，默认值，表示`Maven`每天检查一次
- `never`，从不检查更新
- `always`，每次构建都检查更新
- `interval: X`，每隔`X`分钟检查一次更新（`X`为任意数整数）

`checksumPolicy`用来配置`Maven`检查校验和文件的策略，当构件被部到`Maven`仓库中时，会同时部署对应的校验和文件。当下载构件的时候，`Maven`会校验和文件，如果校验和验证失败，`Maven`会在执行构建时输出警告信息

- `warn`，默认值， `Maven`会在执行构建时输出警告信息
- `fail`，`Maven`遇到校验和错误就让构建失败
- `ignore`，使`Maven`完全忽略校验和错误

> 配置激活那个仓库

```xml
<activeProfiles>
    <!-- 激活myRepository1 -->
    <activeProfile>myRepository1</activeProfile>
    <!-- 激活myRepository2 -->
    <activeProfile>myRepository2</activeProfile>
</activeProfiles>
```

> 在项目的pom.xml文件中配置仓库

```xml

```





## 4.8.3.远程仓库的认证

```xml
<servers>
    <server>
	    <!--id必须与POM中需要认证的repository元素的id完全一致-->
        <id>my-proj</id>
        <username>repo-user</username>
        <password>repo-pwd</password>
    </server>
</servers>	
```

配置认证信息必须配置在`settings.xml`文件中

## 4.8.4.部署构件至远程仓库

```xml
<distributionManagement>
    <repository>
        <id>releases</id>
        <name>public</name>
        <url>http://59.50.95.66:8081/nexus/content/repositories/releases</url>
    </repository>
    <snapshotRepository>
        <id>snapshots</id>
        <name>Snapshots</name>
        <url>http://59.50.95.66:8081/nexus/content/repositories/snapshots</url>
    </snapshotRepository>
</distributionManagement>
```

- `<repository>`表示发布稳定版本构建的仓库
- `<snapshotRepository>`表示发布快照版本的仓库
- 配置完成后运行`mvn clean deploy`，就会将项目构建输出的构件部署到配置对应的远程仓库



## 4.10.生命周期

`Maven`拥有三套相互独立的生命周期，它们分别为`clean`、`default`和`sit`。

- `clean`生命周期的目的是清理项目
- `default`生命周期的目的是构建项目
- `site`生命周期的目的是建立项目站点

## 4.10.1.`clean`生命周期

`clean`生命周期的目的是清理项目

- `per-clean：`执行一些清理前需要完成的工作
- `clean:`清理上一次构建生成的文件
- `post-clean:`执行一些清理后需要完成的工作

## 4.10.2.`default`生命周期

`default`生命周期定义了真正构建时所需要执行的所有步骤，它是所有生命周期中最核心的部分

- `validate`
- `initialize`

------

- `generate-sources`
- `process-sources`

------

- `generate-resources: `处理项目主资源文件。是对`src/main/resources`目录的内容进行变量替换等工作后，复制到项目输出的主`classpath`目录中
- `process-resources`
- `compile:`编译项目的主代码。是编译`src/main/java`目录下的`java`文件至项目输出的主`classpath`目录中。
- `process-classes`

------

- `generate-test-sources`
- `process-test-sources:`处理项目测试资源文件。一般来说，是对`src/test/resources`目录的内容进行变量替换工作后，复制到项目输出的测试`classpath`目录下

------

- `generate-test-resources`
- `process-test-resources`

------

- `test-compile:`编译项目的测试代码。是编译`src/test/java`目录下的`java`文件至项目输出的测试`classpath`目录中。
- `process-test-classes`
- `test：`使用单元测试框架运行测试，测试代码不会被打包或者部署。

------

- `prepare-package`
- `package:`接受编译好的代码，打包成可发布的格式，如`JAR`。

------

- `pre-integration-test`
- `integration-test`
- `post-integration-test`

------

- `verify`
- `install:`将安装包安装到`Maven`仓库，供本地其他的`Maven`项目使用。
- `deploy:`将最终的包复制到远程仓库，供其他开发人员和`Maven`项目使用。

## 4.10.3.`site`生命周期

`site`生命周期的目的是建立和发布项目站点，`Maven`能够基于`POM`所包含的信息，自动的生成一个友好的站点

- `pre-site:`执行一些在生成项目站点之前需要完成的工作。
- `site:`生成项目站点文档。
- `post-site:`执行一些在生成项目站点之后需要完成的工作。
- `site-deploy:`将生成的项目站点发布到服务器上



# 十、异常处理

## 10.1.mvn clean 删除target目录异常

错误提示：

```xml
Failed to execute goal org.apache.maven.plugins:maven-clean-plugin:3.0.0:clean (default-clean) on project ssm: Failed to clean project: Failed to delete
 E:\IDEA\ssm\target -> [Help 1]
```

原因分析：

targe目录被占用，可通过“资源监视器”查看目录被那个进程使用

![1532576120363](E:\typora\images\1532576120363.png)

解决方法：

将占用目录的进程结束掉就行了

## 10.2.`*.lastUpdated`

`*.lastUpdated`表示文件下载不成功，若该文件存在，下次下载可能不成功

- Window清除`lastUpdated`文件

```bash
@echo off
rem 这里写你的仓库路径
set REPOSITORY_PATH=D:\maven_2017
rem 正在搜索...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    del /s /q %%i
)
rem 搜索完毕
pause
```

注意：路径中不能有空格

- Linux清除`lastUpdated`文件

```bash
# 这里写你的仓库路径
REPOSITORY_PATH=~/Documents/tools/apache-maven-3.0.3/repository
echo 正在搜索...
find $REPOSITORY_PATH -name "*lastUpdated*" | xargs rm -fr
echo 搜索完
```

1. 若把仓库的地址配置在`<mirror>`节点下，此时若生成了`.lastUpdated`文件，下次Maven不会去获取最新的资源；

```xml
<mirror>
    <!-- id、mirrorOf、name可以随意填写， -->
    <id>nexus-aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Nexus aliyun</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
```

1. 若把仓库地址配置在`<profile>`节点下，


## 10.3.`maven`编译时错误：无效的目标发行版
`MAVEN`依赖的`JDK`版本与项目的`JDK`不一致

1. 修改`POM`文件的编译插件依赖的`JDK`
```
<plugin>  
   <artifactId>maven-compiler-plugin</artifactId>  
   <version>3.1</version>  
   <configuration>  
     <source>1.7</source>  
     <target>1.7</target>  
   </configuration>  
</plugin>  
```
`IDEA`中要注意`Runner`的版本一致

![1537183015217](E:\typora\images\1537183015217.png)

2. 修改`MAVEN`依赖`JDK`与项目的`JDK`一致
```xml
mvn -v 
修改JAVA_HOME指向的JDK
```
3. 修改`MAVEN`全局依赖的`JDK`,即修改`setting.xml`文件
```xml
<profiles>     
      <profile>       
           <id>jdk-1.6</id>       
           <activation>       
               <activeByDefault>true</activeByDefault>       
               <jdk>1.6</jdk>       
           </activation>       
           <properties>       
               <maven.compiler.source>1.6</maven.compiler.source>       
               <maven.compiler.target>1.6</maven.compiler.target>       
               <maven.compiler.compilerVersion>1.6</maven.compiler.compilerVersion>       
           </properties>       
   </profile>      
</profiles>   
```

## 10.4.`IDEA`中`MAVEN`的依赖出现红色波浪线
1. 去除标红的依赖，重新导入`（Reimport）`后，再加上标红的依赖，重新导入。
2. 删除本地仓库该`jar`的依赖，强制`maven`重新下载该`jar`包



## 十二、参考资料
[Maven——生命周期与插件](http://www.cnblogs.com/wangwei-beijing/p/6535081.html)



```xml
<profiles>
    <profile>
        <!-- 本地开发环境 -->
        <id>dev</id>
        <properties>
            <profiles.active>dev</profiles.active>
        </properties>
        <activation>
            <!-- 设置默认激活这个配置 -->
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <!-- 发布环境 -->
        <id>release</id>
        <properties>
            <profiles.active>release</profiles.active>
        </properties>
    </profile>
    <profile>
        <!-- 测试环境 -->
        <id>beta</id>
        <properties>
            <profiles.active>beta</profiles.active>
        </properties>
    </profile>
</profiles> 
```

`application-环境名称.properties `

# maven项目依赖本地lib下的jar

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
        <source>1.7</source>
        <target>1.7</target>
        <encoding>UTF-8</encoding>
        <compilerArguments>
            <verbose />
            <bootclasspath>${env.JAVA_HOME}/jre/lib/rt.jar</bootclasspath>
            <extdirs>${project.basedir}/src/main/webapp/WEB-INF/lib</extdirs>
        </compilerArguments>
    </configuration>
</plugin>
```

不过在3.1版本以后maven-compiler-plugin将compilerArguments定为过时了,其中表示javac平时用空格隔开的的每一个参数

* 其中${project.basedir}一定要写,不然会出现“在windows”下可以正常编译，在Linux服务器上就“有可能”出现编译找不到jar包的错误 

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
        <source>1.7</source>
        <target>1.7</target>
        <encoding>UTF-8</encoding>
        <compilerArgs> 
            <arg>-verbose</arg>
            <arg>-Xlint:unchecked</arg>
            <arg>-Xlint:deprecation</arg>
            <arg>-bootclasspath</arg>
            <arg>${env.JAVA_HOME}/jre/lib/rt.jar</arg>
            <arg>-extdirs</arg> 
            <arg>${project.basedir}/src/main/webapp/WEB-INF/lib</arg>
        </compilerArgs> 
    </configuration>
</plugin>
```

pom.xml所在的目录应为项目的根目录，假设该目录为${proj-dir}，那么Maven有以下假设：

${proj-dir}/src/main/java —— 存放项目的.java文件。

${proj-dir}/src/main/resources —— 存放项目资源文件，如spring, hibernate配置文件。

${proj-dir}/src/test/jave —— 存放所有测试.java文件，如JUnit测试类。

${proj-dir}/src/test/resources —— 测试资源文件。

${proj-dir}/target —— 项目输出位置

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-dependencies</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}/lib</outputDirectory>
                <overWriteReleases>false</overWriteReleases>
                <overWriteSnapshots>false</overWriteSnapshots>
                <overWriteIfNewer>true</overWriteIfNewer>
            </configuration>
        </execution>
    </executions>
</plugin>
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>lib/</classpathPrefix>
                <mainClass>theMainClass</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```



