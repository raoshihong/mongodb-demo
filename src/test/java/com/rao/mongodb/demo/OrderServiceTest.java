package com.rao.mongodb.demo;

import com.rao.mongodb.demo.dao.Item;
import com.rao.mongodb.demo.dao.Orders;
import com.rao.mongodb.demo.service.OrderService;
import com.rao.mongodb.demo.vo.OrderCountVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author raoshihong
 * @date 2021-01-17 23:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testSave(){

        Orders orders = new Orders();
        orders.setCustId("11111");
        orders.setOrdDate(new Date());
        orders.setPrice(100D);
        orders.setStatus("A");
        orders.setItems(new ArrayList<>());
        orderService.save(orders);
    }

    @Test
    public void testFind(){
        Orders orders = orderService.find("abc123");
        System.out.println(orders);
    }

    @Test
    public void testUpdate(){
        Orders orders = new Orders();
        orders.setId("600cc0d0a78aa6167a69fb12");
        orders.setStatus("1");
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setPrice("100");
        items.add(item);
        orders.setItems(items);
        orderService.update(orders);
    }

    @Test
    public void testOr(){
        List<Orders> byKey = orderService.findByKey();
        System.out.println(byKey);
    }

    @Test
    public void testMatch(){
        List<Orders> match = orderService.match();
        System.out.println(match);
    }

    @Test
    public void testGroup(){
        List<OrderCountVo> group = orderService.group();
        System.out.println(group);
    }
}
