package com.github.rpc.client.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.RecyclableArrayList;

import java.util.List;

/**
 * Created by qianxuecheng on 15/9/4.
 */
public class NettyProtocolDecoder extends ChannelInboundHandlerAdapter {
    ByteBuf cumulation;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RecyclableArrayList out = RecyclableArrayList.newInstance();
        if(msg instanceof ByteBuf) {//this will be recycle by netty don't worry
            ByteBuf data= (ByteBuf) msg;
            if(cumulation==null){
                cumulation=data;
                callDecode(ctx,cumulation,out);
            }
        }
        super.channelRead(ctx, msg);
    }

    private void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {//输出参数 bad smell
        while (in.isReadable()){
            int outSize=out.size();
            int oldInputLength=in.readableBytes();
            decode(ctx,in,out);

        }

    }

    private void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        NettyByrteBufferWrapper wrapper=new NettyByrteBufferWrapper(in);
       // Object res


    }
}
