package com.github.rpc.client.netty;

import com.github.rpc.client.AbstractClientInvocationHandler;
import com.github.rpc.client.ClientFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class NettyClientInvocationHandler extends AbstractClientInvocationHandler {
    public NettyClientInvocationHandler(List<InetSocketAddress> servers, String group, String interfaceName, String version, int codecType, int protocolType) {
        super(servers, group, interfaceName, version, codecType, protocolType);
    }




    @Override
    protected ClientFactory getClientFactory() {
        return NettyClientFactory.getInstance();
    }
}
