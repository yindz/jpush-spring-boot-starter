# 简单易用的极光推送服务端辅助工具包
## 概述
包含极光推送工具服务端SDK一些常用的配置和逻辑。可直接快速集成到您的 SpringBoot 项目中。

## 引入
### 添加依赖
暂时尚未进入maven中央仓库，因此请安装到本地仓库目录：
```
mvn install
```
引入依赖：
```xml
<dependency>
    <groupId>com.apifan.framework</groupId>
    <artifactId>jpush-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

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
```
@Autowired
private JPushApi jPushApi;
    
//...


public void push(){
    PushMessage pm = new PushMessage();

    //消息内容
    pm.setContent("这是一条测试消息！");
    
    //附加业务参数
    Map<String, String> extras = new HashMap<>();
    extras.put("bizCode", "123");
    pm.setExtras(extras);

    //指定角标值(仅在iOS设备生效)
    pm.setBadge(8);

    //指定优先级(仅在安卓设备生效)
    pm.setPriority(1);
    
    //根据设备ID推送
    List<String> deviceIdList = new ArrayList<>();
    deviceIdList.add("asdfghjkl");
    Long msgId = jPushApi.pushToDevices(deviceIdList, pm);
    
    //根据别名推送
    List<String> aliasList = new ArrayList<>();
    aliasList.add("poiuytreqw");
    Long msgId = jPushApi.pushToAliases(aliasList, pm);
    
    //根据标签推送
    List<String> tagsList = new ArrayList<>();
    tagsList.add("test-tag1");
    Long msgId = jPushApi.pushToTags(tagsList, pm);
    
    //推送给全部客户端
    Long msgId = jPushApi.pushToAll(pm);
}

```
