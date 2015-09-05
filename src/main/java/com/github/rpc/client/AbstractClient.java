package com.github.rpc.client;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.github.rpc.protocol.Request;
import com.github.rpc.protocol.Response;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public abstract class AbstractClient implements Client{
    public ConcurrentHashMap<Integer,ArrayBlockingQueue<Object>/**ResponseWrapper or List<ResponseWrapper>*/> responses=new ConcurrentHashMap<Integer, ArrayBlockingQueue<Object>>();
    @Override
    public Object invokeSync(Request request) throws Exception{
    /*    byte[][] parameterTypesBytes=new byte[parameterTypeStrs.length][];
        for(int i=0;i<parameterTypeStrs.length;i++){
            parameterTypesBytes[i]=parameterTypeStrs[i].getBytes();
        }*/


        return invokeSyncIntern(request);
    }

    private Object invokeSyncIntern(Request request) throws Exception {
        ArrayBlockingQueue<Object> responseQueue=new ArrayBlockingQueue<Object>(1);
        responses.put(request.getSessionId(),responseQueue);
        Response response=null;
        //TODO 流量控制
        sendRequest(request);

        Object result=responseQueue.poll(100, TimeUnit.SECONDS);
        if(result instanceof Response){
            response= (Response) result;
        }else if(result instanceof List){
            @SuppressWarnings("unchecked")
            List<Response> responses = (List<Response>) result;
            for (Response response2: responses){
                if(response2.getSessionId()==request.getSessionId()){
                    response=response2;
                }
                else {
                    putResponse(response2);

                }
            }


        }

        //do deserialize in business thread pool
/*
        if(re.getResponse() instanceof byte[]){
            String responseClassName=respnseWrapper.getResponseClassName()!=null?new String(respnseWrapper.getResponseClassName()):null;
            if(((byte[]) respnseWrapper.getResponse()).length==0){
                respnseWrapper.setResponse(null);
            }
            else {
                Object responseObject= CodecFactory.getDecoder(CodecFactory.CodecType.Jdk).
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

        }*/
        return response.reCreate();
    }

    @Override
    public void putResponse(Response response) throws Exception {
        if(!responses.containsKey(response.getSessionId())){
            return;
        }

        ArrayBlockingQueue<Object> responseQueue=responses.get(response.getSessionId());
        if(responseQueue!=null){
            responseQueue.put(response);
        }

    }

    @Override
    public void putResponses(List<Response> responses) throws Exception {
        for(com.github.rpc.protocol.Response response:responses){
            putResponse(response);
        }

    }

    public abstract void sendRequest(Request wrapper) throws Exception;

}
