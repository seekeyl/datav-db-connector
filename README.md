# datav-db-connector

## 0. 此项目旨在提供阿里官方以外另一种数据库连接方式
正常情况下，还是建议使用阿里官方的连接器
<https://help.aliyun.com/document_detail/111123.htm>
这个应用主要用于解决有特殊需求的应用

## 1. 证书(当然也可以用自有的证书但必需有一个，因为DataV使用的是https的连接方式)：
### 1.1. 创建证书
``` shell
keytool -genkey -alias <证书名称> -keyalg RSA -keystore <证书文件名，例：d:\keystore.jks>  -dname "CN=<域名>, OU=<机构>, O=<公司>, L=<城市>, ST=<省>, C=<国家>" -keypass <key密码> -storepass <存储密码> -validity <有效期>  -storetype <存储类型，如：PKCS12>
```

### 1.2. 存放证书
把证书复制到本项目的cert目录下

### 1.3. 修改配置
修改src/main/resource下的application.yml

``` yaml
server:
  port: 8080
  ssl:
    key-alias: 证书名称
    key-store: cert/证书文件名.jks
    key-store-password: 存储密码
    key-store-type: JKS
```

## 2. 数据库配置
### 2.1. 注册数据库
修改src/main/resource/spring/下的context-db.xml
按实际需要，维护需要连接的数据库连接

``` xml
<bean id="postgresql" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
	<property name="name" value="Postgresql"/>
	<property name="driverClassName" value="org.postgresql.Driver"/>
	<property name="url" value="jdbc:postgresql://localhost:5432/databasename"/>
	<property name="username" value="root"/>
	<property name="password" value="password"/>
	...
</bean>
```
在数据库列表里配置数据库表

``` xml
<bean id="multipleDataSource" class="com.seekey.dao.MultipleDataSource">
	<property name="defaultTargetDataSource" ref="默认数据库ID" />
	<property name="targetDataSources">
		<map>
			<entry key="默认数据库ID 1" value-ref="默认数据库实例名 1"/>
			<entry key="默认数据库ID 2" value-ref="默认数据库实例名 2"/>
			...
		</map>
	</property>
</bean>
```

## 3. 配置DataV连接参数
修改src/main/resource下的application.yml

``` yaml
datav:
  visitkey: visitkey必需是一个32位的英数混合字符串
  secret: srcret必需是一个16位的英数混合字符串
  validatetime: 是否校验发出时间
  expired: 超时时长（秒）
```