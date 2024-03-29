package com.aliware.tianchi;

import org.apache.dubbo.rpc.Invoker;

import java.util.concurrent.atomic.AtomicInteger;

public class HiveInvokerInfo {
    String name;
    Invoker invoker;

    volatile int maxPendingRequest = 0;
    AtomicInteger pendingRequest = new AtomicInteger(0);

    volatile int totalTime = 0;
    volatile int totalRequest = 0;
    volatile int rtt = 0;

    volatile int maxConcurrency = 0;

    volatile double weight = 0;
    volatile double weightInitial = 0;
//    volatile double currentWeight = 0;

    volatile boolean stressed = false;

    public HiveInvokerInfo(Invoker invoker) {
        String host = invoker.getUrl().getHost();
        int start = host.indexOf('-');
        this.name = host.substring(start + 1);
        this.invoker = invoker;
    }

    public void setTotalTime(int totalTime) {
        if (totalTime > this.totalTime) {
            this.totalTime = totalTime;
        }
    }

    public void setTotalRequest(int totalRequest) {
        if (totalRequest > this.totalRequest) {
            this.totalRequest = totalRequest;
        }
    }

    public void setMaxConcurrency(int maxConcurrency) {
        if (maxConcurrency > this.maxConcurrency) {
            this.maxConcurrency = maxConcurrency;
        }
    }

    @Override
    public String toString() {
        return "HiveInvokerInfo{" +
                "name='" + name + '\'' +
                ", pendingRequest=" + pendingRequest +
                ", rtt=" + rtt +
                ", totalTime=" + totalTime +
                ", totalRequest=" + totalRequest +
                ", maxConcurrency=" + maxConcurrency +
                ", weight=" + weight +
                '}';
    }
}
