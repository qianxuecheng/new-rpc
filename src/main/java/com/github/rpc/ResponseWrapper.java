package com.github.rpc;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class ResponseWrapper {
    private int requestId;
    private Object response;
    private byte[] responseClassName;
    private Throwable exception;
    private int codecType;
    private int protocolType;

    private  boolean isError=false;

    public boolean isError() {
        return isError;
    }

    public ResponseWrapper(int requestId,int codecType,int protocolType){
        this.requestId = requestId;
        this.codecType = codecType;
        this.protocolType = protocolType;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public byte[] getResponseClassName() {
        return responseClassName;
    }

    public void setResponseClassName(byte[] responseClassName) {
        this.responseClassName = responseClassName;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        isError=true;
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
