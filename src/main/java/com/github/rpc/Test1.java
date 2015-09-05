package com.github.rpc;

import com.github.rpc.server.NettyServer;

import java.util.concurrent.Executors;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        NettyServer server=new NettyServer();
        Hello hello=new HelloImpl();
        server.registerProcessor("hehe",Hello.class.getName(),"1.0",Hello.class,hello);
        server.start(9999, Executors.newFixedThreadPool(10));


    }
}
