package com.github.rpc.client;

/**
 * Created by qianxuecheng on 15/9/2.
 *  API   Singleton ThreadSafe
 */
public interface ClientFactory {
    public Client getClient(String targetIP,int targetPort) throws Exception;

}
