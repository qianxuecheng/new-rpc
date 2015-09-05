package com.github.rpc.codec;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class JdkEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
