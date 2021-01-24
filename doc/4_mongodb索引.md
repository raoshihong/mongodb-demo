mongodb中的索引跟mysql中的索引基本一样，包含单字段索引和复合索引，在查询时也有覆盖索引（即查询的字段就是索引字段，那么可以不用查去查询数据表了，直接返回索引的值）

# 1.索引的查看

```plain
db.comment.getIndexes()
```
![图片](https://uploader.shimo.im/f/faOZTM6bdXy3H9dN.png!thumbnail?fileGuid=QYHwhHwDDtqHcgY9)

可以看到上面索引为_id字段，索引的名称为_id_

# 2.索引的创建

语法格式：

```plain
db.集合名称.createIndex(keys, options)
```
参数说明：
* keys 包含字段和值对的文档，其中字段是索引键，值秒杀该字段的索引类型，对于字段上的升序索引，值为1，对于降序索引，值为-1，比如：{字段:1或-1}
* options 可选，包含一组控制索引创建的选项

![图片](https://uploader.shimo.im/f/1d3x3QbrQDRrzk9S.png!thumbnail?fileGuid=QYHwhHwDDtqHcgY9)


## 2.1 单字段创建索引

测试数据还是用之前的comment评论文章的数据

示例：

对userid字段建立索引：

```plain
db.comment.createIndex({userid:1})
```
![图片](https://uploader.shimo.im/f/xd0TndJUAUqzhvcg.png!thumbnail?fileGuid=QYHwhHwDDtqHcgY9)

查看上面创建的索引

```plain
db.comment.getIndexes()
```
![图片](https://uploader.shimo.im/f/Qdw6l040Hyicc7wF.png!thumbnail?fileGuid=QYHwhHwDDtqHcgY9)


## 2.2创建复合索引

对userid和nickname同时建立复合索引

```plain
db.comment.createIndex({userid:1,nickname:-1})
```
上面表示创建了一个复合索引，这个索引根据userid进行升序索引，根据nickname降序索引
![图片](https://uploader.shimo.im/f/EkMcPeVYjV8slKfR.png!thumbnail?fileGuid=QYHwhHwDDtqHcgY9)


# 3.索引的删除

## 3.1 删除指定的索引

语法格式:

```plain
db.集合名称.dropIndex(index)
```
示例：

```plain
db.comment.dropIndex({userid:1})
#或者通过索引名称删除
db.comment.dropIndex("userid_1")
```
![图片](https://uploader.shimo.im/f/ARqmbvw1meAs18rc.png!thumbnail?fileGuid=QYHwhHwDDtqHcgY9)

## 3.2 删除所有的索引

```plain
db.comment.dropIndexes()
```






