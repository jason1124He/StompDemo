package com.websocket.websocket;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.util.logging.Logger;

/**
 * 订阅监听
 */
@Component
public class SubscribeEventListener implements ApplicationListener {

    private static final Logger logger = Logger.getLogger(SubscribeEventListener.class.getName());

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 连接请求
        if (event instanceof SessionConnectEvent) {
            logger.info("连接请求");
            SessionConnectEvent sessionConnectEvent = (SessionConnectEvent) event;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
            System.out.println(headerAccessor);
//            throw new RuntimeException();
        }
        // 连接成功
        else if (event instanceof SessionConnectedEvent) {
            logger.info("连接成功");
            SessionConnectedEvent connectedEvent = (SessionConnectedEvent) event;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(connectedEvent.getMessage());
            System.out.println(headerAccessor);
        }
        // 订阅
        else if (event instanceof SessionSubscribeEvent) {
            logger.info("订阅");
            SessionSubscribeEvent sessionSubscribeEvent = (SessionSubscribeEvent) event;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
            System.out.println(headerAccessor);

        }
        // 取消订阅
        else if (event instanceof SessionUnsubscribeEvent) {
            logger.info("取消订阅");
            SessionUnsubscribeEvent unsubscribeEvent = (SessionUnsubscribeEvent) event;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(unsubscribeEvent.getMessage());
            System.out.println(headerAccessor);
        }
        // 断开连接
        else if (event instanceof SessionDisconnectEvent) {
            logger.info("断开连接");
            SessionDisconnectEvent sessionDisconnectEvent = (SessionDisconnectEvent) event;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        }


    }

}
