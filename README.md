# 简单易用的极光推送服务端辅助工具包
## 概述
包含极光推送工具服务端SDK一些常用的配置和逻辑。可直接快速集成到您的 SpringBoot 项目中。

## 引入
### 配置仓库
暂时尚未进入maven中央仓库，因此请在 pom.xml 中配置一个仓库地址：
```xml
<repositories>
    <repository>
        <id>apifan-repo</id>
        <name>apifan-repo</name>
        <url>http://118.31.70.236:8004/nexus/content/repositories/biz-repo/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
### 添加依赖
```
<dependency>
    <groupId>com.apifan.framework</groupId>
    <artifactId>jpush-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
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
#### 使用匿名代理
```
jpush:
  app-key: 123456789
  master-secret: 123456789
  use-proxy: true
  proxy-host: 192.168.2.2
  proxy-port: 3128
```
#### 使用非匿名代理
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

## 使用范例
```
@Autowired
private JPushHelper jPushHelper;
    
//...


public void push(){
    //消息内容    
    String content = "这是一条测试消息！";
    
    //附加业务参数
    Map<String, String> extras = new HashMap<>();
    extras.put("bizCode", "123");
    
    //根据设备ID推送
    List<String> deviceIdList = new ArrayList<>();
    deviceIdList.add("asdfghjkl");
    jPushHelper.pushToDevices(deviceIdList, content, extras);
    
    //根据别名推送
    List<String> aliasList = new ArrayList<>();
    aliasList.add("poiuytreqw");
    jPushHelper.pushToAliases(aliasList, content, extras);
    
    //根据标签推送
    List<String> tagsList = new ArrayList<>();
    tagsList.add("test-tag1");
    jPushHelper.pushToTags(tagsList, content, extras);
    
    //推送给全部客户端
    jPushHelper.pushToAll(content, extras);    
}

```
