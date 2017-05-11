
package com.xz.activeplan.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**进程管理<br>
 * 线程池-生产线程
 */
public class ThreadManager {
    /**
     * 最大队列长度
     */
    private static final int MAX_QUEUE_LENGTH = 128;

    /**
     * 常驻内在线程数
     */
    private static final int ALIVE_THREAD_SIZE = 2;

    /**
     * 最大活动线程数
     */
    private static final int MAX_THREAD_SIZE = 5;

    /**
     * 线程空置多长时间销毁
     */
    private static final int THREAD_ALIVE_SECONDS = 60;

    /**
     * 线程池
     */
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(ALIVE_THREAD_SIZE,
            MAX_THREAD_SIZE, THREAD_ALIVE_SECONDS, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(MAX_QUEUE_LENGTH),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 取得线程池实例
     * 
     * @return
     */
    public static final ThreadPoolExecutor getPool() {
        return threadPool;
    }
}
