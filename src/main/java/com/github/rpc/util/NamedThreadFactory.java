package com.github.rpc.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadFactoryNumber=new AtomicInteger(1);
    private final  AtomicInteger threadNumber=new AtomicInteger(1);
    private String namePrefix;
    private final ThreadGroup group;
    private boolean isDaemon;
    public NamedThreadFactory(String prefix){
        this(prefix,false);
    }
    public NamedThreadFactory(String prefix,boolean daemon) {
        SecurityManager s;
        group=((s=System.getSecurityManager())!=null?s.getThreadGroup():Thread.currentThread().getThreadGroup());
        isDaemon =daemon;
        this.namePrefix= prefix+"-"+threadFactoryNumber.incrementAndGet()+"-thread-" ;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t=new Thread(group,r,namePrefix+threadNumber.incrementAndGet(),0);
        t.setDaemon(isDaemon);
        return t;
    }
}
