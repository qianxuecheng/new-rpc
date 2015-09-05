package com.github.rpc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class RequestWrapper {
    private static final AtomicInteger invocationId=new AtomicInteger(0);
    private int id;
    private byte[] targetInstanceName;
    private byte[] methodName;
    private byte[][] parameterTypesBytes;
    private Object[] args;
    private int codeType;
    private int protocolType;


    public RequestWrapper(byte[] methodName, byte[][] parameterTypesBytes,Object[] args, int codeType, int protocolType, byte[] targetInstanceName) {
        id=invocationId.incrementAndGet();
        this.args=args;
        this.methodName = methodName;
        this.parameterTypesBytes = parameterTypesBytes;
        this.codeType = codeType;
        this.protocolType = protocolType;
        this.targetInstanceName = targetInstanceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getTargetInstanceName() {
        return targetInstanceName;
    }

    public void setTargetInstanceName(byte[] targetInstanceName) {
        this.targetInstanceName = targetInstanceName;
    }

    public byte[] getMethodName() {
        return methodName;
    }

    public void setMethodName(byte[] methodName) {
        this.methodName = methodName;
    }

    public byte[][] getParameterTypesBytes() {
        return parameterTypesBytes;
    }

    public void setParameterTypesBytes(byte[][] parameterTypesBytes) {
        this.parameterTypesBytes = parameterTypesBytes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }
}
