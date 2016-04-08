package com.github.rpc.codec;

/**
 * Created by qianxuecheng on 15/9/5.
 */
public interface ByteBufferWrapper {
     ByteBufferWrapper get(int capacity);

     void writeByte(int index);

     void writeByte(byte data);

     byte readByte();

     void writeInt(int data);

     void writeBytes(byte[] data);

     int readableBytes();

     int readInt();

     void readBytes(byte[] dst);

     int readerIndex();

     void setReaderIndex(int readerIndex);

}
