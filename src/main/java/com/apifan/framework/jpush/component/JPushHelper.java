package com.apifan.framework.jpush.component;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.apifan.biz.common.util.StringUtils;
import com.apifan.framework.jpush.config.JPushProperties;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 极光推送辅助工具
 *
 * @author yinzl
 */
public class JPushHelper {
    private static final Logger logger = LoggerFactory.getLogger(JPushHelper.class);

    private final JPushProperties jPushProperties;

    private JPushClient jPushClient;

    private boolean initSuccess;

    public JPushHelper(JPushProperties jPushProperties) {
        this.jPushProperties = jPushProperties;
    }

    /**
     * 初始化
     */
    public void init() {
        if (initSuccess) {
            return;
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(jPushProperties.getAppKey()), "appKey不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(jPushProperties.getMasterSecret()), "masterSecret不能为空");
        ClientConfig clientConfig = ClientConfig.getInstance();
        jPushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey(), null, clientConfig);
        logger.info("初始化JPushClient成功");
        initSuccess = true;
    }

    /**
     * 根据设备ID推送
     *
     * @param deviceIdList 设备ID列表
     * @param content      内容
     * @param extras       附加参数
     * @return 是否成功
     */
    public boolean pushToDevices(List<String> deviceIdList, String content, Map<String, String> extras) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(deviceIdList), "设备ID为空");
        Preconditions.checkArgument(deviceIdList.size() <= 1000, "设备ID不超过1000个");
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "消息为空");
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(deviceIdList))
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(IosNotification.newBuilder().setSound("default").setBadge(1).addExtras(extras).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setBuilderId(1).addExtras(extras).build()).build()
                ).build();
        return push(payload);
    }

    /**
     * 根据别名推送
     *
     * @param aliasList 别名列表
     * @param content   内容
     * @param extras    附加参数
     * @return 是否成功
     */
    public boolean pushToAliases(List<String> aliasList, String content, Map<String, String> extras) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(aliasList), "别名为空");
        Preconditions.checkArgument(aliasList.size() <= 1000, "别名不超过1000个");
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "消息为空");
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(aliasList))
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(IosNotification.newBuilder().setSound("default").setBadge(1).addExtras(extras).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setBuilderId(1).addExtras(extras).build()).build()
                ).build();
        return push(payload);
    }

    /**
     * 根据标签推送
     *
     * @param tagsList 标签列表
     * @param content  内容
     * @param extras   附加参数
     * @return 是否成功
     */
    public boolean pushToTags(List<String> tagsList, String content, Map<String, String> extras) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(tagsList), "标签为空");
        Preconditions.checkArgument(tagsList.size() <= 1000, "标签不超过1000个");
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "消息为空");
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tagsList))
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(IosNotification.newBuilder().setSound("default").setBadge(1).addExtras(extras).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setBuilderId(1).addExtras(extras).build()).build()
                ).build();
        return push(payload);
    }

    /**
     * 推送给所有客户端
     *
     * @param content 内容
     * @param extras  附加参数
     * @return 是否成功
     */
    public boolean pushToAll(String content, Map<String, String> extras) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "消息为空");
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(IosNotification.newBuilder().setSound("default").setBadge(1).addExtras(extras).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setBuilderId(1).addExtras(extras).build()).build()
                ).build();
        return push(payload);
    }

    /**
     * 进行推送
     *
     * @param payload 消息体
     * @return
     */
    private boolean push(PushPayload payload) {
        logger.info("推送消息体: {}", payload.toJSON());
        try {
            PushResult result = jPushClient.sendPush(payload);
            if (result != null) {
                if (200 == result.getResponseCode()) {
                    logger.info("消息推送成功，消息ID: {}", result.msg_id);
                    return true;
                } else {
                    logger.error("消息 {} 推送失败，ResponseCode={}", result.getResponseCode());
                }
            }
        } catch (APIConnectionException e) {
            logger.error("推送消息出现APIConnectionException异常", e);
        } catch (APIRequestException e) {
            logger.error("推送消息出现APIRequestException异常", e);
            logger.error("推送消息出现异常, status={}, errorCode={}, errorMessage={}", e.getStatus(), e.getErrorCode(), e.getErrorMessage());
        }
        return false;
    }

}
