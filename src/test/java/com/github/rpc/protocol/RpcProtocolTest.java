package com.github.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class RpcProtocolTest {

    public Request request;
    public Response response;
    public ByteBuf byteBuf= PooledByteBufAllocator.DEFAULT.buffer(1024);
    public RpcProtocol protocol=RpcProtocol.getInstance();
    @Before
    public void setUp(){
        request=new Request("tts","Hello",0,"1",1,19,"sayHello",new Class<?>[]{String.class},new Object[]{"qian"});
        response=new Response(2,1,3);
        response.setReply("hehe");

    }

    @After
    public void tearDown(){

    }


    @org.junit.Test
    public void testDecodeResponse() throws Exception {

    }

    @org.junit.Test
    public void testDecodeRequest() throws Exception {

    }

    @org.junit.Test
    public void testEncodeResponse() throws Exception {

    }

    @org.junit.Test
    public void testEncodeRequest() throws Exception {
        protocol.encode(request,byteBuf);
        Object request = protocol.decode(byteBuf);
        System.out.println(request);


    }
}