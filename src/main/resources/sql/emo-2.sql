/*
 Navicat Premium Data Transfer

 Source Server         : kgc
 Source Server Type    : MySQL
 Source Server Version : 50528
 Source Host           : localhost:3306
 Source Schema         : emo

 Target Server Type    : MySQL
 Target Server Version : 50528
 File Encoding         : 65001

 Date: 11/08/2023 13:04:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attribute
-- ----------------------------
DROP TABLE IF EXISTS `attribute`;
CREATE TABLE `attribute`  (
  `attribute_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`attribute_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of attribute
-- ----------------------------
INSERT INTO `attribute` VALUES ('1', 'type');
INSERT INTO `attribute` VALUES ('2', 'description');
INSERT INTO `attribute` VALUES ('3', 'parentId');
INSERT INTO `attribute` VALUES ('4', 'behavior');
INSERT INTO `attribute` VALUES ('5', 'contribution');

-- ----------------------------
-- Table structure for attribute_history
-- ----------------------------
DROP TABLE IF EXISTS `attribute_history`;
CREATE TABLE `attribute_history`  (
  `attribute_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`attribute_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for criterion
-- ----------------------------
DROP TABLE IF EXISTS `criterion`;
CREATE TABLE `criterion`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `criterion_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `review_points` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `review_criteria` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `review_process` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `display` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of criterion
-- ----------------------------
INSERT INTO `criterion` VALUES (1, '功能完整性', '确定软件是否包含项目需求文档的创建、编辑、版本控制、审批流程等基本功能', '软件功能能否满足项目需求管理的基本要求', '评估软件的功能列表或需求管理模块;针对项目需求文档的创建、编辑、审批流程等功能进行测试;确认软件是否能满足项目需求管理流程\n', 1);
INSERT INTO `criterion` VALUES (2, '用户友好性', '评估软件的用户界面是否直观、简洁，是否易于学习和使用', '软件是否具有良好的用户体验，能否减少用户的操作困难和错误', '让团队成员使用软件，并收集他们的反馈意见;检查软件是否提供帮助文档或在线支持;评估软件的用户界面设计是否符合用户习惯', 2);
INSERT INTO `criterion` VALUES (3, '可定制性', '检查软件是否允许自定义字段、工作流程和权限设置', '软件是否灵活，能够适应不同项目的需求管理要求', '研究软件的设置和配置选项，看是否能进行灵活的定制;确定是否支持自定义字段、工作流程和权限', 3);
INSERT INTO `criterion` VALUES (4, '数据安全性', '确认软件是否提供访问控制和身份认证功能，以限制数据访问权限', '软件是否提供足够的数据安全措施，以保护敏感信息和客户数据', '了解软件的数据传输和存储方式，确认是否采取了加密措施;检查软件是否提供访问控制和身份认证功能', 4);

-- ----------------------------
-- Table structure for entity
-- ----------------------------
DROP TABLE IF EXISTS `entity`;
CREATE TABLE `entity`  (
  `entity_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '枚举：mission,activity,capability,function,system,node  没有默认，必填',
  PRIMARY KEY (`entity_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of entity
-- ----------------------------
INSERT INTO `entity` VALUES ('activity_id', 'activity_name', 'activity');
INSERT INTO `entity` VALUES ('capability_id', 'capability_name', 'capability');
INSERT INTO `entity` VALUES ('function_id', 'function_name', 'function');
INSERT INTO `entity` VALUES ('mission_id', 'mission_name', 'mission');
INSERT INTO `entity` VALUES ('node_id', 'node_name', 'node');
INSERT INTO `entity` VALUES ('system_id', 'system_name', 'system');

-- ----------------------------
-- Table structure for entity_history
-- ----------------------------
DROP TABLE IF EXISTS `entity_history`;
CREATE TABLE `entity_history`  (
  `entity_historyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '枚举：mission,activity,capability,function,system,node  没有默认，必填',
  PRIMARY KEY (`entity_historyid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for host_info
-- ----------------------------
DROP TABLE IF EXISTS `host_info`;
CREATE TABLE `host_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `host_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主机Ip',
  `port` int(11) NOT NULL COMMENT '端口',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `new_database` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库名',
  `status` int(11) DEFAULT NULL COMMENT '数据库类型1：Mysql2：Oracle',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '个人定义的数据库名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of host_info
-- ----------------------------
INSERT INTO `host_info` VALUES (1, '127.0.0.1', 3306, 'root', 'root', 'demo', 1, 'localhost-3306');
INSERT INTO `host_info` VALUES (3, '127.0.0.1', 3307, 'root', 'root', 'demo', 1, 'localhost-3307');
INSERT INTO `host_info` VALUES (7, '192.168.31.120', 3306, 'root', 'root', 'demo', 1, '192.168.31.120');

-- ----------------------------
-- Table structure for opinion
-- ----------------------------
DROP TABLE IF EXISTS `opinion`;
CREATE TABLE `opinion`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '专家名',
  `expert_opinion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '意见',
  `status` int(11) DEFAULT NULL COMMENT '状态:0为通过，1为驳回',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '专家意见表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of opinion
-- ----------------------------
INSERT INTO `opinion` VALUES (1, '张三', '我觉得该项目是合适的', 0);

-- ----------------------------
-- Table structure for personnel
-- ----------------------------
DROP TABLE IF EXISTS `personnel`;
CREATE TABLE `personnel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '专家姓名',
  `introduction` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介',
  `display` int(11) NOT NULL COMMENT '显示顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评审人员' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for relationship
-- ----------------------------
DROP TABLE IF EXISTS `relationship`;
CREATE TABLE `relationship`  (
  `relationship_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_id1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_id2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `relationship_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '枚举：组成,依赖,影响,冲突,关联  默认是关联',
  PRIMARY KEY (`relationship_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of relationship
-- ----------------------------
INSERT INTO `relationship` VALUES ('1', 'node_id', 'activity_id', '关联');
INSERT INTO `relationship` VALUES ('2', 'mossion_id', 'node_id', '包含');
INSERT INTO `relationship` VALUES ('3', 'function_id', 'system_id', '关联');
INSERT INTO `relationship` VALUES ('4', 'capability_id', 'activity_id', '关联');
INSERT INTO `relationship` VALUES ('5', 'activity_id', 'node_id', '关联');
INSERT INTO `relationship` VALUES ('6', 'system_id', 'function_id', '关联');

-- ----------------------------
-- Table structure for relationship_history
-- ----------------------------
DROP TABLE IF EXISTS `relationship_history`;
CREATE TABLE `relationship_history`  (
  `relationship_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_history_id1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_history_id2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `relationship_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '枚举：组成,依赖,影响,冲突,关联  默认是关联',
  PRIMARY KEY (`relationship_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `sponsor` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起人',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型，本次评审的类型分别为增量，变更',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '0为未处理，1为通过，2为未通过',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (4, 'ss', 'sc', '变更', 0);
INSERT INTO `review` VALUES (5, 'ss', 'sc', '增量', 0);

-- ----------------------------
-- Table structure for review_entity
-- ----------------------------
DROP TABLE IF EXISTS `review_entity`;
CREATE TABLE `review_entity`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `review_id` int(11) DEFAULT NULL,
  `entity_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `review_id`(`review_id`) USING BTREE,
  INDEX `entity_id`(`entity_id`) USING BTREE,
  CONSTRAINT `review_entity_ibfk_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_entity_ibfk_2` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`entity_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of review_entity
-- ----------------------------
INSERT INTO `review_entity` VALUES (1, 4, 'activity_id');

-- ----------------------------
-- Table structure for value
-- ----------------------------
DROP TABLE IF EXISTS `value`;
CREATE TABLE `value`  (
  `value_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`value_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of value
-- ----------------------------
INSERT INTO `value` VALUES ('1', 'activity_id', '2', 's1');
INSERT INTO `value` VALUES ('2', 'capability_id', '1', 's2');
INSERT INTO `value` VALUES ('3', 'node_id', '3', 's3');

-- ----------------------------
-- Table structure for value_history
-- ----------------------------
DROP TABLE IF EXISTS `value_history`;
CREATE TABLE `value_history`  (
  `value_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entity_historyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`value_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
