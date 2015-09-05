package com.github.rpc.server;

import com.github.rpc.protocol.Request;
import com.github.rpc.protocol.Response;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public interface ServerHandler {
    public void registerProcessor(String group,String interfaceName,String version, Class<?> interfaceType,Object instance)throws Exception;

    /**
     * handle the request
     */
    public Response handleRequest(final Request request);
}
