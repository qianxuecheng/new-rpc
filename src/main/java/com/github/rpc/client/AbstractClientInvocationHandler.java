package com.github.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * Created by qianxuecheng on 15/9/2.
 */
public abstract class AbstractClientInvocationHandler implements InvocationHandler {
    /**
     * 现在来看这个类可能不是很重，主要是因为现在没有注册中心的概念，否则后期还会维护与注册中心的连接
     */
    private List<InetSocketAddress> servers;//cluster ?
    private String targetInstanceName;
    private int codecType;
    private int protocolType;
    private int timeout;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InetSocketAddress server;
        if(servers.size()==1){
            server=servers.get(0);
        }
        else {
            Random random=new Random();
            server=servers.get(random.nextInt(servers.size()));
        }
        Client client = getClientFactory().getClient(server.getAddress().getHostAddress(), server.getPort());
        String methodName=method.getName();
        String[] parameterTypesStrs= createParameterTypesStrs(method.getParameterTypes());
        return client.invokeSync(targetInstanceName,methodName,parameterTypesStrs,args,codecType,protocolType);
    }

    protected  String[] createParameterTypesStrs(Class<?>[] parameterTypes){
        if(parameterTypes==null||parameterTypes.length==0){
            return new String[]{};
        }
        String [] parameterTypesStrs=new String[parameterTypes.length];
        for(int i=0;i<parameterTypes.length;i++){
            parameterTypesStrs[i]=parameterTypes[i].getName();
        }
        return parameterTypesStrs;

    }


    /**
     *
     * @return
     */
   protected   abstract     ClientFactory getClientFactory();





}
