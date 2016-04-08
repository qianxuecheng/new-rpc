package com.github.rpc.codec;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public interface Decoder {
     Object decode(String className,byte[] bytes)throws Exception;
}
