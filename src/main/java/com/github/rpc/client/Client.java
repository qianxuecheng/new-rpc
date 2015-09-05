package com.github.rpc.client;

import com.github.rpc.protocol.Request;
import com.github.rpc.protocol.Response;

import java.util.List;

/**
 * Created by qianxuecheng on 15/9/2.
 */
public interface Client {

    @SuppressWarnings("unused")
    public Object invokeSync(Request request)throws Exception;


    /**
     * receive response from server
     */
    public void putResponse(Response response) throws Exception;

    /**
     * receive responses from server
     */
    public void putResponses(List<Response> responses) throws Exception;

}
