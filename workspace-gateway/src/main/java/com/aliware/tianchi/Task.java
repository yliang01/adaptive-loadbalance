package com.aliware.tianchi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class Task implements Runnable {
    Map<String, Integer> map = new HashMap<>();
    private static boolean mess = true;

    @Override
    public void run() {
        while (true) {
            ConcurrentMap<String, Integer> weightMap = UserLoadBalance.weightMap;
            ConcurrentMap<String, Boolean> exhaustedMap = TestClientFilter.exhaustedMap;
            if (TestClientFilter.startCheck) {
                TestClientFilter.startCheck = false;
                if (exhaustedMap.size() > 0 && exhaustedMap.size() < weightMap.size()) {
                    Set<String> changeKeys = new HashSet<>();
                    Set<String> exhaustedKeys = exhaustedMap.keySet();
                    Set<String> weightKeys = weightMap.keySet();
                    for (String key : weightKeys) {
                        if (!exhaustedKeys.contains(key)) {
                            changeKeys.add(key);
                        }
                    }

                    int total = 0;
                    Set<Map.Entry<String, Boolean>> entries = exhaustedMap.entrySet();
                    for (Map.Entry<String, Boolean> entry : entries) {
                        if (entry.getValue()) {
                            int weight = weightMap.get(entry.getKey());
                            weightMap.put(entry.getKey(), weight - 2);
                            total += 2;
                        }
                    }
                    while (total > 0) {
                        for (String key : changeKeys) {
                            if (total > 0) {
                                int weight = weightMap.get(key);
                                weightMap.put(key, weight + 1);
                                total -= 1;
                            }
                        }
                    }
                } else if (exhaustedMap.size() == 0) {

                    int size = weightMap.size();
                    List<String> keyList = new ArrayList<>(weightMap.keySet());
                    for (int i = 0; i < size; i++) {
                        int random = ThreadLocalRandom.current().nextInt(size);
                        weightMap.compute(keyList.get(random), (k, v) -> v + 2);
                    }
                    for (int i = 0; i < size; i++) {
                        int random = ThreadLocalRandom.current().nextInt(size);
                        weightMap.compute(keyList.get(random), (k, v) -> v - 2);
                    }

                }


            } else {
                TestClientFilter.startCheck = true;
                TestClientFilter.exhaustedMap = new ConcurrentHashMap<>();
            }

            System.out.println(weightMap);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
