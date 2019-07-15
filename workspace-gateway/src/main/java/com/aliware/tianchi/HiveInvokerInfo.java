package com.aliware.tianchi;

import org.apache.dubbo.rpc.Invoker;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HiveInvokerInfo {
    String name;
    Invoker invoker;

    AtomicLong pendingRequest = new AtomicLong(0);
    volatile int maxPendingRequest = 0;


    //    AtomicLong totalTime = new AtomicLong(0);
//    AtomicLong totalRequest = new AtomicLong(0);
    volatile long totalTime = 0;
    volatile long totalRequest = 0;

    volatile double rttAverage = 0;
    double rttAverageUpper = 0;
    double rttAverageDowner = 0;

    volatile double weight = 0;
    volatile double weightInitial = 0;
    volatile double currentWeight = 0;
    volatile double weightTop = 0;

    ReadWriteLock lock = new ReentrantReadWriteLock();


    public HiveInvokerInfo(Invoker invoker) {
        String host = invoker.getUrl().getHost();
        int start = host.indexOf('-');
        this.name = host.substring(start + 1);
        this.invoker = invoker;
    }



//    volatile double maxRequestCoefficient = 1;

}
