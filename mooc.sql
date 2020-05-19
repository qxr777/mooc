/*
 Navicat Premium Data Transfer

 Source Server         : local_docker
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : dbserver:3306
 Source Schema         : mooc

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 18/05/2020 22:39:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mclass_answer
-- ----------------------------
DROP TABLE IF EXISTS `mclass_answer`;
CREATE TABLE `mclass_answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `answer` varchar(255) DEFAULT NULL,
  `examination_record_id` bigint(20) DEFAULT NULL,
  `is_right` tinyint(3) unsigned DEFAULT '0',
  `status` int(11) DEFAULT NULL,
  `subject_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKak05md5crmt5h1vfrobmiwgt1` (`examination_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_answer
-- ----------------------------
BEGIN;
INSERT INTO `mclass_answer` VALUES (1, '2020-05-18 22:37:38', '2020-05-18 22:37:38', '填空题答案_UNIT_TEST', 1, 1, 1, 4, 2);
INSERT INTO `mclass_answer` VALUES (2, '2020-05-18 22:37:38', '2020-05-18 22:37:38', 'B', 1, 0, 1, 5, 2);
INSERT INTO `mclass_answer` VALUES (3, '2020-05-18 22:37:38', '2020-05-18 22:37:38', 'false', 1, 1, 1, 6, 2);
INSERT INTO `mclass_answer` VALUES (4, '2020-05-18 22:37:39', '2020-05-18 22:37:39', '填空题答案_UNIT_TEST', 2, 1, 1, 4, 3);
INSERT INTO `mclass_answer` VALUES (5, '2020-05-18 22:37:39', '2020-05-18 22:37:39', 'A,B', 2, 1, 1, 5, 3);
INSERT INTO `mclass_answer` VALUES (6, '2020-05-18 22:37:39', '2020-05-18 22:37:39', 'false', 2, 1, 1, 6, 3);
COMMIT;

-- ----------------------------
-- Table structure for mclass_attendance
-- ----------------------------
DROP TABLE IF EXISTS `mclass_attendance`;
CREATE TABLE `mclass_attendance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `check_in_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4j2g19plyherqpn3w1rnfnv7o` (`user_id`),
  KEY `FKrxtm0n53nqcoskln4ny96r04j` (`check_in_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_attendance
-- ----------------------------
BEGIN;
INSERT INTO `mclass_attendance` VALUES (1, '2020-05-18 22:35:57', '2020-05-18 22:35:57', 1, 1, 2);
INSERT INTO `mclass_attendance` VALUES (2, '2020-05-18 22:35:57', '2020-05-18 22:35:57', 1, 1, 3);
COMMIT;

-- ----------------------------
-- Table structure for mclass_check_in
-- ----------------------------
DROP TABLE IF EXISTS `mclass_check_in`;
CREATE TABLE `mclass_check_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `absence_count` int(11) DEFAULT NULL,
  `checked_count` int(11) DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `is_gps` bit(1) DEFAULT NULL,
  `late_count` int(11) DEFAULT NULL,
  `latitude` decimal(5,2) DEFAULT NULL,
  `lesson_id` bigint(20) DEFAULT NULL,
  `longitude` decimal(5,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_check_in
-- ----------------------------
BEGIN;
INSERT INTO `mclass_check_in` VALUES (1, '2020-05-18 22:35:28', '2020-05-18 22:36:21', 0, 2, '2020-05-18 23:05:11', b'1', 0, 100.00, 1, 100.00, 2);
COMMIT;

-- ----------------------------
-- Table structure for mclass_choice
-- ----------------------------
DROP TABLE IF EXISTS `mclass_choice`;
CREATE TABLE `mclass_choice` (
  `choice_id` bigint(20) NOT NULL,
  PRIMARY KEY (`choice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_choice
-- ----------------------------
BEGIN;
INSERT INTO `mclass_choice` VALUES (2);
INSERT INTO `mclass_choice` VALUES (5);
COMMIT;

-- ----------------------------
-- Table structure for mclass_class_user
-- ----------------------------
DROP TABLE IF EXISTS `mclass_class_user`;
CREATE TABLE `mclass_class_user` (
  `mooc_class_id` bigint(20) NOT NULL,
  `users_id` bigint(20) NOT NULL,
  KEY `FKfaxv8drigcod8wq6xyt836m27` (`users_id`),
  KEY `FKccekm2qroa7vxdc5yitcadx2a` (`mooc_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_class_user
-- ----------------------------
BEGIN;
INSERT INTO `mclass_class_user` VALUES (1, 2);
INSERT INTO `mclass_class_user` VALUES (1, 3);
COMMIT;

-- ----------------------------
-- Table structure for mclass_course
-- ----------------------------
DROP TABLE IF EXISTS `mclass_course`;
CREATE TABLE `mclass_course` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `teacher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6a8tuuij8qb7sh3365efhyj0v` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_course
-- ----------------------------
BEGIN;
INSERT INTO `mclass_course` VALUES (1, '2020-05-18 22:29:59', '2020-05-18 22:29:59', 'Java企业版课程', 2, 1);
COMMIT;

-- ----------------------------
-- Table structure for mclass_examination
-- ----------------------------
DROP TABLE IF EXISTS `mclass_examination`;
CREATE TABLE `mclass_examination` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `lesson_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `submit_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh6ci26ntg34iapoo2p7a5j6r6` (`lesson_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_examination
-- ----------------------------
BEGIN;
INSERT INTO `mclass_examination` VALUES (1, '2020-05-18 22:34:37', '2020-05-18 22:37:39', 1, '课程引论练习', 2, 2);
COMMIT;

-- ----------------------------
-- Table structure for mclass_examination_record
-- ----------------------------
DROP TABLE IF EXISTS `mclass_examination_record`;
CREATE TABLE `mclass_examination_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `correct_count` int(11) DEFAULT NULL,
  `examination_id` bigint(20) DEFAULT NULL,
  `score` decimal(5,2) DEFAULT NULL,
  `submit_time` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9smo6p5a3odet2kvub0tqay6j` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_examination_record
-- ----------------------------
BEGIN;
INSERT INTO `mclass_examination_record` VALUES (1, '2020-05-18 22:37:38', '2020-05-18 22:37:38', 2, 1, 20.00, '2020-05-18 22:37:38', 2);
INSERT INTO `mclass_examination_record` VALUES (2, '2020-05-18 22:37:39', '2020-05-18 22:37:39', 3, 1, 30.00, '2020-05-18 22:37:39', 3);
COMMIT;

-- ----------------------------
-- Table structure for mclass_exercise
-- ----------------------------
DROP TABLE IF EXISTS `mclass_exercise`;
CREATE TABLE `mclass_exercise` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_exercise
-- ----------------------------
BEGIN;
INSERT INTO `mclass_exercise` VALUES (1, '2020-05-18 22:31:18', '2020-05-18 22:31:18', 1, '课程引论练习');
COMMIT;

-- ----------------------------
-- Table structure for mclass_fill
-- ----------------------------
DROP TABLE IF EXISTS `mclass_fill`;
CREATE TABLE `mclass_fill` (
  `decimal_key` varchar(255) DEFAULT NULL,
  `key_type` int(11) DEFAULT NULL,
  `match_type` int(11) DEFAULT NULL,
  `text_key` varchar(255) DEFAULT NULL,
  `is_unique` tinyint(3) unsigned DEFAULT '0',
  `fill_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_fill
-- ----------------------------
BEGIN;
INSERT INTO `mclass_fill` VALUES (NULL, 2, 1, '填空题答案_UNIT_TEST', 1, 1);
INSERT INTO `mclass_fill` VALUES (NULL, 2, 1, '填空题答案_UNIT_TEST', 1, 4);
COMMIT;

-- ----------------------------
-- Table structure for mclass_judgement
-- ----------------------------
DROP TABLE IF EXISTS `mclass_judgement`;
CREATE TABLE `mclass_judgement` (
  `false_count` int(11) DEFAULT NULL,
  `result` tinyint(3) unsigned DEFAULT '0',
  `true_count` int(11) DEFAULT NULL,
  `judgement_id` bigint(20) NOT NULL,
  PRIMARY KEY (`judgement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_judgement
-- ----------------------------
BEGIN;
INSERT INTO `mclass_judgement` VALUES (0, 0, 0, 3);
INSERT INTO `mclass_judgement` VALUES (2, 0, 0, 6);
COMMIT;

-- ----------------------------
-- Table structure for mclass_lesson
-- ----------------------------
DROP TABLE IF EXISTS `mclass_lesson`;
CREATE TABLE `mclass_lesson` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `mooc_class_id` bigint(20) DEFAULT NULL,
  `service_date` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `check_in_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfay0v3u145ymqgfkfrlxki51` (`check_in_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_lesson
-- ----------------------------
BEGIN;
INSERT INTO `mclass_lesson` VALUES (1, '2020-05-18 22:34:10', '2020-05-18 22:38:25', '2020-05-18 22:38:25', 1, '2020-05-18 22:33:53', '2020-05-18 22:35:02', 3, NULL);
COMMIT;

-- ----------------------------
-- Table structure for mclass_mooc_class
-- ----------------------------
DROP TABLE IF EXISTS `mclass_mooc_class`;
CREATE TABLE `mclass_mooc_class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `offline_course` varchar(255) DEFAULT NULL,
  `semester` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhbruw69f379dlto1dmv62snsr` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_mooc_class
-- ----------------------------
BEGIN;
INSERT INTO `mclass_mooc_class` VALUES (1, '2020-05-18 22:29:59', '2020-05-18 22:29:59', 'Java企业版研究生课堂', 'Java企业版课程', '春季', '2020', 1);
COMMIT;

-- ----------------------------
-- Table structure for mclass_option
-- ----------------------------
DROP TABLE IF EXISTS `mclass_option`;
CREATE TABLE `mclass_option` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `is_correct` tinyint(3) unsigned DEFAULT '0',
  `count` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `choice_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdva24lm5bx7qpbwi3q86ddqm` (`choice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_option
-- ----------------------------
BEGIN;
INSERT INTO `mclass_option` VALUES (1, '2020-05-18 22:33:22', '2020-05-18 22:33:22', '选项A_UNIT_TEST', 1, 0, 'A', 2);
INSERT INTO `mclass_option` VALUES (2, '2020-05-18 22:33:22', '2020-05-18 22:33:22', '选项B_UNIT_TEST', 1, 0, 'B', 2);
INSERT INTO `mclass_option` VALUES (3, '2020-05-18 22:34:37', '2020-05-18 22:37:39', '选项A_UNIT_TEST', 1, 1, 'A', 5);
INSERT INTO `mclass_option` VALUES (4, '2020-05-18 22:34:37', '2020-05-18 22:37:39', '选项B_UNIT_TEST', 1, 2, 'B', 5);
COMMIT;

-- ----------------------------
-- Table structure for mclass_subject
-- ----------------------------
DROP TABLE IF EXISTS `mclass_subject`;
CREATE TABLE `mclass_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `error_count` int(11) DEFAULT NULL,
  `error_percent` int(11) DEFAULT NULL,
  `examination_id` bigint(20) DEFAULT NULL,
  `exercise_id` bigint(20) DEFAULT NULL,
  `right_count` int(11) DEFAULT NULL,
  `right_percent` int(11) DEFAULT NULL,
  `score` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3tpqqo88sfcesfjj5ddljfia2` (`exercise_id`),
  KEY `FKihb1sa66psshqpf8j249n04p8` (`examination_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mclass_subject
-- ----------------------------
BEGIN;
INSERT INTO `mclass_subject` VALUES (1, '2020-05-18 22:33:22', '2020-05-18 22:33:22', '填空题题干_UNIT_TEST', 0, NULL, NULL, 1, 0, NULL, 10.00);
INSERT INTO `mclass_subject` VALUES (2, '2020-05-18 22:33:22', '2020-05-18 22:33:22', '选择题题干_UNIT_TEST', 0, NULL, NULL, 1, 0, NULL, 10.00);
INSERT INTO `mclass_subject` VALUES (3, '2020-05-18 22:33:22', '2020-05-18 22:33:22', '判断题题干_UNIT_TEST', 0, NULL, NULL, 1, 0, NULL, 10.00);
INSERT INTO `mclass_subject` VALUES (4, '2020-05-18 22:34:37', '2020-05-18 22:37:39', '填空题题干_UNIT_TEST', 0, 0, 1, NULL, 2, 100, 10.00);
INSERT INTO `mclass_subject` VALUES (5, '2020-05-18 22:34:37', '2020-05-18 22:37:39', '选择题题干_UNIT_TEST', 1, 50, 1, NULL, 1, 50, 10.00);
INSERT INTO `mclass_subject` VALUES (6, '2020-05-18 22:34:37', '2020-05-18 22:37:39', '判断题题干_UNIT_TEST', 0, 0, 1, NULL, 2, 100, 10.00);
COMMIT;

-- ----------------------------
-- Table structure for upms_user
-- ----------------------------
DROP TABLE IF EXISTS `upms_user`;
CREATE TABLE `upms_user` (
  `discriminator` varchar(30) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `student_no` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rms9jmyq5ysgxhkgy98ujd50i` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of upms_user
-- ----------------------------
BEGIN;
INSERT INTO `upms_user` VALUES ('Teacher', 1, '2020-05-18 22:29:13', '2020-05-18 22:29:13', NULL, 'whut', '李工大', NULL, NULL, NULL);
INSERT INTO `upms_user` VALUES ('Student', 2, '2020-05-18 22:29:13', '2020-05-18 22:29:13', NULL, 'xes1', '薛而思', NULL, NULL, NULL);
INSERT INTO `upms_user` VALUES ('Student', 3, '2020-05-18 22:29:13', '2020-05-18 22:29:13', NULL, 'xdf1', '信东方', NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
