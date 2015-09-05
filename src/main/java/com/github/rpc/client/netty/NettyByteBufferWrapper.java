package com.github.rpc.client.netty;

import com.github.rpc.codec.ByteBufferWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qianxuecheng on 15/9/5.
 */
public class NettyByteBufferWrapper implements ByteBufferWrapper{
    private ByteBuf buffer;
    private ChannelHandlerContext ctx;

    public NettyByteBufferWrapper(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public NettyByteBufferWrapper(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public ByteBufferWrapper get(int capacity) {
        if(buffer!=null){
            return this;
        }
        buffer=ctx.alloc().buffer(capacity);
        return this;
    }

    @Override
    public void writeByte(int index) {

    }


    public void writeByte(int index, byte data) {
        buffer.writeByte(data);

    }

    @Override
    public void writeByte(byte data) {
        buffer.writeByte(data);

    }

    @Override
    public byte readByte() {
        return  buffer.readByte();
    }

    @Override
    public void writeInt(int data) {
        buffer.writeInt(data);

    }

    @Override
    public void writeBytes(byte[] data) {
        buffer.writeBytes(data);

    }

    @Override
    public int readableBytes() {
        return buffer.readableBytes();
    }

    @Override
    public int readInt() {
        return buffer.readInt();
    }

    @Override
    public void readBytes(byte[] dst) {
        buffer.readBytes(dst);

    }

    @Override
    public int readerIndex() {
        return buffer.readerIndex();
    }

    @Override
    public void setReaderIndex(int readerIndex) {
        buffer.setIndex(readerIndex, buffer.writerIndex());

    }
}
