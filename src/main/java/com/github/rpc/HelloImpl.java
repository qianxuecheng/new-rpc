package com.github.rpc;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class HelloImpl implements Hello {
    @Override
    public String sayHello(String name) {
        return "Hello "+name;
    }
}
