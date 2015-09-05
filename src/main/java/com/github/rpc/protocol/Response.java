package com.github.rpc.protocol;

/**
 * Created by qianxuecheng on 15/9/5.
 */
public class Response {
    private int sessionId;
    private Object reply;
    private Class<?> clazz;//reply or exception
    private Throwable exception;

    private int codecType;
    private int protocolType;

    public Response(int sessionId, int codecType, int protocolType) {
        this.sessionId = sessionId;
        this.codecType = codecType;
        this.protocolType = protocolType;
    }

    public Object reCreate() throws Exception{
        if(reply!=null){
            return reply;
        }
        throw new Exception(exception);

    }

    public int getSessionId() {
        return sessionId;
    }

    public Object getReply() {
        return reply;
    }

    public void setReply(Object reply) {
        this.reply = reply;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public int getCodecType() {
        return codecType;
    }

    public void setCodecType(int codecType) {
        this.codecType = codecType;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }
}
