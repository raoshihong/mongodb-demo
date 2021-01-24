package com.rao.mongodb.demo.service.impl;

import com.rao.mongodb.demo.dao.Orders;
import com.rao.mongodb.demo.service.OrderService;
import com.rao.mongodb.demo.vo.OrderCountVo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**Ø
 * @author raoshihong
 * @date 2021-01-17 23:47
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Orders orders) {
        mongoTemplate.save(orders);
    }

    @Override
    public Orders find(String key) {
        Criteria criteria = Criteria.where("cust_id").is(key);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query,Orders.class);
    }

    @Override
    public void update(Orders orders){
        Query condition = new Query(Criteria.where("_id").is(orders.getId()));

        Update update = new Update();
        update.set("status",orders.getStatus())
            .set("name","ffff")
            .set("items", orders.getItems());

        mongoTemplate.updateMulti(condition,update,Orders.class);
    }

    @Override
    public List<Orders> findByKey() {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("cust_id").is("abc123"),Criteria.where("name").regex("^ffff"));
        Query query = new Query(criteria);
        return mongoTemplate.find(query,Orders.class);
    }

    @Override
    public List<Orders> match(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("cust_id").is("abc123")));

        // 注意需要使用spring-data-mongodb 1.10.10 以上的版本才支持,否则会报cursor错误
        AggregationResults<Orders> aggregate = mongoTemplate.aggregate(aggregation, "orders", Orders.class);
        return aggregate.getMappedResults();
    }

    @Override
    public List<OrderCountVo> group(){

        MatchOperation matchOperation = Aggregation.match(Criteria.where("cust_id").is("abc123"));

        Field field = Fields.field("orderDate","ord_date");
        Fields fields = Fields.from(field,Fields.field("custId","cust_id"));
        GroupOperation groupOperation = Aggregation.group(fields).sum("price").as("count");


        AggregationExpression dateExpression = DateOperators.DateToString
            .dateOf("orderDate")
            .toString("%Y-%m-%d")
            .withTimezone(
                DateOperators.Timezone.valueOf("+08")
            );

        // 字段映射
        ProjectionOperation projectionOperation = Aggregation.project("custId", "count")
            .andExclude("_id").and(dateExpression).as("date");

        Aggregation aggregation = Aggregation.newAggregation(
            matchOperation,
            groupOperation,
            projectionOperation);

        AggregationResults<OrderCountVo> aggregate =
            mongoTemplate.aggregate(aggregation, "orders", OrderCountVo.class);

        return aggregate.getMappedResults();
    }

}
