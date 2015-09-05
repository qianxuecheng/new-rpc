package com.github.rpc.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qianxuecheng on 15/9/5.
 */
public class Request {
    private static final AtomicInteger sessionIdGenerator=new AtomicInteger(0);
    //distributed interface identifier
    private String group;
    private String interfaceName;
    private String version;

    private int codecType;
    private int protocolType;

    //session
    private int sessionId;
    private String  methodName;
    private Class<?>[] parameterTypes;//->String[] parameterTypesStrs->byte[][] parameterTypesBytes
    private Object[] args;//Codec

    public Request(String group, String interfaceName, int codecType, String version, int protocolType, int sessionId, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.group = group;
        this.interfaceName = interfaceName;
        this.codecType = codecType;
        this.version = version;
        this.protocolType = protocolType;
        this.sessionId = sessionId;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public Request(String group, String interfaceName, String version, int codecType, int protocolType,String methodName, Class<?>[] parameterTypes, Object[] args) {

        this.group = group;
        this.interfaceName = interfaceName;
        this.version = version;
        this.codecType = codecType;
        this.protocolType = protocolType;
        this.sessionId = sessionIdGenerator.incrementAndGet();
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
