# 介绍
本项目主要使用了 Sping Boot 、 Jersey 、JWT 来构建 Resetful 风格的服务端。

## 技术选型

|名称|
|---|
|Spring Boot 最新版本|
|Jersey,使用 Spring Boot 提供的 starter |
|Mongo,使用 spring-boot-starter-data-mongodb|
|Redis,整合了 redisson、spting-boot-starter-data-redis|
|Mybats、通用Maper、及分页插件|


## 环境准备

* 启动前请确保启动了 MongoDB、Redis、Mysql
* 数据库相关端口、名字、密码，请到 application.properties 中配置

## 启动方式

* 通过 idea 直接运行main方法即可

* 在 example-app 目录下运行，还有些问题
```
mvn spring-boot:run
```

## Mybatis 

* 通用 Mapper 文档 https://github.com/abel533/Mapper
* Myabtis 分页插件 文档 https://github.com/pagehelper/Mybatis-PageHelper
* test 方法见UserBeanMapperTest ，生成 mapper 
* mybatis 代码生成，通过 maven 插件

## 已经实现的自定义注解

* @Cache 基于 Etag 实现的缓存注解
* @RequireLimit(count = 3,time = 60000)  接口访问频率限制注解


## JWT 认证流程

### 用户注册

```
curl --request POST \
  --url http://localhost:8080/authentication/register \
  --header 'cache-control: no-cache' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --header 'postman-token: a1f1c08e-f2a5-2f18-47a3-cd7b34e24eae' \
  --data 'username=test&password=123456'
```

或者在 UserRepositoryTest 方法运行 testAddUser 方法

### 用户登陆获取 token

* 带上用户名和密码访问 authentication
```
curl --request POST \
  --url http://localhost:8080/authentication \
  --header 'cache-control: no-cache' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data 'username=binglin&password=123456'
```

### 访问需要认证的资源

* 在Headers 中添加 `Authorization Bearer "${token}"
```
curl --request POST \
  --url http://localhost:8080/user \
  --header 'authorization: bearer  eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKZXJzZXktU2VjdXJpdHktQmFzaWMiLCJzdWIiOiJiaW5nbGluIzIwMDAwIiwiYXVkIjoidXNlciIsImV4cCI6MTQ3NjE4MjU5MSwiaWF0IjoxNDc2MTc1MzkxLCJqdGkiOiIxMSJ9.qsdjujdIwh_jHS-ImKi2B-j2BX_upgOgfs8Oa7v7bSI' \
  --header 'cache-control: no-cache' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --header 'postman-token: fb4def64-2672-b09a-0b41-fce91b520028' \
  --data 'username=binglin&password=123456'
```

## 指定 profile

* 在 application.propertites 指定，这样启动的时候不需要带参数
```
spring.profiles.active=test
```

* 在 mvn  或者 gradle 中指定 profile
```
mvn clean package -Dmaven.test.skip=true -P prod  
mvn spring-boot:run  -Dspring.profiles.active=dev
java -jar app.jar --spring.profiles.active=dev
gradle bootRun -Dspring.profiles.active=dev
```

* 在 Idea 中指定 profile
在  Run Configuration 中配置，甚至可以 Override parametres ,更多请见 [Faster Spring Boot with IntelliJ IDEA 14.1](https://blog.jetbrains.com/idea/2015/03/develop-spring-boot-applications-more-productively-with-intellij-idea-14-1/)

## 指定 logback 配置文件

```
logging.config=classpath:logback-pro.xml
```