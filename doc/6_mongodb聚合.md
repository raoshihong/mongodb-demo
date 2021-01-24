# 1.聚合相关操作术语

MongoDB 中聚合(aggregate)主要用于处理数据(诸如统计平均值，求和等)，并返回计算后的数据结果。

有点类似**SQL**语句中的**count(*)**。

其实mongodb中的聚合跟elasticsearch中的聚合很像，都采用了管道的概念

### 语法

aggregate() 方法的基本语法格式如下所示：

```plain
db.集合名称.aggregate(pipeline,options)
```
pipeline表示管道
### 管道操作符

|常用管道|含义|
|:----|:----|
|$group|将collection中的document分组，可用于统计结果|
|$match|过滤数据，只输出符合结果的文档|
|$project|修改输入文档的结构(例如重命名，增加、删除字段，创建结算结果等)|
|$sort|将结果进行排序后输出|
|$limit|限制管道输出的结果个数|
|$skip|跳过制定数量的结果，并且返回剩下的结果|
|$unwind|将数组类型的字段进行拆分|

而管道操作可以结合表达式操作进行数据操作

### 表达式操作符

|常用表达式|含义|
|:----|:----|
|$sum|计算总和，{$sum: 1}表示返回总和×1的值(即总和的数量),使用{$sum: '$制定字段'}也能直接获取制定字段的值的总和|
|$avg|平均值|
|$min|min|
|$max|max|
|$push|将结果文档中插入值到一个数组中|
|$first|根据文档的排序获取第一个文档数据|
|$last|同理，获取最后一个数据|

下面可以把相关的管道操作于sql中的联系起来

|    SQL 操作/函数|mongodb聚合操作|
|:----|:----|:----|:----|
|where|$match|
|group by|$group|
|having|$match|
|select|$project|
|order by|$sort|
|limit|$limit|
|sum()|$sum|
|count()|$sum|
|join|$lookup<br>（v3.2 新增）|

# 2.$group 管道

创建名叫orders的collection

```plain
db.orders.insertMany([
{
  cust_id: "abc123",
  ord_date: ISODate("2012-11-02T17:04:11.102Z"),
  status: 'A',
  price: 50,
  items: [ { sku: "xxx", qty: 25, price: 1 },
           { sku: "yyy", qty: 25, price: 1 } ]
}
])
```

