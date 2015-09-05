package com.github.rpc.server;

import java.util.concurrent.ExecutorService;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public interface Server {
    public void start(int listenPort,ExecutorService bizThreadPool) throws Exception;

    public void registerProcessor(String group,String interfaceName,String version,Class<?> interfaceType,Object instance) throws Exception;

    public void stop()throws Exception;


}
