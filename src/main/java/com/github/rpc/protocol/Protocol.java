package com.github.rpc.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by qianxuecheng on 15/9/5.
 * 耦合netty
 */
public interface Protocol {
    /**
     * Header
     * VERSION(1B):
     * LENGTH(4B)
     * TYPE:request/response
     *
     *
     * @param byteBuf
     * @return
     * @throws Exception
     */


    Object decode(ByteBuf byteBuf) throws Exception;

    ByteBuf encode(Object msg, ByteBuf byteBuf)throws Exception;


}
