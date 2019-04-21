package com.riverlcn.demo.cid.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;

/**
 * Redis 分布式锁.
 * 参考 <a href="http://www.cnblogs.com/0201zcr/p/5942748.html">Redis 分布式锁</a>
 *
 * @author Liujian
 */
public class RedisLock {
    
    private RedisTemplate<String, Object> redisTemplate;
    private static final int DEFAULT_ACQUIRED_RESOLUTION_MILLIS = 100;

    // 锁超时时间，防止线程在获取锁后，无限的执行等待
    private static final int expireTimeout = 20 * 1000;

    // 锁等待时间，防止线程饥饿
    private static final int waitTimeout = 10 * 1000;

    private volatile boolean locked = false;
    private String lockKey;

    /**
     * Detailed constructor with default acquire timeout 10000  and lock expiration of 20000 .
     *
     * @param lockKey lock key (ex. account:1, ...)
     */
    public RedisLock(RedisTemplate<String, Object> redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
    }

    private Long get(final String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj instanceof Long) {
            return (Long)obj;
        } else {
            return null;
        }
    }

    private boolean setNX(final String key, final Long value) {
        Object obj = redisTemplate.opsForValue().setIfAbsent(key, value);
        return obj != null ? (Boolean) obj : false;
    }

    private Long getSet(final String key, final Long value) {
        Object obj = redisTemplate.opsForValue().getAndSet(key, value);
        if (obj instanceof Long) {
            return (Long)obj;
        } else {
            return null;
        }
    }

    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids 缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    public synchronized boolean lock() throws InterruptedException {
        int timeout = waitTimeout;
        Random random = new Random();
        while (timeout >= 0) {
            
            // 获取锁，如果使用 setNx 可以得到锁，则认为锁定
            Long expire = System.currentTimeMillis() + expireTimeout + 1;
            if (setNX(lockKey, expire)) {
                locked = true;
                return true;
            }

            // 检查锁是否到期
            // 如果到期，则重新设置时间，注意，其余 application 可能会同时有这一步的操作
            // 如果设置返回的时间与原有时间相同，则认为获取锁成功
            Long currentValue = get(lockKey);
            if (currentValue != null && currentValue < System.currentTimeMillis()) {
                Long oldValue = getSet(lockKey, expire);
                if (oldValue != null && oldValue.equals(currentValue)) {
                    locked = true;
                    return true;
                }
            }

            // 等待一段时间再次获取锁，防止饥饿线程出现
            timeout -= random.nextInt(DEFAULT_ACQUIRED_RESOLUTION_MILLIS);
            wait(DEFAULT_ACQUIRED_RESOLUTION_MILLIS);

        }
        return false;
    }


    /**
     * 使用锁后，释放锁.
     */
    public synchronized void unlock() {
        if (locked) {
            redisTemplate.delete(lockKey);
            locked = false;
        }
    }

}