package com.websocket.websocket;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.logging.Logger;


@RestController
public class SocketController {
    private static final Logger log = Logger.getLogger(SocketController.class.getName());

    @Autowired
    SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    public SimpMessagingTemplate template;

    @Autowired
    private WebSocketUtil webSocketUtil;

    /**
     * 接收客户端消息
     *
     * @param message
     */
    /*@MessageMapping("/subscribe")
    public void handleSubscribe(String message) {
        for (int i = 1; i <= 20; i++) {
            //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
            template.convertAndSendToUser("zhangsan", "/toUser/getResponse", message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
    @MessageMapping("/subscribe")
    public void subscribe(String message) {
        for (int i = 1; i <= 20; i++) {
            //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
            template.convertAndSend("/topic/getResponse", message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 测试对指定用户发送消息方法
     * 浏览器  访问https://xcx.dcssn.com/sendToUser?user=123456 前端小程序会收到信息
     * 客户端接收一对一消息的主题应该是“/user/” + 用户Id + “/message” ,这里的用户id可以是一个普通的字符串，只要每个用户端都使用自己的id并且服务端知道每个用户的id就行。
     *
     * @return 发送的消息内容
     */
    @RequestMapping(value = "/sendToUser", method = RequestMethod.GET)
    public void sendToUser() {
       /* String payload = "指定用户" + user + "接收信息" + LocalTime.now();
        simpMessageSendingOperations.convertAndSendToUser(user, "/message", payload);

        return payload;*/

        webSocketUtil.sendMsg("/toUser/getResponse", 123456);
    }


    @RequestMapping(value = "/test")
    public void test() {
       /* String payload = "指定用户" + user + "接收信息" + LocalTime.now();
        simpMessageSendingOperations.convertAndSendToUser(user, "/message", payload);

        return payload;*/

//        template.convertAndSendToUser("zhangsan", "/toUser/getResponse", 123);
        String UUID = "123456123456";
        NewTree newTree = new NewTree();
        newTree.setKey(1L);
        newTree.setText("1");
        template.convertAndSend("/user/" + UUID + "/message", newTree);
    }


    @MessageMapping("/queue")
    public void queue(String name, String user) {
//    public void queue(SysRoleVO sysRoleVO) {
        System.out.println("进入方法");
//        for (int i = 1; i <= 20; i++) {
//            广播使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
//            "/user/" + 用户Id + "/message", 其中 "/user" 是固定的
        template.convertAndSendToUser("zhangsan", "/message", name);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    /**
     * 定时推送
     */
    @Scheduled(fixedDelay = 1500)
//    @Scheduled(cron = "${time.cron}")
    public void testPlan() {
        String UUID = "123456123456";
        NewTree newTree = new NewTree();
        newTree.setKey(1500L);
        newTree.setText("定时推送");
        template.convertAndSend("/user/" + UUID + "/message", newTree);
    }


    /**
     * 接收客户端发来的消息
     */
    @MessageMapping("/message")
    public void handleMessage(String msg) {
        log.info("客户端发来消息：" + msg);
        JSONObject json = JSONObject.parseObject(msg);
        String to = json.getString("to");
        if (!StringUtils.isEmpty(to)) {
            simpMessageSendingOperations.convertAndSendToUser(to, "/message", msg);
        }
    }

    /**
     * 群发消息 订阅/topic/greetings 会收到
     * 浏览器  访问https://xcx.dcssn.com/sendToAll 前端小程序会收到信息
     *
     * @return 发送的消息内容
     */
    @RequestMapping(path = "/sendToAll", method = RequestMethod.GET)
    public String sendToUserAll() {
        String payload = "群发消息" + LocalTime.now();
        simpMessageSendingOperations.convertAndSend("/topic/greetings", payload);
        return payload;
    }


    /*收到消息后广播推送*/
    @MessageMapping("/boardcast")
    @SendTo("/topic/boardcast")
    public String sendtousers(String s) {
        System.out.println(s);
        return "我是广播消息";
    }

    /*收到消息后精准投送到单个用户*/
    @MessageMapping("/precise")
    /*broadcast = false避免把消息推送到同一个帐号不同的session中*/
    @SendToUser(value = "/topic/precise", broadcast = false)
    public String sendtouser(String s) {
        System.out.println(s);
        return "我是精准投送消息";
    }

    /*下面2个是不用接收消息动态发送消息的方法*/
    /*Scheduled为定时任务，演示*/
    @Scheduled(fixedDelay = 1500)
    public void boardcast() {
        this.template.convertAndSend("/topic", "来自服务器广播消息");
    }

    @Scheduled(fixedDelay = 1500)
    public void precise() {
        this.template.convertAndSendToUser("abc", "/message", "来自服务器精准投送消息");
    }
}
