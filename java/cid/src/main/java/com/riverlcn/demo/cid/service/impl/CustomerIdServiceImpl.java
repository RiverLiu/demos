package com.riverlcn.demo.cid.service.impl;


import com.riverlcn.demo.cid.service.CustomerIdService;
import com.riverlcn.demo.cid.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


@Slf4j
@Service
public class CustomerIdServiceImpl implements CustomerIdService {

    // CID 转换成数字之后的 最大值
    private static final Integer CID_MAX_VALUE = 100000;
    private static final int CID_INC_MAGIC_OFFSET = 13;
    private static final int CID_CACHE_COUNT = 10;

    private static final String cidListCacheKey = "cid:keys";
    private static  final String cidLockKey = "cid:lock";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        generateCidCacheList();
    }

    /**
     * 生成客户号列表.
     */
    private void generateCidCacheList() {
        log.info("Generate cid cache");

        // 只负责生成，不维护数量
        Long size = redisTemplate.opsForList().size(cidListCacheKey);
        if (size > 0) {
            return;
        }

        // 获取已经存在的客户号, 转换成 set, 利用索引进行查找
        List<String> cids = getCidsFromDb();
        if (cids.size() >= CID_MAX_VALUE) {
            throw new RuntimeException("客户号过多，请联系系统管理员");
        }
        HashSet<String> cidSet = new HashSet<>(cids);

        // 使用全局锁进行锁定，然后设置值
        RedisLock lock = new RedisLock(redisTemplate, cidLockKey);
        try {
            if (lock.lock()) {
                // check again
                size = redisTemplate.opsForList().size(cidListCacheKey);
                if (size > 0) {
                    return;
                }

                Random random = new Random();
                Set<Object> ids = new HashSet<>(10);
                while (ids.size() < CID_CACHE_COUNT) {
                    // 获取不重复的值
                    int num = random.nextInt(CID_MAX_VALUE);
                    String id  = String.format("%05d", num);
                    int tries = cidSet.size();
                    while (tries > 0) {
                        if (!cidSet.contains(id)) {
                            break;
                        }
                        num =  (num + CID_INC_MAGIC_OFFSET) % CID_MAX_VALUE;
                        id = String.format("%05d", num);
                        --tries;
                    }

                    ids.add(String.format("%05d", random.nextInt(CID_MAX_VALUE)));
                }

                redisTemplate.opsForList().rightPushAll(cidListCacheKey, ids);
            }
        } catch (InterruptedException e) {
            throw new InternalError("Cannot generate cid", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从数据库中获取客户号列表.
     * @return 客户号列表
     */
    private List<String> getCidsFromDb() {
        //TODO 从数据库获取客户号
        return new ArrayList<String>(0);
    }

    /**
     * 将获取到的客户号，保存到数据库
     */
    private void saveCidToDb(String cid) {
        //TODO 保存客户号到数据库
    }

    /**
     * 获取客户号. 内部创建使用
     * @return 新生成的客户号，后续连接的 app key， app secret
     */
    @Override
    public String getNextCustomerId() {
        String cid = getNextCid();
        saveCidToDb(cid);
        return cid;
    }

    /**
     * 获取下一个客户的 cid. 如果缓存中，不存在记录，则从重新生成cid缓存.
     * @return 下一个客户的 cid.
     */
    private String getNextCid() {
        int tries = 3;
        while (tries > 0) {
            Object obj = redisTemplate.opsForList().leftPop(cidListCacheKey);
            if (obj != null) {
                return obj.toString();
            }
            generateCidCacheList();
            --tries;
        }
        throw new InternalError("系统繁忙，请稍后重试");
    }

}
