package com.github.rpc.client.netty;

import com.github.rpc.RequestWrapper;
import com.github.rpc.client.AbstractClient;
import com.github.rpc.client.ClientFactory;
import com.github.rpc.protocol.Request;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

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
    public void sendRequest(Request request) throws Exception {
        ChannelFuture writeFuture = channelFuture.channel().writeAndFlush(request);
        writeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    return;
                }
            }
        });


    }
    public ClientFactory getClientFactory() {
        return NettyClientFactory.getInstance();
    }

}
