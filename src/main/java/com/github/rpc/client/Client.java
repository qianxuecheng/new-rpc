package com.github.rpc.client;

import com.github.rpc.ResponseWrapper;

import java.util.List;

/**
 * Created by qianxuecheng on 15/9/2.
 */
public interface Client {

    @SuppressWarnings("unused")
    public Object invokeSync(String targetInstanceName,String methodName,
                             String[] parameterTypeStrs,Object[] args,
                             int codecType,int protocolType)throws Exception;


    /**
     * receive response from server
     */
    public void putResponse(ResponseWrapper response) throws Exception;

    /**
     * receive responses from server
     */
    public void putResponses(List<ResponseWrapper> responses) throws Exception;

}
