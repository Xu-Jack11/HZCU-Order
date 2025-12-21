-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hzcuorder
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_permission`
--

DROP TABLE IF EXISTS `admin_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_permission` (
  `code` varchar(50) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `module` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_permission`
--

/*!40000 ALTER TABLE `admin_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_permission` ENABLE KEYS */;

--
-- Table structure for table `admin_role`
--

DROP TABLE IF EXISTS `admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_role` (
  `role_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role`
--

/*!40000 ALTER TABLE `admin_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_role` ENABLE KEYS */;

--
-- Table structure for table `admin_role_permission`
--

DROP TABLE IF EXISTS `admin_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_role_permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint unsigned NOT NULL,
  `permission_code` varchar(50) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`),
  KEY `permission_code` (`permission_code`),
  CONSTRAINT `admin_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`role_id`),
  CONSTRAINT `admin_role_permission_ibfk_2` FOREIGN KEY (`permission_code`) REFERENCES `admin_permission` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role_permission`
--

/*!40000 ALTER TABLE `admin_role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_role_permission` ENABLE KEYS */;

--
-- Table structure for table `admin_user`
--

DROP TABLE IF EXISTS `admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_user` (
  `admin_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `real_name` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
INSERT INTO `admin_user` VALUES (1,'admin','$2a$10$3PayhyxiJPRIxlabH2gpce6bqbokE9D42qSrD4sx0i9X7p1qZNyGW','系统管理员',NULL,NULL,1,'2025-12-21 14:39:07','2025-12-19 00:26:46','2025-12-21 14:39:07');
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;

--
-- Table structure for table `admin_user_role`
--

DROP TABLE IF EXISTS `admin_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_user_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint unsigned NOT NULL,
  `admin_id` bigint unsigned NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `admin_id` (`admin_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `admin_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`role_id`),
  CONSTRAINT `admin_user_role_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admin_user` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_role`
--

/*!40000 ALTER TABLE `admin_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_user_role` ENABLE KEYS */;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcement` (
  `announcement_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `scope` varchar(20) DEFAULT NULL COMMENT 'global,canteen,user',
  `target_id` bigint unsigned DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  `effective_from` datetime DEFAULT NULL,
  `effective_to` datetime DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '1:active, 0:inactive',
  `created_by` bigint unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcement`
--

/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `operator_type` varchar(20) DEFAULT NULL COMMENT 'admin,merchant,user',
  `operator_id` bigint unsigned DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `request_path` varchar(500) DEFAULT NULL,
  `changes` json DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;

--
-- Table structure for table `banner`
--

DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `banner_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `jump_link` varchar(500) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `status` tinyint DEFAULT '1' COMMENT '1:active, 0:inactive',
  `created_by` bigint unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`banner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banner`
--

/*!40000 ALTER TABLE `banner` DISABLE KEYS */;
/*!40000 ALTER TABLE `banner` ENABLE KEYS */;

--
-- Table structure for table `bundle`
--

DROP TABLE IF EXISTS `bundle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bundle` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '1:active, 0:inactive',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `bundle_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bundle`
--

/*!40000 ALTER TABLE `bundle` DISABLE KEYS */;
/*!40000 ALTER TABLE `bundle` ENABLE KEYS */;

--
-- Table structure for table `bundle_item`
--

DROP TABLE IF EXISTS `bundle_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bundle_item` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `bundle_id` bigint unsigned NOT NULL,
  `dish_id` bigint unsigned NOT NULL,
  `quantity` int DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `bundle_id` (`bundle_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `bundle_item_ibfk_1` FOREIGN KEY (`bundle_id`) REFERENCES `bundle` (`id`),
  CONSTRAINT `bundle_item_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bundle_item`
--

/*!40000 ALTER TABLE `bundle_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `bundle_item` ENABLE KEYS */;

--
-- Table structure for table `canteen`
--

DROP TABLE IF EXISTS `canteen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `canteen` (
  `canteen_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `campus` varchar(100) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `contact_phone` varchar(20) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `business_hours` varchar(100) DEFAULT NULL,
  `service_fee_rate` decimal(5,4) DEFAULT '0.0000',
  `remark` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint DEFAULT '0',
  `sort_order` int DEFAULT '0',
  `image_url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`canteen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canteen`
--

/*!40000 ALTER TABLE `canteen` DISABLE KEYS */;
INSERT INTO `canteen` VALUES (1,'瓦香鸡米饭','主校区',NULL,NULL,1,NULL,NULL,NULL,'2025-12-18 17:05:53','2025-12-20 00:18:02',0,NULL,NULL),(2,'测试商家','南校区',NULL,NULL,1,NULL,NULL,NULL,'2025-12-19 12:06:15','2025-12-20 00:18:02',0,NULL,NULL),(4,'APITestMerchantFlat','???','API Loc Flat','12345678',1,'09:00-20:00',0.1000,'Test','2025-12-19 12:31:43','2025-12-20 00:18:02',0,0,'http://test.com/img.jpg');
/*!40000 ALTER TABLE `canteen` ENABLE KEYS */;

--
-- Table structure for table `canteen_announcement`
--

DROP TABLE IF EXISTS `canteen_announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `canteen_announcement` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  `type` varchar(20) DEFAULT NULL COMMENT 'notice,promotion,maintenance',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `canteen_announcement_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canteen_announcement`
--

/*!40000 ALTER TABLE `canteen_announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `canteen_announcement` ENABLE KEYS */;

--
-- Table structure for table `canteen_status`
--

DROP TABLE IF EXISTS `canteen_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `canteen_status` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned NOT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT 'open,closed,busy,maintenance',
  `estimated_wait_time` int DEFAULT '0' COMMENT 'minutes',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `canteen_status_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canteen_status`
--

/*!40000 ALTER TABLE `canteen_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `canteen_status` ENABLE KEYS */;

--
-- Table structure for table `cart_snapshot`
--

DROP TABLE IF EXISTS `cart_snapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_snapshot` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `items` json DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `cart_snapshot_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_snapshot`
--

/*!40000 ALTER TABLE `cart_snapshot` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_snapshot` ENABLE KEYS */;

--
-- Table structure for table `coupon_template`
--

DROP TABLE IF EXISTS `coupon_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon_template` (
  `coupon_template_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `scope_type` varchar(20) DEFAULT NULL COMMENT 'global,canteen,dish',
  `scope_id` bigint unsigned DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT 'discount,cash,rate',
  `threshold_amount` decimal(10,2) DEFAULT '0.00',
  `discount_amount` decimal(10,2) DEFAULT '0.00',
  `discount_rate` decimal(5,4) DEFAULT '0.0000',
  `total_count` int DEFAULT '0',
  `claimed_count` int DEFAULT '0',
  `valid_from` datetime DEFAULT NULL,
  `valid_to` datetime DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '1:active, 0:inactive',
  `created_by` bigint unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`coupon_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon_template`
--

/*!40000 ALTER TABLE `coupon_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon_template` ENABLE KEYS */;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish` (
  `dish_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `description` text,
  `cover_image` longtext,
  `month_sales` int DEFAULT '0',
  `base_price` decimal(10,2) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `is_deleted` tinyint DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`dish_id`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `dish_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish` VALUES (1,1,'红烧肉盖饭','经典口味，肥而不腻','',NULL,18.00,1,0,'2025-12-19 00:36:33','2025-12-19 10:27:39',4),(2,1,'宫保鸡丁盖饭','酸甜适口，鸡肉鲜嫩',NULL,0,15.00,1,0,'2025-12-19 00:36:33','2025-12-19 00:36:33',1),(22,1,'Test Dish',NULL,NULL,0,10.00,1,1,'2025-12-19 10:16:32','2025-12-19 10:22:02',1),(23,1,'方便面','好吃','',NULL,10.00,1,0,'2025-12-19 10:21:35','2025-12-19 10:21:55',2);
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;

--
-- Table structure for table `dish_category`
--

DROP TABLE IF EXISTS `dish_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint DEFAULT '1',
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `dish_category_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_category`
--

/*!40000 ALTER TABLE `dish_category` DISABLE KEYS */;
INSERT INTO `dish_category` VALUES (1,1,'招牌主菜',1,'2025-12-19 00:36:33',1,'2025-12-19 00:36:32.621596'),(2,1,'套餐',99,'2025-12-19 01:44:46',1,'2025-12-19 01:44:45.521459'),(3,1,'主食',99,'2025-12-19 09:58:44',1,'2025-12-19 09:58:43.522024'),(4,1,'热销',99,'2025-12-19 10:22:56',1,'2025-12-19 10:22:56.289169');
/*!40000 ALTER TABLE `dish_category` ENABLE KEYS */;

--
-- Table structure for table `dish_spec`
--

DROP TABLE IF EXISTS `dish_spec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_spec` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `dish_id` bigint unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int DEFAULT '0',
  `is_default` tinyint DEFAULT '0' COMMENT '1:default, 0:not default',
  `spicy_level` tinyint DEFAULT '0' COMMENT '0-5 level',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `sort_order` int DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `dish_spec_ibfk_1` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_spec`
--

/*!40000 ALTER TABLE `dish_spec` DISABLE KEYS */;
/*!40000 ALTER TABLE `dish_spec` ENABLE KEYS */;

--
-- Table structure for table `dish_spec_option`
--

DROP TABLE IF EXISTS `dish_spec_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_spec_option` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `spec_id` bigint unsigned NOT NULL,
  `option_type` varchar(50) DEFAULT NULL COMMENT 'size,extra,ingredient',
  `option_name` varchar(100) DEFAULT NULL,
  `extra_price` decimal(10,2) DEFAULT '0.00',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `spec_id` (`spec_id`),
  CONSTRAINT `dish_spec_option_ibfk_1` FOREIGN KEY (`spec_id`) REFERENCES `dish_spec` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_spec_option`
--

/*!40000 ALTER TABLE `dish_spec_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `dish_spec_option` ENABLE KEYS */;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `target_type` varchar(20) DEFAULT NULL COMMENT 'dish,canteen',
  `target_id` bigint unsigned NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`target_type`,`target_id`),
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;

--
-- Table structure for table `footprint`
--

DROP TABLE IF EXISTS `footprint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `footprint` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `target_type` varchar(20) DEFAULT NULL COMMENT 'dish,canteen',
  `target_id` bigint unsigned NOT NULL,
  `viewed_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `footprint_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `footprint`
--

/*!40000 ALTER TABLE `footprint` DISABLE KEYS */;
/*!40000 ALTER TABLE `footprint` ENABLE KEYS */;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `menu_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `is_active` tinyint DEFAULT '1' COMMENT '1:active, 0:inactive',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`menu_id`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;

--
-- Table structure for table `menu_dish`
--

DROP TABLE IF EXISTS `menu_dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_dish` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `dish_id` bigint unsigned NOT NULL,
  `menu_id` bigint unsigned NOT NULL,
  `sort_order` int DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dish_id` (`dish_id`,`menu_id`),
  KEY `menu_id` (`menu_id`),
  CONSTRAINT `menu_dish_ibfk_1` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`dish_id`),
  CONSTRAINT `menu_dish_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_dish`
--

/*!40000 ALTER TABLE `menu_dish` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu_dish` ENABLE KEYS */;

--
-- Table structure for table `merchant_account`
--

DROP TABLE IF EXISTS `merchant_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_account` (
  `merchant_account_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `canteen_id` bigint unsigned DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `real_name` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL COMMENT 'admin,staff',
  `status` tinyint DEFAULT '1',
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`merchant_account_id`),
  UNIQUE KEY `username` (`username`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `merchant_account_ibfk_1` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_account`
--

/*!40000 ALTER TABLE `merchant_account` DISABLE KEYS */;
INSERT INTO `merchant_account` VALUES (1,1,'merchant','$2a$10$gjxEnNg7MwWsEw8bFLmJB.CxHkbqe8vt29ACb8dtfJuN2jlZPMPUa','及木商家',NULL,'ADMIN',0,'2025-12-21 14:42:12','2025-12-19 00:30:52','2025-12-21 14:42:12'),(2,4,'apitestflat','$2a$10$HVWEpy5Nc0JlLpMnkupYwOW9ADveUBvPrblo7UTwazCmeR7IRPMWi','API Admin Flat','13900000001','ADMIN',1,NULL,'2025-12-19 12:31:43','2025-12-19 13:02:14');
/*!40000 ALTER TABLE `merchant_account` ENABLE KEYS */;

--
-- Table structure for table `merchant_operation_log`
--

DROP TABLE IF EXISTS `merchant_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_operation_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `merchant_account_id` bigint unsigned NOT NULL,
  `operation` varchar(100) DEFAULT NULL,
  `detail` json DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `merchant_account_id` (`merchant_account_id`),
  CONSTRAINT `merchant_operation_log_ibfk_1` FOREIGN KEY (`merchant_account_id`) REFERENCES `merchant_account` (`merchant_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_operation_log`
--

/*!40000 ALTER TABLE `merchant_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_operation_log` ENABLE KEYS */;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `order_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `canteen_id` bigint unsigned NOT NULL,
  `review_id` bigint unsigned DEFAULT NULL,
  `order_no` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT 'pending,paid,preparing,ready,completed,cancelled,refunded',
  `dining_mode` varchar(20) DEFAULT NULL COMMENT 'dine_in,takeaway,delivery',
  `reserve_start` datetime DEFAULT NULL,
  `reserve_end` datetime DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `package_fee` decimal(10,2) DEFAULT '0.00',
  `discount_amount` decimal(10,2) DEFAULT '0.00',
  `paid_amount` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(20) DEFAULT NULL COMMENT 'wechat,alipay,balance,cash',
  `pickup_code` varchar(10) DEFAULT NULL,
  `pickup_window` varchar(50) DEFAULT NULL,
  `remark` text,
  `cancel_reason` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `user_id` (`user_id`),
  KEY `canteen_id` (`canteen_id`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `order_ibfk_2` FOREIGN KEY (`canteen_id`) REFERENCES `canteen` (`canteen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,1,1,NULL,'2479219629d04920','COMPLETED','DINE_IN',NULL,NULL,18.00,0.00,0.00,18.00,'WECHAT','B012',NULL,NULL,NULL,'2025-12-19 00:36:33','2025-12-19 01:10:36'),(2,1,1,NULL,'0a2b1bda3054463f','COMPLETED','DINE_IN',NULL,NULL,15.00,0.00,0.00,15.00,'WECHAT','B013',NULL,NULL,NULL,'2025-12-19 00:36:33','2025-12-19 01:10:36'),(3,1,1,NULL,'335a8e9272e84dae','COMPLETED','DINE_IN',NULL,NULL,18.00,0.00,0.00,18.00,'WECHAT','B014',NULL,NULL,NULL,'2025-12-19 00:36:33','2025-12-19 01:10:37'),(5,1,1,NULL,'ORD-1766163636402-12D16CA9','COMPLETED','DINE_IN',NULL,NULL,25.00,NULL,NULL,NULL,NULL,'100',NULL,'',NULL,'2025-12-20 01:00:36','2025-12-21 14:42:31'),(6,1,1,NULL,'ORD-1766163748100-74943100','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'100',NULL,'',NULL,'2025-12-20 01:02:28','2025-12-21 14:42:31'),(7,1,1,NULL,'ORD-1766163868086-1CAFF9C8','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'100',NULL,'',NULL,'2025-12-20 01:04:28','2025-12-21 14:42:31'),(8,1,1,NULL,'ORD-1766164229575-63E4D304','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'100',NULL,'',NULL,'2025-12-20 01:10:30','2025-12-21 14:42:31'),(9,1,1,NULL,'ORD-1766164594128-6F6CE88A','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'100',NULL,'',NULL,'2025-12-20 01:16:34','2025-12-21 14:42:32'),(10,1,1,NULL,'ORD-1766164880078-D881DE4D','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'100',NULL,'',NULL,'2025-12-20 01:21:20','2025-12-21 14:42:32'),(37,5,1,NULL,'ORD-1766296291732-35B2D165','COMPLETED','DINE_IN',NULL,NULL,25.00,NULL,NULL,NULL,NULL,'101',NULL,'',NULL,'2025-12-21 13:51:32','2025-12-21 14:42:32'),(38,5,1,NULL,'ORD-1766297212687-74D82027','COMPLETED','DINE_IN',NULL,NULL,25.00,NULL,NULL,NULL,NULL,'102',NULL,'',NULL,'2025-12-21 14:06:53','2025-12-21 14:42:32'),(39,5,1,NULL,'ORD-1766297939408-637D668D','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'103',NULL,'',NULL,'2025-12-21 14:18:59','2025-12-21 14:29:43'),(40,5,1,NULL,'ORD-1766298476097-10A8C02C','COMPLETED','DINE_IN',NULL,NULL,15.00,NULL,NULL,NULL,NULL,'104',NULL,'',NULL,'2025-12-21 14:27:56','2025-12-21 14:29:41'),(41,5,1,NULL,'ORD-1766299101703-1089728C','COMPLETED','DINE_IN',NULL,NULL,32.00,NULL,NULL,NULL,NULL,'105',NULL,'香辣牛肉面',NULL,'2025-12-21 14:38:22','2025-12-21 14:38:42');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint unsigned NOT NULL,
  `dish_id` bigint unsigned NOT NULL,
  `dish_name` varchar(200) DEFAULT NULL,
  `spec_name` varchar(100) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT '1',
  `extra_options` json DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`dish_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,'红烧肉盖饭',NULL,18.00,1,NULL,18.00),(2,2,2,'宫保鸡丁盖饭',NULL,15.00,1,NULL,15.00),(3,3,1,'红烧肉盖饭',NULL,18.00,1,NULL,18.00),(4,5,2,'宫保鸡丁盖饭',NULL,NULL,1,NULL,NULL),(5,5,23,'方便面',NULL,NULL,1,NULL,NULL),(6,6,2,'宫保鸡丁盖饭',NULL,NULL,1,NULL,NULL),(7,7,2,'宫保鸡丁盖饭',NULL,NULL,1,NULL,NULL),(8,8,2,'宫保鸡丁盖饭',NULL,NULL,1,NULL,NULL),(9,9,2,'宫保鸡丁盖饭',NULL,NULL,1,NULL,NULL),(10,10,2,'宫保鸡丁盖饭',NULL,NULL,1,NULL,NULL),(11,37,2,NULL,NULL,15.00,1,NULL,15.00),(12,37,23,NULL,NULL,10.00,1,NULL,10.00),(13,38,2,NULL,NULL,15.00,1,NULL,15.00),(14,38,23,NULL,NULL,10.00,1,NULL,10.00),(15,39,2,NULL,NULL,15.00,1,NULL,15.00),(16,40,2,'宫保鸡丁盖饭',NULL,15.00,1,NULL,15.00),(17,41,23,'方便面',NULL,10.00,4,NULL,40.00);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;

--
-- Table structure for table `order_status_log`
--

DROP TABLE IF EXISTS `order_status_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_status_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint unsigned NOT NULL,
  `from_status` varchar(20) DEFAULT NULL,
  `to_status` varchar(20) DEFAULT NULL,
  `operator_type` varchar(20) DEFAULT NULL COMMENT 'user,merchant,admin,system',
  `operator_id` bigint unsigned DEFAULT NULL,
  `remark` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `order_status_log_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_status_log`
--

/*!40000 ALTER TABLE `order_status_log` DISABLE KEYS */;
INSERT INTO `order_status_log` VALUES (1,1,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-19 01:10:10'),(2,1,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-19 01:10:32'),(3,2,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-19 01:10:34'),(4,1,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-19 01:10:36'),(5,2,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-19 01:10:36'),(6,3,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-19 01:10:37'),(7,5,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-20 01:00:36'),(8,5,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-20 01:00:37'),(9,6,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-20 01:02:28'),(10,6,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-20 01:02:28'),(11,7,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-20 01:04:28'),(12,7,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-20 01:04:28'),(13,8,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-20 01:10:30'),(14,8,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-20 01:10:30'),(15,9,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-20 01:16:34'),(16,9,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-20 01:16:34'),(17,10,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-20 01:21:20'),(18,10,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-20 01:21:20'),(19,37,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-21 13:51:32'),(20,37,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-21 13:51:32'),(21,38,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-21 14:06:53'),(22,38,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-21 14:06:53'),(23,39,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-21 14:18:59'),(24,39,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-21 14:18:59'),(25,40,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-21 14:27:56'),(26,40,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-21 14:27:56'),(27,40,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:29:38'),(28,40,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:29:39'),(29,40,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:29:41'),(30,39,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:29:42'),(31,39,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:29:42'),(32,39,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:29:43'),(33,41,NULL,'PENDING_PAYMENT','SYSTEM',0,'Order created','2025-12-21 14:38:22'),(34,41,'PENDING_PAYMENT','PAID','SYSTEM',0,'Payment successful via WECHAT','2025-12-21 14:38:22'),(35,41,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:38:32'),(36,41,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:38:37'),(37,41,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:38:42'),(38,5,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:24'),(39,6,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:24'),(40,7,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:25'),(41,8,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:25'),(42,9,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:25'),(43,10,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:25'),(44,37,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:26'),(45,38,'PAID','PREPARING','MERCHANT',1,'Merchant accepted order','2025-12-21 14:42:26'),(46,5,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:28'),(47,6,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:28'),(48,7,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:29'),(49,8,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:29'),(50,9,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:29'),(51,10,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:29'),(52,37,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:29'),(53,38,'PREPARING','READY_FOR_PICKUP','MERCHANT',1,'Meal is ready for pickup','2025-12-21 14:42:30'),(54,5,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:31'),(55,6,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:31'),(56,7,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:31'),(57,8,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:31'),(58,9,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:32'),(59,10,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:32'),(60,37,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:32'),(61,38,'READY_FOR_PICKUP','COMPLETED','MERCHANT',1,'Order completed','2025-12-21 14:42:32');
/*!40000 ALTER TABLE `order_status_log` ENABLE KEYS */;

--
-- Table structure for table `payment_record`
--

DROP TABLE IF EXISTS `payment_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint unsigned NOT NULL,
  `pay_no` varchar(100) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL COMMENT 'wechat,alipay,balance',
  `amount` decimal(10,2) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT 'pending,success,failed',
  `paid_at` datetime DEFAULT NULL,
  `raw_response` json DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `payment_record_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_record`
--

/*!40000 ALTER TABLE `payment_record` DISABLE KEYS */;
INSERT INTO `payment_record` VALUES (1,5,'PAY-1766163636544','WECHAT',NULL,'SUCCESS','2025-12-20 01:00:37',NULL,'2025-12-20 01:00:37'),(2,6,'PAY-1766163748158','WECHAT',NULL,'SUCCESS','2025-12-20 01:02:28',NULL,'2025-12-20 01:02:28'),(3,7,'PAY-1766163868130','WECHAT',NULL,'SUCCESS','2025-12-20 01:04:28',NULL,'2025-12-20 01:04:28'),(4,8,'PAY-1766164229689','WECHAT',NULL,'SUCCESS','2025-12-20 01:10:30',NULL,'2025-12-20 01:10:30'),(5,9,'PAY-1766164594229','WECHAT',NULL,'SUCCESS','2025-12-20 01:16:34',NULL,'2025-12-20 01:16:34'),(6,10,'PAY-1766164880202','WECHAT',NULL,'SUCCESS','2025-12-20 01:21:20',NULL,'2025-12-20 01:21:20'),(7,37,'PAY-1766296291838','WECHAT',NULL,'SUCCESS','2025-12-21 13:51:32',NULL,'2025-12-21 13:51:32'),(8,38,'PAY-1766297212756','WECHAT',NULL,'SUCCESS','2025-12-21 14:06:53',NULL,'2025-12-21 14:06:53'),(9,39,'PAY-1766297939451','WECHAT',NULL,'SUCCESS','2025-12-21 14:18:59',NULL,'2025-12-21 14:18:59'),(10,40,'PAY-1766298476184','WECHAT',NULL,'SUCCESS','2025-12-21 14:27:56',NULL,'2025-12-21 14:27:56'),(11,41,'PAY-1766299101771','WECHAT',NULL,'SUCCESS','2025-12-21 14:38:22',NULL,'2025-12-21 14:38:22');
/*!40000 ALTER TABLE `payment_record` ENABLE KEYS */;

--
-- Table structure for table `system_monitor`
--

DROP TABLE IF EXISTS `system_monitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_monitor` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `service_name` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT 'up,down,warning',
  `metrics` json DEFAULT NULL,
  `checked_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_monitor`
--

/*!40000 ALTER TABLE `system_monitor` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_monitor` ENABLE KEYS */;

--
-- Table structure for table `system_param`
--

DROP TABLE IF EXISTS `system_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_param` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `param_key` varchar(100) DEFAULT NULL,
  `param_value` text,
  `description` varchar(500) DEFAULT NULL,
  `updated_by` bigint unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_param`
--

/*!40000 ALTER TABLE `system_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_param` ENABLE KEYS */;

--
-- Table structure for table `transaction_record`
--

DROP TABLE IF EXISTS `transaction_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `order_id` bigint unsigned NOT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT 'payment,refund,recharge,withdraw',
  `amount` decimal(10,2) DEFAULT NULL,
  `payment_channel` varchar(20) DEFAULT NULL,
  `trade_no` varchar(100) DEFAULT NULL,
  `trade_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `transaction_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `transaction_record_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_record`
--

/*!40000 ALTER TABLE `transaction_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_record` ENABLE KEYS */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) DEFAULT NULL,
  `unionid` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `avatar_url` varchar(500) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test-openid-123',NULL,'微信用户','https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132',NULL,1,'2025-12-20 01:54:33','2025-12-19 00:36:33','2025-12-20 01:54:33'),(2,NULL,NULL,NULL,NULL,NULL,1,'2025-12-20 12:19:04','2025-12-20 12:05:49','2025-12-20 12:19:04'),(3,'test-openid-634',NULL,NULL,NULL,NULL,1,'2025-12-20 12:26:08','2025-12-20 12:26:08','2025-12-20 12:39:17'),(4,'oa2tj190AvQOCZ_Nb9TjbmALiquc',NULL,NULL,NULL,NULL,1,'2025-12-21 12:37:05','2025-12-20 12:27:26','2025-12-21 12:37:05'),(5,'oOchk14S-8402vF886otqKG5hTgI',NULL,NULL,NULL,NULL,1,'2025-12-21 14:06:43','2025-12-21 13:12:40','2025-12-21 14:06:43');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_address` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `receiver` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `campus` varchar(100) DEFAULT NULL,
  `building` varchar(200) DEFAULT NULL,
  `detail_address` varchar(500) DEFAULT NULL,
  `is_default` tinyint DEFAULT '0' COMMENT '1:default, 0:not default',
  `is_deleted` tinyint DEFAULT '0' COMMENT '0:normal, 1:deleted',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;

--
-- Table structure for table `user_asset`
--

DROP TABLE IF EXISTS `user_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_asset` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `balance` decimal(10,2) DEFAULT '0.00',
  `points` int DEFAULT '0',
  `total_recharge` decimal(10,2) DEFAULT '0.00',
  `total_spend` decimal(10,2) DEFAULT '0.00',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `user_asset_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_asset`
--

/*!40000 ALTER TABLE `user_asset` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_asset` ENABLE KEYS */;

--
-- Table structure for table `user_coupon`
--

DROP TABLE IF EXISTS `user_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_coupon` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `coupon_template_id` bigint unsigned NOT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT 'unused,used,expired',
  `user_order_id` bigint unsigned DEFAULT NULL,
  `claimed_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `used_at` datetime DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`coupon_template_id`),
  CONSTRAINT `user_coupon_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_coupon`
--

/*!40000 ALTER TABLE `user_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_coupon` ENABLE KEYS */;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_notification` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT 'order,promotion,system',
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  `is_read` tinyint DEFAULT '0' COMMENT '0:unread, 1:read',
  `extra` json DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_notification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notification`
--

/*!40000 ALTER TABLE `user_notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_notification` ENABLE KEYS */;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_profile` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `gender` tinyint DEFAULT NULL COMMENT '0:unknown, 1:male, 2:female',
  `campus_card_no` varchar(50) DEFAULT NULL,
  `campus_card_status` varchar(20) DEFAULT NULL COMMENT 'active,inactive,lost',
  `birthday` date DEFAULT NULL,
  `remark` text,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `user_profile_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;

--
-- Dumping routines for database 'hzcuorder'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-21 22:36:22
