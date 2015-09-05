package com.github.rpc.client.netty;

import com.github.rpc.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.RecyclableArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianxuecheng on 15/9/4.
 */
public class NettyProtocolDecoder extends ChannelInboundHandlerAdapter {
    ByteBuf cumulation;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RecyclableArrayList out = RecyclableArrayList.newInstance();
        try {
            if(msg instanceof ByteBuf) {//this will be recycle by netty don't worry
                ByteBuf data= (ByteBuf) msg;
                if(cumulation==null){
                    cumulation=data;
                    try {
                        callDecode(ctx,cumulation,out);
                    } finally {
                        if(cumulation!=null&&!cumulation.isReadable()){
                            cumulation.release();
                            cumulation=null;
                        }
                    }
                }else{
                    try {
                        if(cumulation.writerIndex()+data.readableBytes()>cumulation.capacity()){
                            ByteBuf oldCumulation=cumulation;
                            cumulation=ctx.alloc().buffer(oldCumulation.readableBytes()+data.readableBytes());
                            cumulation.writeBytes(oldCumulation);
                            oldCumulation.release();
                        }
                        cumulation.writeBytes(data);
                        callDecode(ctx,cumulation,out);
                    } finally {
                        if(cumulation!=null){
                            if(!cumulation.isReadable()){
                                cumulation.release();
                                cumulation=null;
                            }
                        }

                    }
                    data.release();
                }
            }else {
                out.add(msg);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        } finally {
            List<Object> results=new ArrayList<Object>();
            for(Object result:out){
                results.add(result);
            }
            ctx.fireChannelRead(results);
            out.recycle();
        }
    }

    private void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception{//输出参数 bad smell
        while (in.isReadable()){
            int outSize=out.size();
            int oldInputLength=in.readableBytes();
            decode(ctx,in,out);

        }

    }

    private void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception{
        Object result = RpcProtocol.getInstance().decode(in);
        if(result!=null){
            out.add(result);
        }

        // Object res


    }
}
