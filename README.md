# webcached

## 简介
本项目旨在帮助web开发人员建立一个web端缓存控制机制，以web请求的信息来创建key（包含uri、参数、method），将请求结果保存到缓存服务器中，开发人员可以选择或忽略特定uri和特定参数。同时，还允许开发人员将一些uri设置为“缓存刷新触发器”，当这些uri被业务逻辑正确的执行后（response statusCode==200），会触发刷新或删除与之相关联的缓存。通过这种方式来提高开发人员对缓存的控制能力。


## 实现功能
1. 根据url、参数等信息缓存url结果

    直接根据url、参数等web请求信息来生成key，而它的结果将被当做value

2. 关联刷新功能

    当一个trigger url被调用时，它所关联的缓存将得到刷新，支持表达式匹配

3. 到期自动刷新功能

    当缓存快要到期时，将会自动刷新


## 配置文件举例说明
```xml
<caches>
	<cache>
		<uri>/user/userInfo</uri>
		<parameter type="exclude_all" exceptions="userId"/>
		<expired-time>180</expired-time>
		<auto-refresh>true</auto-refresh>
		<support-status>200</support-status>
	</cache>
	<cache>
		<uri>/user/userList</uri>
		<parameter type="exclude_all" exceptions="pageNo"/>
	</cache>
</caches>
<update-triggers>
	<trigger cache-uri="/user/userInfo" trigger-uri="/user/saveUser" strategy="refresh" scope="specific">
		<conditions>
			<condition type="param-equal" name="userId" value="${userId}"/>
		</conditions>
	</trigger>
	<trigger cache-uri="/user/userList" trigger-uri="/user/saveUser" strategy="refresh" scope="all">
		<conditions>
			<condition type="non-param"/>
		</conditions>
	</trigger>
</update-triggers>
```
这段xml配置表示，有两个uri需要缓存，分别是`/usr/userInfo`（用户详情接口）和`/user/userList`（用户列表接口），在`/usr/userInfo`中，要包含参数`userId`，不同的`userId`要保存成不同的缓存对象，相互之间不能干扰；而`/user/userList`的情况也类似，只不过参数换成了`pageNo`。

还有两个缓存刷新触发器，以第一个来举例说明，表示如果保存一条用户时，要去刷新这条用户的“用户详情接口”，根据`userId`来确定是哪一个用户。而第二个触发器，表示当保存任意一条用户信息时，要刷新全部的“用户列表接口”。

## 适用场景
本工具适用于采用Java技术搭建的Web服务器。

## 项目结构
本项目采用maven构建，项目结构采用标准maven结构，分为两个模块：webcached-core，webcached-example

```
webcached
  -- webcached-core 核心包，在引入时只引入这个包就行了
  -- webcached-example 示例，使用springmvc搭建了一个简易的web系统，用来演示webcached的功能
```
  
## 快速体验
- 说明：本项目对缓存服务器进行了抽象，可以支持任意一种缓存服务器（诸如memcached/redis/mongodb或者干脆直接用HashMap），但截至到目前，只实现了memcached版本和本地HashMap版本。如果要实现其他版本的CacheHandler，只需实现`CacheHandler`接口就行了，并在`webcached/webcached-example/src/main/resoures/spring-context.xml`中配置一下即可
- 下载项目，导入到开发环境（我用的是Intellij Idea）
- 打开`webcached/webcached-example/src/main/resoures/config.properties`，修改`memcached.servers`为自己的memcached服务器地址
- 为`webcached-example`模块配置一个tomcat容器，跑起来，假设端口`8080`，contextPath为`webcached`
- 用下面的三个接口来验证缓存管理机制
	```
	用户详情接口： http://127.0.0.1:8080/webcached/user/userInfo?userId=1
	
	保存用户信息接口： http://127.0.0.1:8080/webcached/user/saveUser?userId=1&userName=james1
	
	用户列表接口： http://127.0.0.1:8080/webcached/user/userList?pageNo=1
	```
	这三个接口显然存在一定的关系，用户详情接口和用户列表接口是可以被缓存的，而“保存用户信息接口”一旦成功，那么它修改的那条用户的详情接口的缓存需要刷新一下，用户列表接口的数据（可以分页）全都要刷新。通过查看这些接口是否使用了缓存、是否刷新来验证缓存控制机制是否生效，“是否使用缓存”将通过下面的方式来判断
- 接口的返回结果，关于缓存管理的信息都保存在response的header中  
	- header `webcache` 表示是否使用缓存，有3个可能的值，值为`hit`表示命中，值为`no cache`表示没有用到缓存，值为`skip`表示此接口不需要缓存

	- header `cach_left_time` 表示本次访问时此接口所剩余的缓存时间（前提是此次访问命中了缓存，即`webcache->hit`），以“时分秒”的格式展现

