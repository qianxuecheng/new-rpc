package com.github.rpc.client;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.github.rpc.RequestWrapper;
import com.github.rpc.ResponseWrapper;
import com.github.rpc.codec.CodecFactory;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public abstract class AbstractClient implements Client{
    public ConcurrentHashMap<Integer,ArrayBlockingQueue<Object>/**ResponseWrapper or List<ResponseWrapper>*/> responses=new ConcurrentHashMap<Integer, ArrayBlockingQueue<Object>>();
    @Override
    public Object invokeSync(String targetInstanceName, String methodName, String[] parameterTypeStrs, Object[] args, int codecType, int protocolType) throws Exception{
        byte[][] parameterTypesBytes=new byte[parameterTypeStrs.length][];
        for(int i=0;i<parameterTypeStrs.length;i++){
            parameterTypesBytes[i]=parameterTypeStrs[i].getBytes();
        }


        return invokeSyncIntern(new RequestWrapper(methodName.getBytes(),parameterTypesBytes,args,codecType,protocolType,targetInstanceName.getBytes()));
    }

    private Object invokeSyncIntern(RequestWrapper requestWrapper) throws Exception {
        ArrayBlockingQueue<Object> response=new ArrayBlockingQueue<Object>(1);
        responses.put(requestWrapper.getId(),response);
        ResponseWrapper respnseWrapper=null;
        //TODO 流量控制
        sendRequest(requestWrapper);

        Object result=response.poll();
        if(result instanceof ResponseWrapper){
            respnseWrapper= (ResponseWrapper) result;
        }else if(result instanceof List){
            @SuppressWarnings("unchecked")
            List<ResponseWrapper> responseWrappers= (List<ResponseWrapper>) result;
            for (ResponseWrapper response2:responseWrappers){
                if(response2.getRequestId()==requestWrapper.getId()){
                    respnseWrapper=response2;
                }
                else {
                    putResponse(response2);

                }
            }


        }
        //do deserialize in business thread pool

        if(respnseWrapper.getResponse() instanceof byte[]){
            String responseClassName=respnseWrapper.getResponseClassName()!=null?new String(respnseWrapper.getResponseClassName()):null;
            if(((byte[]) respnseWrapper.getResponse()).length==0){
                respnseWrapper.setResponse(null);
            }
            else {
                Object responseObject= CodecFactory.getDecoder(CodecFactory.CodecType.JDK).
                        decode(responseClassName, (byte[]) respnseWrapper.getResponse());
                if(responseObject instanceof Throwable){
                    respnseWrapper.setException((Throwable) responseObject);
                }else {
                    respnseWrapper.setResponse(responseObject);
                }

            }


        }
        if(respnseWrapper.isError()){
            Throwable t=respnseWrapper.getException();
            throw  new Exception(t);

        }
        return respnseWrapper.getResponse();
    }

    @Override
    public void putResponse(ResponseWrapper response) throws Exception {
        if(!responses.containsKey(response.getRequestId())){
            return;
        }

        ArrayBlockingQueue<Object> responseQueue=responses.get(response.getRequestId());
        if(responseQueue!=null){
            responseQueue.put(response);
        }

    }

    @Override
    public void putResponses(List<ResponseWrapper> responses) throws Exception {
        for(ResponseWrapper responseWrapper:responses){
            putResponse(responseWrapper);
        }

    }

    public abstract void sendRequest(RequestWrapper wrapper) throws Exception;

}
