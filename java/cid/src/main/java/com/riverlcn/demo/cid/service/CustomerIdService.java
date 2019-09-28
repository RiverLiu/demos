package com.riverlcn.demo.cid.service;

/**
 * 客户号服务. 获取客户号，维护客户号的生命周期(创建，删除)
 *
 * @author Liu Jian
 */
public interface CustomerIdService {

    /**
     * 获取客户号
     *
     * @return 新生成的客户号
     */
    String getNextCustomerId();

}
