#创建数据库
CREATE DATABASE `oexam` CHARACTER SET UTF8;

#切换数据库
USE oexam;

#创建用户表
CREATE TABLE `user`(
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID自增',
  `email` VARCHAR(50) DEFAULT NULL,
  `password` VARCHAR(50) NOT NULL COMMENT '用户密码，存放加密后的MD5',
  `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `department` varchar(50) not null  comment '部门（班级）',
  `role`  INT(4) NOT NULL COMMENT '用户角色,1:普通用户，2：管理员',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '最后一次更新时间',
  UNIQUE KEY `unique_email` (`email`) USING BTREE
)ENGINE =InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET = UTF8;

#创建题库表
CREATE TABLE `question`(
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '试题ID自增',
  `ask` TEXT NOT NULL COMMENT '试题名称',
  `analysis` TEXT NULL COMMENT '试题解析',
  `type` INT NOT NULL COMMENT '试题类型 1：单选，2：多选，3、判断',
  `status` INT NOT NULL comment '试题状态：1、启用，2、停用',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '最后一次更新时间'
)ENGINE = InnoDB DEFAULT charset = UTF8;

#创建选项表
CREATE TABLE `option`(
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '选项ID自增',
  `question_id` BIGINT NOT NULL COMMENT '试题id',
  `content` VARCHAR(255) NOT NULL COMMENT '选项内容',
  `answer` TINYINT NOT NULL DEFAULT 0 COMMENT '是否是答案，0：不是答案，1：是答案'
)ENGINE = InnoDB DEFAULT CHARSET =UTF8;

#创建试卷库
CREATE TABLE `paper`(
  `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '试卷ID自增',
  `title` varchar(255) not null comment '试卷标题',
  `time` INT not null comment '考试时间',
  `start_time` datetime not null comment '开始时间',
  `end_time` datetime not null comment '结束时间',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '最后一次更新时间'
)ENGINE =InnoDB DEFAULT charset = utf8;

#创建试卷详情表
create table `paper_detail`(
  `id` bigint not null primary key auto_increment,
  `paper_id` bigint not null comment '试卷ID',
  `question_id` bigint not null comment '试题id',
  `score` int not null comment '单题分数'
)engine = InnoDB default charset = utf8;

#创建用户答卷表
create table achievement(
  `id` bigint not null primary key auto_increment,
  `user_id` bigint not null comment '用户id',
  `paper_id` bigint not null comment '试卷id',
  `question_id` bigint not null comment '试题id',
  `option_id` bigint not null comment '试题选项'
)engine =InnoDB default charset = utf8;

#创建用户成绩表
create table mark(
  `id` bigint not null primary key auto_increment,
  `user_id` bigint not null comment '用户id',
  `paper_id` bigint not null comment '试卷id',
  `score` int not null comment '总分'
)engine =InnoDB default charset = utf8;

#创建参加考试人员表
create table exam_user(
  `id` BIGINT primary key auto_increment,
  `user_id` bigint not null comment '用户id',
  `paper_id` bigint not null comment '试卷id',
  `status` int not null default 0 comment '0、未参加,1、未交卷，3、待评阅,4、已评阅'
)engine = InnoDB default charset = utf8;