package com.apifan.framework.jpush;

import com.apifan.framework.jpush.component.JPushApi;
import com.apifan.framework.jpush.config.JPushProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类
 *
 * @author yin
 */
@Configuration
@ConditionalOnProperty(prefix = "jpush", name = "app-key")
@EnableConfigurationProperties(JPushProperties.class)
@ComponentScan(basePackages = "com.apifan.framework.jpush")
public class JPushAutoConfiguration {

    private final JPushProperties jPushProperties;

    public JPushAutoConfiguration(final JPushProperties jPushProperties) {
        this.jPushProperties = jPushProperties;
    }

    /**
     * 极光推送API辅助工具
     *
     * @return JPushApi实例
     */
    @Bean
    public JPushApi jPushApi() {
        final JPushApi jPushApi = new JPushApi(jPushProperties);
        jPushApi.init();
        return jPushApi;
    }
}
