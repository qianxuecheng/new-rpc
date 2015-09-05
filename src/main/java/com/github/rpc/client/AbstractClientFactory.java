package com.github.rpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public abstract class AbstractClientFactory implements ClientFactory{
    private static ConcurrentHashMap<String,FutureTask<List<Client>>> clients=new ConcurrentHashMap<String, FutureTask<List<Client>>>();

    public Client get(final String targetIP, final int targetPort,
                      final int connectTimeout, String... customKey) throws Exception {
        return get(targetIP, targetPort, connectTimeout, 1, customKey);
    }

    public Client get(final String targetIP, final int targetPort,
                      final int connectTimeout, final int clientNums, String... customKey)
            throws Exception {
        String key = targetIP + ":" + targetPort;
        if (customKey != null && customKey.length == 1) {
            key = customKey[0];
        }
        if (clients.containsKey(key)) {
            if (clientNums == 1) {
                return clients.get(key).get().get(0);
            } else {
                Random random = new Random();
                return clients.get(key).get().get(random.nextInt(clientNums));
            }
        }
        else {
            final String cacheKey = key;
            FutureTask<List<Client>> task = new FutureTask<List<Client>>(
                    new Callable<List<Client>>() {
                        public List<Client> call() throws Exception {
                            List<Client> clients = new ArrayList<Client>(
                                    clientNums);
                            for (int i = 0; i < clientNums; i++) {
                                clients.add(createClient(targetIP, targetPort,
                                        connectTimeout, cacheKey));
                            }
                            return clients;
                        }
                    });
            FutureTask<List<Client>> currentTask = clients.putIfAbsent(key,task);
            if (currentTask == null) {
                task.run();
            }
            else {
                task = currentTask;
            }
            if (clientNums == 1)
                return task.get().get(0);
            else {
                Random random = new Random();
                return task.get().get(random.nextInt(clientNums));
            }
        }
    }
    protected abstract Client createClient(String targetIP, int targetPort,
                                           int connectTimeout, String key) throws Exception;


}
