package com.github.rpc.client.netty;

import com.github.rpc.client.AbstractClientFactory;
import com.github.rpc.client.Client;
import com.github.rpc.util.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class NettyClientFactory extends AbstractClientFactory {

    private static final int nProcessors=Runtime.getRuntime().availableProcessors();
    private static final ThreadFactory workerThreadFactory = new NamedThreadFactory("NETTYCLIENT-WORKER-");

    private static EventLoopGroup workerGroup=new NioEventLoopGroup(nProcessors,workerThreadFactory);
    private static final NettyClientFactory instance=new NettyClientFactory();
    public static NettyClientFactory getInstance(){
        return instance;
    }
    @Override
    protected Client createClient(String targetIP, int targetPort, int connectTimeout, final String key) throws Exception {
        Bootstrap bootstrap=new Bootstrap();
        final NettyClientHandler handler = new NettyClientHandler(key);

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.TCP_NODELAY,true)//参考UNP p152禁止Nagle算法  turning
                .option(ChannelOption.SO_REUSEADDR,true)//参考UNP p165 port建议设置选项 only for tcp
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder",new NettyProtocolDecoder());
                        pipeline.addLast("encoder",new NettyProtocolEncoder());
                        pipeline.addLast(handler);


                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(targetIP, targetPort)).sync();
        channelFuture.awaitUninterruptibly(connectTimeout);
        NettyClient client=new NettyClient(channelFuture,key);
        handler.setClient(client);
        return client;
    }

    @Override
    public Client getClient(String targetIP, int targetPort) throws Exception {
        return createClient(targetIP,targetPort,1000,"");
    }
}
