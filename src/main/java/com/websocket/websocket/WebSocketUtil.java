package com.websocket.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * websocket 工具类
 *
 * @Author: admin
 * @Date: 2019/8/19 16:58
 * @Description:
 */
@Component
public class WebSocketUtil {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送数据
     *
     * @param url
     * @param msg
     */

    public void sendMsg(String url, Object msg) {
        messagingTemplate.convertAndSend(url, msg);
    }
}
