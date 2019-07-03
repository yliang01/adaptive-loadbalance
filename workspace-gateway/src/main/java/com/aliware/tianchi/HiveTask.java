package com.aliware.tianchi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HiveTask implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                long start =System.currentTimeMillis();
                List<HiveInvokerInfo> values = new ArrayList<>(UserLoadBalance.infoMap.values());
                for (int i = 0; i < values.size(); i++) {
                    HiveInvokerInfo hiveInvokerInfo = values.get(i);
                    int lastAverage = 0;
                    hiveInvokerInfo.stressCoefficient = 0.5;
                    UserLoadBalance.stressInvokerInfo = hiveInvokerInfo;
                    for (int j = 0; j < 25; j++) {
                        UserLoadBalance.stress = true;
                        HiveFilter.stress = true;
                        Thread.sleep(2);
                        UserLoadBalance.stress = false;
                        HiveFilter.stress = false;
                        int rttTotalCount = hiveInvokerInfo.rttTotalCount.get();
                        if (rttTotalCount == 0) {
                            continue;
                        }
                        int average = hiveInvokerInfo.rttTotalTime.get() / hiveInvokerInfo.rttTotalCount.get();
                        hiveInvokerInfo.rttTotalTime.updateAndGet(x -> 0);
                        hiveInvokerInfo.rttTotalCount.updateAndGet(x -> 0);
                        if (lastAverage == 0) {
                            lastAverage = average;
                        } else {
                            if (average > lastAverage * 1.5) {
                                break;
                            }
                        }
                        hiveInvokerInfo.stressCoefficient += 0.02;
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println(end-start);
                for (HiveInvokerInfo info : values) {
                    System.out.println(info.stressCoefficient);
                }
                Thread.sleep(1000);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}
