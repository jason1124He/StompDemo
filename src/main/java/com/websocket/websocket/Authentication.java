package com.websocket.websocket;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * @Author: admin
 * @Date: 2019/8/20 16:01
 * @Description:
 */
public class Authentication implements Principal {

    private String name;

    public Authentication(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
