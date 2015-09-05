package com.github.rpc.client.netty;

import com.github.rpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {//客户端upstream
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
            List<Response> responses = (List<Response>) msg;
            client.putResponses(responses);
        }else if(msg instanceof Response){
            client.putResponse((Response)msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
