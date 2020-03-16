package com.websocket.websocket;


import cn.hutool.core.collection.CollectionUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Map;
import java.util.logging.Logger;

/**
 * springboot websocoket stomp 配置
 *
 * @Author: admin
 * @Date: 2019/8/19 14:52
 * @Description:
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    private final Logger logger = Logger.getLogger(WebSocketStompConfig.class.getName());


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user")//topic用来广播，user用来实现p2p
                .setHeartbeatValue(new long[]{5000L, 5000L})//心跳监测：第一值表示server最小能保证发的心跳间隔毫秒数, 第二个值代码server希望client发的心跳间隔毫秒数
                .setTaskScheduler(new DefaultManagedTaskScheduler());

        // 这句话表示客户向服务器端发送时的主题上面需要加"/topic"作为前缀。
        config.setApplicationDestinationPrefixes("/topic");
        // 这句话表示给指定用户发送一对一的主题前缀是"/user"。
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/webServer").setAllowedOrigins("*").withSockJS();//使用sockJS连接，且允许跨域
        registry.addEndpoint("/queueServer").setAllowedOrigins("*").withSockJS();//注册两个STOMP的endpoint，分别用于广播和点对点
    }

    /**
     * 建立连接验证，请求拦截器
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //判断是否是首次连接
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    if (CollectionUtil.isNotEmpty(accessor.getNativeHeader("username")) && CollectionUtil.isNotEmpty(accessor.getNativeHeader("password"))) {

                        //验证是否登陆
                        String userName = accessor.getNativeHeader("username").get(0);
                        String password = accessor.getNativeHeader("password").get(0);
                        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
                            for (Map.Entry<String, String> entry : Users.USERS_MAP.entrySet()) {
                                if (entry.getKey().equalsIgnoreCase(userName) && entry.getValue().equalsIgnoreCase(password)) {
                                    //验证成功
                                    Authentication user = new Authentication(userName);
                                    accessor.setUser(user);
                                    logger.info("建立连接时用户名密码已保存到Principal");
                                    return message;
                                }

                            }
                        }
                        logger.info("建立连接时用户名密码为空");
                        return null;
                    }
                    //建立连接时未传参（用户名密码）
                    return null;
                }
                //不是第一次登陆
                return message;
            }
        });
    }
}