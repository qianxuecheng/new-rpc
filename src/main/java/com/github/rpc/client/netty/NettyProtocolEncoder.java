package com.github.rpc.client.netty;

import com.github.rpc.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class NettyProtocolEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf= PooledByteBufAllocator.DEFAULT.buffer(1000);
        RpcProtocol.getInstance().encode(msg,byteBuf);
        ctx.write(byteBuf,promise);
    }
}
