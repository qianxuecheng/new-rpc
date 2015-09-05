package com.github.rpc.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.rpc.codec.CodecFactory;
import io.netty.buffer.ByteBuf;

/**
 * Created by qianxuecheng on 15/9/5.
 * Prototype
 */
public class RpcProtocol extends AbstractProtocol {
    //why version see GET HTTP/1.1
    private static final byte MAJOR=1;//high 4 bit
    private static final byte MINOR=1;//low 4 bit
    private static final byte protocolVersion=MAJOR<<4+MINOR;

    private static final int CODEC_TYPE_LENGTH=1;
    private static final int PROTOCOL_TYPE_LENGTH=1;
    private static final byte[] NO_BYTE=new byte[0];

    private static final int SESSION_ID_LENGTH =4;
    private static final int INTERFACE_GROUP_LENGTH=4;
    private static final int INTERFACE_NAME_LENGTH=4;
    private static final int INTERFACE_VERSION_LENGTH=4;
    private static final int METHOD_NAME_LENGTH=4;
    private static final int METHOD_PARAMETERS_LENGTH=4;
    private static final int ATTACHMENT_LENGTH=4;
    private static final int CLASSNAME_LENGTH=4;
    private static final int VALUE_LENGTH=4;
    private static final int REQUEST_HEADER_LENGTH=
            PROTOCOL_VERSION_LENGTH+
                    SESSION_ID_LENGTH +
                    TYPE_LENGTH+
                    CODEC_TYPE_LENGTH+
                    PROTOCOL_TYPE_LENGTH+
                    TOTAL_LENGTH +
                    INTERFACE_GROUP_LENGTH+
                    INTERFACE_NAME_LENGTH+
                    INTERFACE_VERSION_LENGTH+
                    METHOD_NAME_LENGTH+
                    METHOD_PARAMETERS_LENGTH+
                    ATTACHMENT_LENGTH;
    private static final int RESPONSE_HEADER_LENGTH=PROTOCOL_TYPE_LENGTH+
            TOTAL_LENGTH +
            TYPE_LENGTH+
            SESSION_ID_LENGTH +
            CODEC_TYPE_LENGTH+PROTOCOL_TYPE_LENGTH+
            CLASSNAME_LENGTH+
            VALUE_LENGTH+
            ATTACHMENT_LENGTH;


    @Override
    protected int getProtocolVersion() {
        return protocolVersion ;
    }

    @Override
    protected Object decodeResponse(ByteBuf byteBuf) throws Exception{

        int sessionId=byteBuf.readInt();
        int codecType=byteBuf.readByte();
        int protocolType=byteBuf.readByte();
        int classNameLength=byteBuf.readInt();
        int replyLength=byteBuf.readInt();
        byte[] dst;
        byteBuf.readBytes(dst=new byte[classNameLength]);
        String className=new String(dst);
        byteBuf.readBytes(dst=new byte[replyLength]);
        Response response=new Response(sessionId,codecType,protocolType);
        Object result=CodecFactory.getDecoder(CodecFactory.CodecType.getCodecType(codecType)).decode(className, dst);
        if(result instanceof Throwable){
            response.setException((Throwable) result);
        }else {
            response.setReply(result);
        }
        return response;
    }

    @Override
    protected Object decodeRequest(ByteBuf byteBuf) throws Exception{//
        int sessionId=byteBuf.readInt();
        int codeType=byteBuf.readByte();
        int protocolType=byteBuf.readByte();

        int interfaceGroupLength=byteBuf.readInt();
        int interfaceNameLength=byteBuf.readInt();
        int interfaceVersionLength=byteBuf.readInt();
        int methodNameLength=byteBuf.readInt();
        int parameterNumber=byteBuf.readInt();
        //TODO
        int attachmentNumber=byteBuf.readInt();
        byte[] dst;
        byteBuf.readBytes(dst=new byte[interfaceGroupLength]);
        String interfaceGroup=new String(dst);
        byteBuf.readBytes(dst=new byte[interfaceNameLength]);
        String interfaceName=new String(dst);
        byteBuf.readBytes(dst=new byte[interfaceVersionLength]);
        String version=new String(dst);
        byteBuf.readBytes(dst=new byte[methodNameLength]);
        String methodName=new String(dst);

        List<String> parameterTypesStrs=new ArrayList<String>(parameterNumber);
        List<byte[]> argBytes=new ArrayList<byte[]>();
        for(int i=0;i<parameterNumber;i++){
            byteBuf.readBytes((dst=new byte[byteBuf.readInt()]));
            parameterTypesStrs.add(new String(dst));
        }
        for(int i=0;i<parameterNumber;i++){
            byteBuf.readBytes((dst=new byte[byteBuf.readInt()]));
            argBytes.add(dst);
        }
        Class<?>[] parameterTypes=new Class<?>[parameterNumber];
        Object[] args=new Object[parameterNumber];
        for(int i=0;i<parameterNumber;i++){
            parameterTypes[i]=Class.forName(parameterTypesStrs.get(i));
            args[i]=CodecFactory.getDecoder(CodecFactory.CodecType.getCodecType(codeType))
                    .decode(parameterTypesStrs.get(i), argBytes.get(i));

        }
        Map<String,String> attachments=new HashMap<String,String>(attachmentNumber);
        for(int i=0;i<attachmentNumber;i++){
            byteBuf.readBytes(dst = new byte[byteBuf.readInt()]);
            String key=new String(dst);
            byteBuf.readBytes(dst = new byte[byteBuf.readInt()]);
            String value=new String(dst);
            attachments.put(key,value);
        }

        //TODO attachment
        Request request=new Request(interfaceGroup,interfaceName,codeType,version,protocolType,sessionId,methodName,parameterTypes,args);


        return request;
    }

