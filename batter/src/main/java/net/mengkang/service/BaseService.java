package net.mengkang.service;


import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luoxiaosong on 2018/3/10.
 */
public class BaseService {

    private static AtomicLong CONCURRENT_INTEGER = new AtomicLong(0);

    public static long getId() {
        Long id = CONCURRENT_INTEGER.getAndIncrement();
         if (id == null){
            id = CONCURRENT_INTEGER.getAndIncrement();
         }
        return id+ System.nanoTime();
    }
}
