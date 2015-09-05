package com.github.rpc.server;

import com.github.rpc.client.netty.NettyProtocolDecoder;
import com.github.rpc.client.netty.NettyProtocolEncoder;
import com.github.rpc.util.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class NettyServer implements Server {
    private ServerBootstrap bootstrap=null;
    private AtomicBoolean startFlag=new AtomicBoolean(false);
    private static final int NPROCESSORS=Runtime.getRuntime().availableProcessors();

    public NettyServer() {
        ThreadFactory serverBossTF=new NamedThreadFactory("NETTYSERVER-BOSS-");
        ThreadFactory serverWorkerTF=new NamedThreadFactory("NETTTYSERVER-WORKER-");
        EventLoopGroup bossGroup=new NioEventLoopGroup(NPROCESSORS,serverBossTF);
        EventLoopGroup workerGroup=new NioEventLoopGroup(NPROCESSORS*2,serverWorkerTF);
        bootstrap=new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_REUSEADDR,true);


    }

    @Override
    public void start(int listenPort, final ExecutorService bizThreadPool) throws Exception {
        if(!startFlag.compareAndSet(false,true)){
            return;
        }
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new NettyProtocolDecoder());
                pipeline.addLast("encoder", new NettyProtocolEncoder());
                pipeline.addLast("handler", new NettyServerHandler(bizThreadPool));
            }
        });
        bootstrap.bind(new InetSocketAddress(listenPort)).sync();

    }

    @Override
    public void registerProcessor(String group, String interfaceName, String version, Class<?> interfaceType,Object instance) throws Exception {
        RpcServerHandler.instance.registerProcessor(group,interfaceName,version,interfaceType,instance);

    }

    @Override
    public void stop() throws Exception {

    }
}
