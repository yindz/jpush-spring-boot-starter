![http://www.opensource.org/licenses/mit-license.php](https://img.shields.io/badge/license-MIT-blue)
![](https://img.shields.io/badge/java-1.8%2B-yellow)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.apifan.framework/jpush-spring-boot-starter/badge.svg "Maven Central")](https://search.maven.org/artifact/com.apifan.framework/jpush-spring-boot-starter/1.0.1/jar)
# 极光推送服务端辅助工具包
## 概述
包含极光推送工具服务端SDK一些常用的配置和逻辑。可直接快速集成到您的 SpringBoot 项目中。支持的功能包括：
- 根据根据设备Registration ID推送
- 根据别名推送
- 根据标签推送
- 推送给全部客户端设备
- 查询指定别名下的设备Registration ID
- 删除指定别名
- 自定义代理服务器
- 失败重试
- 指定角标
- 指定优先级
- 传递自定义业务参数

## 配置说明
### 默认
当主机可以直接访问 api.jpush.cn 时，采用如下配置：
```
jpush:
  app-key: 123456789
  master-secret: 123456789
```
### 使用代理服务器
对于某些主机无法直接访问 api.jpush.cn 的情况，可以配置代理服务器来实现连接。
#### 使用公开代理
```
jpush:
  app-key: 123456789
  master-secret: 123456789
  use-proxy: true
  proxy-host: 192.168.2.2
  proxy-port: 3128
```
#### 使用需认证的代理
```
jpush:
  app-key: 123456789
  master-secret: 123456789
  use-proxy: true
  proxy-host: 192.168.2.3
  proxy-port: 3127
  proxy-username: myproxy
  proxy-password: 123456
```
### 开启重试
可自行配置重试时间间隔和重试次数：
```
  retry-interval: 1000
  retry-max-attempts: 3
```
说明：
- retry-interval 表示第1次重试的时间间隔，单位为毫秒；如果 retry-max-attempts 值大于1，则从第2次重试开始，重试间隔时间逐次翻倍；
- retry-max-attempts=0 时表示不做重试，默认值为0。

## 使用范例
### 引入依赖
```xml
<dependency>
    <groupId>com.apifan.framework</groupId>
    <artifactId>jpush-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```
### 注入
```
@Autowired
private JPushApi jPushApi;
```    
### 设置消息属性
```
PushMessage pm = new PushMessage();

//消息内容
pm.setContent("这是一条测试消息！");

//指定角标值(仅在iOS设备生效)
pm.setBadge(8);

//指定优先级(仅在安卓设备生效)
pm.setPriority(1);

//附加业务参数
Map<String, String> extras = new HashMap<>();
extras.put("bizCode", "123");
pm.setExtras(extras);
```
### 根据设备Registration ID推送
```
List<String> deviceIdList = new ArrayList<>();
deviceIdList.add("asdfghjkl");
deviceIdList.add("lkjhgfdsa");
Long msgId = jPushApi.pushToDevices(deviceIdList, pm);
```
### 根据别名推送
```
List<String> aliasList = new ArrayList<>();
aliasList.add("3ab016d0");
aliasList.add("7e9e2382");
Long msgId = jPushApi.pushToAliases(aliasList, pm);
```
### 根据标签推送
```
List<String> tagsList = new ArrayList<>();
tagsList.add("test-tag1");
Long msgId = jPushApi.pushToTags(tagsList, pm);
```
### 推送给全部客户端设备
```
Long msgId = jPushApi.pushToAll(pm);
```
### 查询指定别名下的设备Registration ID
```
List<String> regIdList = jPushApi.findRegistrationId("3ab016d0");
```
### 删除指定别名
```
List<String> toDelete = new ArrayList<>();
toDelete.add("3ab016d0");
toDelete.add("7e9e2382");
jPushApi.deleteAlias(toDelete);
```
