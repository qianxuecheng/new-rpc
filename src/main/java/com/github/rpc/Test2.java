package com.github.rpc;

import com.github.rpc.client.netty.NettyClientFactory;
import com.github.rpc.client.netty.NettyClientInvocationHandler;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Collections;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class Test2 {
    public static void main(String[] args) {

        Hello hello=(Hello)Proxy.newProxyInstance(Hello.class.getClassLoader(),new Class<?>[]{Hello.class},
                new NettyClientInvocationHandler(Collections.singletonList(new InetSocketAddress("127.0.0.1", 9999)),"hehe"
                        ,Hello.class.getName(),"1.0",0,1));
        System.out.println(hello.sayHello("qian"));


    }
}