    @Override
    protected ByteBuf encodeResponse(Response msg, ByteBuf byteBuf) throws Exception{
        int length=RESPONSE_HEADER_LENGTH;
        byte[] className=NO_BYTE;
        byte[] value=NO_BYTE;
        if(msg.getReply()!=null){
            className=msg.getReply().getClass().getName().getBytes();
            value=CodecFactory.getEncoder(CodecFactory.CodecType.getCodecType(msg.getCodecType())).encode(msg.getReply());
        }
        if(msg.getException()!=null){
            className=msg.getException().getClass().getName().getBytes();
            value=CodecFactory.getEncoder(CodecFactory.CodecType.Jdk).encode(msg.getException());
        }
        byteBuf.writeByte(protocolVersion);
        byteBuf.writeInt(length);
        byteBuf.writeByte(RESPONSE_TYPE);


        byteBuf.writeInt(msg.getSessionId());
        byteBuf.writeByte(msg.getCodecType());
        byteBuf.writeByte(msg.getProtocolType());

        byteBuf.writeInt(className.length);
        byteBuf.writeInt(value.length);
        byteBuf.writeBytes(className);
        byteBuf.writeBytes(value);
        return byteBuf;
    }

    @Override
    protected ByteBuf encodeRequest(Request msg, ByteBuf byteBuf) throws Exception{
        //协议头长度
        int length=REQUEST_HEADER_LENGTH;//总长度=header+body
        byte codecType= (byte) msg.getCodecType();
        byte protocolType= (byte) msg.getProtocolType();
        byte[] interfaceGroup=msg.getGroup().getBytes();
        byte[] interfaceVersion=msg.getVersion().getBytes();
        byte[] interfaceName=msg.getInterfaceName().getBytes();//含义 index-based 空间浪费
        length+=interfaceGroup.length;
        length+=interfaceVersion.length;
        length+=interfaceName.length;
        List<byte[]> parameterTypes=new ArrayList<byte[]>();
        List<byte[]> args=new ArrayList<byte[]>();
        byte[] temp;
        for(Class<?> parameterType:msg.getParameterTypes()){
            parameterTypes.add(temp=parameterType.getName().getBytes());
            length+=temp.length;
        }
        for(Object arg:msg.getArgs()){
            args.add(temp=CodecFactory.getEncoder(CodecFactory.CodecType.Jdk).encode(arg));
            length+=temp.length;
        }
        //TODO attachment
        Map<String,String> attachments=new HashMap<String,String>();

        //理论上讲这个值在父类中完成就OK了
        byteBuf.writeByte(protocolVersion);
        byteBuf.writeInt(length);
        byteBuf.writeByte(REQUEST_TYPE);

        byteBuf.writeInt(msg.getSessionId());
        byteBuf.writeByte(codecType);
        byteBuf.writeByte(protocolType);

        byteBuf.writeInt(interfaceGroup.length);
        byteBuf.writeInt(interfaceName.length);
        byteBuf.writeInt(interfaceVersion.length);
        byteBuf.writeInt(msg.getMethodName().getBytes().length);
        byteBuf.writeInt(parameterTypes.size());
        byteBuf.writeInt(attachments.size());


        byteBuf.writeBytes(interfaceGroup);
        byteBuf.writeBytes(interfaceName);
        byteBuf.writeBytes(interfaceVersion);
        byteBuf.writeBytes(msg.getMethodName().getBytes());
        for(byte[] parameterType:parameterTypes){
            byteBuf.writeInt(parameterType.length);
            byteBuf.writeBytes(parameterType);
        }
        for(byte[] arg:args){
            byteBuf.writeInt(arg.length);
            byteBuf.writeBytes(arg);
        }
        //TODO 附件使用 RpcContext
        for(Map.Entry<String,String> attachment:attachments.entrySet()){
            String key=attachment.getKey();
            String value=attachment.getValue();
            byteBuf.writeInt(key.getBytes().length);
            byteBuf.writeBytes(key.getBytes());
            byteBuf.writeInt(value.getBytes().length);
            byteBuf.writeBytes(value.getBytes());
        }
        return byteBuf;
    }
}

