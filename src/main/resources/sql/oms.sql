# # drop database if exists oms;
# #
# # create database oms;
#
# use emo;
#
create table Entity_history(
    history_id int primary key auto_increment,
    entity_id varchar(200),
    version varchar(200),
    context varchar(200),
    text varchar(200)
)engine = Innodb default charset = utf8mb4 comment '配送表';


create table review(
                               id int primary key auto_increment,
                               name varchar(200)comment '名称',
                               sponsor varchar(200)comment '发起人',
                               status int not null default 0 comment '0为未处理，1为通过'
)engine = Innodb default charset = utf8mb4 comment '流程表';


create table personnel(
                       id int primary key auto_increment,
                       name varchar(200)comment '专家姓名',
                       introduction varchar(200)comment '简介',
                       display int  comment '显示顺序'
)engine = Innodb default charset = utf8mb4 comment '评审人员';




create table criterion(
                          id int primary key auto_increment,
                          name varchar(200)comment '专家姓名',
                          points varchar(200)comment '简介',
                          standard int  comment '显示顺序'
)engine = Innodb default charset = utf8mb4 comment '评审人员';




# insert into t_dispatcher(name) values ('name1');
# insert into t_dispatcher(name) values ('name2');
# insert into t_dispatcher(name) values ('name3');
# insert into t_dispatcher(name) values ('name4');
# insert into t_dispatcher(name) values ('name5');
# insert into t_dispatcher(name) values ('name6');
# insert into t_dispatcher(name) values ('name7');
# insert into t_dispatcher(name) values ('name8');
# insert into t_dispatcher(name) values ('name9');
# insert into t_dispatcher(name) values ('name10');
#
#
# create table t_order(
#     id int primary key auto_increment comment '主键',
#     status int comment '订单状态: 0 未配送  1 已配送',
#     dispatcher_id int,
#     dispatch_time datetime,
#     name varchar(200),
#     total_price double
# )engine = Innodb default charset = utf8mb4 comment '订单表';
#
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (1,3,now(),'Jawd','34');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'qwer','43');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (1,1,now(),'wert','23');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'rtyu','65');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (1,2,now(),'tyui','45');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'yuio','76');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'uiop','98');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'asdf','12');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'sdfg','21');
# insert into t_order(status,dispatcher_id,dispatch_time,name,total_price)values (0,null,null,'dfgh','87');
#
#
#

CREATE TABLE baseline (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          baseline_name VARCHAR(255) NOT NULL ,
                          baseline_description TEXT ,
                          priority INT ,
                          created_time DATETIME DEFAULT NULL,
                          modified_time DATETIME DEFAULT NULL
)engine = Innodb default charset = utf8mb4 comment '基线';








SELECT DISTINCT relationship_id,
                entity_history_id1, entity1.entity_name AS entity_name1,
                entity_history_id2, entity2.entity_name AS entity_name2,
                relationship_type, entity1.entity_type AS entity_type
FROM relationship_history
         INNER JOIN entity_history entity1 ON relationship_history.entity_history_id1 = entity1.entity_id
         INNER JOIN entity_history entity2 ON relationship_history.entity_history_id2 = entity2.entity_id
WHERE entity1.entity_historyid = #{entity_historyid}
    AND (#{keyWord} IS NULL OR entity1.entity_type LIKE CONCAT('%', #{keyWord}, '%'))











CREATE TABLE `file_model`  (
                               `id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识符',
                               `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                               `md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5码',
                               `size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
                               `local_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径',
                               `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原文件名称',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;