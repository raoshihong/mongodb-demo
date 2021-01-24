package com.rao.mongodb.demo.service;

import com.rao.mongodb.demo.dao.Orders;
import com.rao.mongodb.demo.vo.OrderCountVo;

import java.util.List;

/**
 * @author raoshihong
 * @date 2021-01-17 23:47
 */
public interface OrderService {
    void save(Orders orders);

    Orders find(String key);

    void update(Orders orders);

    List<Orders> findByKey();

    List<Orders> match();

    List<OrderCountVo> group();
}
