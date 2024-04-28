package top.aixcert.materialdemo.utils;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Response;

public class RateLimitInterceptor implements Interceptor {

    private final Queue<Long> requestTimes = new ArrayDeque<>();
    private final int maxRequests;
    private final long intervalMillis;

    public RateLimitInterceptor(int maxRequests, int intervalSeconds) {
        this.maxRequests = maxRequests;
        this.intervalMillis = TimeUnit.SECONDS.toMillis(intervalSeconds);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        long now = System.currentTimeMillis();

        synchronized (requestTimes) {
            // 移除过期的请求时间
            while (!requestTimes.isEmpty() && requestTimes.peek() < now - intervalMillis) {
                requestTimes.poll();
            }

            // 如果请求数超过限制，等待一段时间
            while (requestTimes.size() >= maxRequests) {
                try {
                    requestTimes.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            requestTimes.offer(now);
            requestTimes.notifyAll();
        }

        return chain.proceed(chain.request());
    }
}
