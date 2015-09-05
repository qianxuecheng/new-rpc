package com.github.rpc.codec;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class JdkDecoder  implements Decoder{
    @Override
    public Object decode(String className, byte[] bytes) throws Exception {
        ObjectInputStream objectInputStream=new ObjectInputStream(new ByteArrayInputStream(bytes));
       Object object=objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }
}
