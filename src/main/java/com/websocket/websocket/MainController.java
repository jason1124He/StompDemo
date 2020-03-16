package com.websocket.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: hej
 * @Date: 2019/7/17 10:58
 * @Description:
 */
@Controller
public class MainController {

    @RequestMapping("/topic")
    public String topic(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("root", request.getContextPath());
        return "topic";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("root", request.getContextPath());
        return "index";
    }

    @RequestMapping("/queue")
    public String queue(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("root", request.getContextPath());
        return "queue";
    }
}
