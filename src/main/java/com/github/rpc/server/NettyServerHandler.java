package com.github.rpc.server;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import com.github.rpc.protocol.Request;
import com.github.rpc.protocol.Response;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter{
    private ExecutorService bizThreadPool;

    public NettyServerHandler(ExecutorService bizThreadPool) {
        this.bizThreadPool = bizThreadPool;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof Request)&&!(msg instanceof List)){
            throw new Exception("only support request ||list");
        }

        handleRequest(ctx, msg);

    }
    private void handleRequest(final ChannelHandlerContext ctx, final Object message) {
        try {
            bizThreadPool.execute(new HandlerRunnable(ctx, message, bizThreadPool));
        }
        catch (RejectedExecutionException exception) {
       //    // LOGGER.error("server threadpool full,threadpool maxsize is:"
             //       + ((ThreadPoolExecutor) threadpool).getMaximumPoolSize());
         /*   if(message instanceof List){
                List<Request> requests = (List<Request>) message;
                for (final RequestWrapper request : requests) {
                    sendErrorResponse(ctx, request);
                }
            }
            else{
                sendErrorResponse(ctx, (RequestWrapper) message);
            }*/
        }
    }
    class HandlerRunnable implements Runnable{

        private ChannelHandlerContext ctx;

        private Object message;

        private ExecutorService bizThreadPool;

        public HandlerRunnable(ChannelHandlerContext ctx,Object message,ExecutorService threadPool){
            this.ctx = ctx;
            this.message = message;
            this.bizThreadPool = threadPool;
        }

        @SuppressWarnings("rawtypes")
        public void run() {
            // pipeline
            if(message instanceof List){
                List messages = (List) message;
                for (Object messageObject : messages) {
                    bizThreadPool.execute(new HandlerRunnable(ctx, messageObject, bizThreadPool));
                }
            }
            else{
                Request request = (Request)message;
                //long beginTime = System.currentTimeMillis();
                Response responseWrapper = RpcServerHandler.instance.handleRequest(request);
               // final int id = request.getSessionId();
                // already timeout,so not return
            /*    if ((System.currentTimeMillis() - beginTime) >= request.getTimeout()) {
                    LOGGER.warn("timeout,so give up send response to client,requestId is:"
                            + id
                            + ",client is:"
                            + ctx.channel().remoteAddress()+",consumetime is:"+(System.currentTimeMillis() - beginTime)+",timeout is:"+request.getTimeout());
                    return;
                }*/
                ChannelFuture wf = ctx.channel().writeAndFlush(responseWrapper);
                wf.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(!future.isSuccess()){
                       //     LOGGER.error("server write response error,request id is: " + id);
                        }
                    }
                });
            }
        }

    }
}
