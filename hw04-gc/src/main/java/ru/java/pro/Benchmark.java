package ru.java.pro;


import java.util.ArrayList;
import java.util.List;

import static java.lang.Runtime.getRuntime;

class Benchmark implements BenchmarkMBean {
    private volatile int size = 0;
    private List<Object[]> list = new ArrayList<>();

    void run() {
        for (int i = 0; ; i++) {
            list.add(new Object[size]);
            if (i % 2 == 0) {
                list = list.subList(0, list.size() / 2);
            }
            if (i % 10000 == 0) {
                System.out.printf("Memory left: %s of %s mb%n",
                        getRuntime().freeMemory() * 1e-6,
                        getRuntime().totalMemory() * 1e-6);
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }
}
