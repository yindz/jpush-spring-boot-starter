# 极光推送工具封装starter
## 概述
包含极光推送工具服务端SDK一些常用的配置和逻辑。建议需要用到极光推送的工程都引入。

## 引入
```
<dependency>
    <groupId>com.apifan.framework</groupId>
    <artifactId>jpush-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 配置说明
```
jpush:
  app-key: 123456789
  master-secret: 123456789
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
