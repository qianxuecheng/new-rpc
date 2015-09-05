package com.github.rpc.server;

import com.github.rpc.protocol.Request;
import com.github.rpc.protocol.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianxuecheng on 15/9/6.
 */
public class RpcServerHandler implements ServerHandler{

    // Server Processors     key: servicename    value: service instance
    private static Map<String, Object> processors = new HashMap<String, Object>();

    // Cached Server Methods  key: instanceName#methodname$argtype_argtype
    private static Map<String, Method> cacheMethods = new HashMap<String, Method>();

    public  static  final RpcServerHandler instance=new RpcServerHandler();


    @Override
    public void registerProcessor(String group, String interfaceName, String version, Class<?> interfaceType,Object instance) throws Exception{
        if(!interfaceType.isInterface()){
            throw new Exception("must be interface");
        }
        String key=group+"/"+interfaceName+"/"+version;
        processors.put(key,instance);
        Method[] methods=interfaceType.getMethods();
        for(Method method:methods){
            Class<?>[] parameterTypes=method.getParameterTypes();
            StringBuilder sb=new StringBuilder();
            sb.append(key).append("#").append(method.getName()).append("$");
            for(Class<?> parameterType:parameterTypes){
                sb.append(parameterType.getName()).append("_");
            }
            cacheMethods.put(sb.toString(),method);
        }

    }

    @Override
    public Response handleRequest(Request request) {
        Response response=new Response(request.getSessionId(),request.getCodecType(),request.getProtocolType());
        String key=request.getGroup()+"/"+request.getInterfaceName()+"/"+request.getVersion();
        Object processor=processors.get(key);

        Class<?>[] parameterTypes = request.getParameterTypes();
        StringBuilder sb=new StringBuilder();
        sb.append(key).append("#").append(request.getMethodName()).append("$");
        for(Class<?> parameterType:parameterTypes){
            sb.append(parameterType.getName()).append("_");
        }
        Method method = cacheMethods.get(sb.toString());
        try {
            response.setReply(method.invoke(processor, request.getArgs()));
        } catch(Exception e){
            response.setException(e);
        }


        return response;
    }
}
