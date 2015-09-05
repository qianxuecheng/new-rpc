package com.github.rpc.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by qianxuecheng on 15/9/5.
 *结构化数据
 */



public abstract class AbstractProtocol implements Protocol{//SQLObject 有点AST抽象语法树的赶脚
    protected static final byte REQUEST_TYPE=1;
    protected static final byte RESPONSE_TYPE=2;

    //长度单位字节1B
    protected static final int PROTOCOL_VERSION_LENGTH=1;

    protected static final int TOTAL_LENGTH=4;

    protected static final int TYPE_LENGTH=1;
    protected static final int COMMON_HEADER_LENGTH=PROTOCOL_VERSION_LENGTH+TOTAL_LENGTH+TYPE_LENGTH;

    /**
     *
     * @return
     */
    protected abstract int getProtocolVersion();

    /**
     * 模板设计
     * @param byteBuf
     * @return
     * @throws Exception
     */


    @Override
    public Object decode(ByteBuf byteBuf) throws Exception {
        int originPos=byteBuf.readerIndex();
        try {
            //Preconditions前置条件
            if(byteBuf.readableBytes()<COMMON_HEADER_LENGTH){
                throw new Exception("Expect More Data!");
            }
            if(byteBuf.readByte()!=getProtocolVersion()){
                throw new Exception("Unsupported Protocol Version!");
            }
            if (byteBuf.readableBytes()<byteBuf.readableBytes()-PROTOCOL_VERSION_LENGTH-TOTAL_LENGTH){
                throw new Exception("Application data is not full");
            }
            int type=byteBuf.readByte();
            if(type==REQUEST_TYPE){
                return decodeRequest(byteBuf);
            }else if(type==RESPONSE_TYPE){
                return decodeResponse(byteBuf);
            }
            throw new Exception("Expect 'type' in 1 or 2 actual "+type);

        } catch (Exception e) {//努力使失败保持原子性
            byteBuf.setIndex(originPos, byteBuf.writerIndex());
            throw e;
        }
    }

    @Override
    public ByteBuf encode(Object msg, ByteBuf byteBuf) throws Exception {
        if(msg instanceof Request){

            return  encodeRequest((Request) msg,byteBuf);
        }else if(msg instanceof Response){
            return encodeResponse((Response) msg,byteBuf);
        }
        throw new Exception("Expected msg instance of 'Request' or 'Response'");

    }

    protected abstract Object decodeResponse(ByteBuf byteBuf)throws  Exception;

    protected abstract Object decodeRequest(ByteBuf byteBuf)throws Exception;

    protected abstract ByteBuf encodeResponse(Response msg, ByteBuf byteBuf)throws Exception;

    protected abstract ByteBuf encodeRequest(Request msg, ByteBuf byteBuf) throws Exception;
}