![图片](https://uploader.shimo.im/f/ocht8wt0kdsfuK0E.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)


```plain
db.orders.aggregate()
```
相当于调用的是find()操作
![图片](https://uploader.shimo.im/f/7uWd6BXsEBzHBwZh.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)

相当于select * from orders


---


## 2.1 $group统计所有

示例：

**对orders表计算所有price求和**

```plain
db.orders.aggregate( [
   {
     $group: {
        _id: null,
        count: { $sum: 1 }
     }
   }
] )
```
相当于select count(*) as count from orders
当_id设置为null时，表示不根据某个字段统计，而是统计整个集合

![图片](https://uploader.shimo.im/f/S7wSzIKNRBVIWyM5.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)


## 2.2 $group 统计指定字段的值

**对orders表计算所有price求和**

```plain
db.orders.aggregate( [
   {
     $group: {
        _id: null,
        total: { $sum: "$price" }
     }
   }
] )
```
相当于select sum(price) as total from orders
![图片](https://uploader.shimo.im/f/pY94iY4Dz3zZ11NL.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)

## 2.3 $group 按指定单个字段进行分组统计

**对每一个唯一的cust_id, 计算price总和**

```plain
db.orders.aggregate( [
   {
     $group: {
        _id: "$cust_id",
        total: { $sum: "$price" }
     }
   }
] )
```
_id 为空时，表示统计整个集合，不为空时，表示按指定字段进行分组统计
total 表示统计结果别名

相当于sql: select cust_id,sum(price) as total from orders group by cust_id

![图片](https://uploader.shimo.im/f/LnuD6JdDzPpo4yER.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)

## 2.3 $group 对多个字段进行分组统计

**对每一个唯一对cust_id和ord_date分组，计算price总和，不包括日期的时间部分**

```plain
db.orders.aggregate( [
   {
     $group: {
        _id: {
           cust_id: "$cust_id",
           ord_date: {
               month: { $month: "$ord_date" },
               day: { $dayOfMonth: "$ord_date" },
               year: { $year: "$ord_date"}
           }
        },
        total: { $sum: "$price" }
     }
   }
] )
```
相当于sql: select cust_id,order_date,sum(price) as total from orders group by cust_id,ord_date

![图片](https://uploader.shimo.im/f/8utBr5Z9aGdGMqSx.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)

# 3.$match管道

$match单独使用时相当于sql中的where，当$match紧跟$group之后时，相当于sql中的having

测试数据，向orders 集合中再插入一条document

```plain
db.orders.insertMany([
{
  cust_id: "abc124",
  ord_date: ISODate("2012-12-02T17:04:11.102Z"),
  status: 'B',
  price: 100,
  items: [ { sku: "ddd", qty: 25, price: 1 },
           { sku: "yyy", qty: 25, price: 1 } ]
},
{
  cust_id: "abc125",
  ord_date: ISODate("2012-12-02T17:04:11.102Z"),
  status: 'B',
  price: 101,
  items: [ { sku: "ddd", qty: 25, price: 1 },
           { sku: "yyy", qty: 25, price: 1 } ]
}
])
```
![图片](https://uploader.shimo.im/f/ItCwl7gkhVTyutx5.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)


## 3.1 $match相当于where

```plain
db.orders.aggregate([
{
	$match:{
		status:'A'
	}
}
])
```
相当于sql: select * from orders where status = 'A'

![图片](https://uploader.shimo.im/f/4cuJYtOgDKSmwWXy.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)

## 3.2 $match 作为having

```plain
db.orders.aggregate([
	{
		$match:{
			status:"B"
		}
	},
	{
		$group:{
			_id:"$cust_id",
			total:{
				$sum:"$price"
			}
		}
	},
	{
		$match:{
			total:{
				$gt:100
			}
		}
	}
])
```
相当于 select cust_id,total from orders where status = "B" group by cust_id having total>100
![图片](https://uploader.shimo.im/f/ZDRhezxSqAdNSf1k.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)


# 4.$project 投影

修改输入文档的结构(例如重命名，增加、删除字段，创建结算结果等)

```plain
db.orders.aggregate([
	{
		$project:{
			_id:0, // 0表示不显示
			cust_id:1, //1表示显示
			order_date:"$ord_date" // 这块字段重命名	
		}
	}
])
```
相当于sql : select cust_id,ord_data as order_date from orders
![图片](https://uploader.shimo.im/f/hdYxyZDzxRRhYDOZ.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)

# 5.$sort 排序管道

```plain
db.orders.aggregate([
	{
		$sort:{
			price:-1
		}
	}
])
```
相当于sql : select * from orders order by price desc
# 6.$skip和$limit

```plain
db.orders.aggregate([
	{
		$limit:2
	},
	{
		$skip:0
	}
])
```
# 7.$unwind

`$unwind`管道可以document中的数组类型的字段进行拆分，每条包含数组中的一个值。

```plain
db.orders.aggregate([
	{
		$unwind:{
			path:'$items',
			preserveNullAndEmptyArrays: true //该属性为true即保留
		}
	}
])
```
我们对orders文档中对items字段进行拆分
![图片](https://uploader.shimo.im/f/73jRZjXEjD29tSZX.png!thumbnail?fileGuid=HcRDTDyg6WxttpDv)


聚合相关的学习可以参考：[https://www.jianshu.com/p/72fc4409936c](https://www.jianshu.com/p/72fc4409936c)

[https://www.cnblogs.com/zhoujie/p/mongo1.html](https://www.cnblogs.com/zhoujie/p/mongo1.html)







