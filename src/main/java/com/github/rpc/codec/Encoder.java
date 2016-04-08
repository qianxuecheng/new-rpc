package com.github.rpc.codec;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public interface Encoder {
     byte[] encode(Object obj) throws Exception;

}
