package com.github.rpc.client.netty;

import com.github.rpc.RequestWrapper;
import com.github.rpc.client.AbstractClient;
import com.github.rpc.client.ClientFactory;
import io.netty.channel.ChannelFuture;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class NettyClient extends AbstractClient {

    private ChannelFuture channelFuture;
    private String key;
    public NettyClient(ChannelFuture channelFuture,String key) {
        this.channelFuture = channelFuture;
        this.key=key;

    }

    @Override
    public void sendRequest(RequestWrapper wrapper) throws Exception {


    }
    public ClientFactory getClientFactory() {
        return NettyClientFactory.getInstance();
    }

}
