package com.github.rpc.client.netty;

import com.github.rpc.ResponseWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private String key;
    private NettyClient client;

    public NettyClientHandler(String key) {
        this.key = key;
    }

    public NettyClient getClient() {
        return client;
    }

    public void setClient(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//
        if(msg instanceof List){
            List<ResponseWrapper> responseWrappers= (List<ResponseWrapper>) msg;
            client.putResponses(responseWrappers);
        }else if(msg instanceof ResponseWrapper){
            client.putResponse((ResponseWrapper)msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
