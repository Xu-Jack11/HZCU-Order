-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: diancan
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
  `code` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `module` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `role_id` char(10) NOT NULL,
  `name` char(10) DEFAULT NULL,
  `description` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `role_id` char(10) NOT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  CONSTRAINT `FK_admin_role_permission` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `admin_id` char(10) NOT NULL,
  `username` char(10) DEFAULT NULL,
  `password_hash` char(10) DEFAULT NULL,
  `real_name` char(10) DEFAULT NULL,
  `mobile` char(10) DEFAULT NULL,
  `email` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `last_login_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  `Attribute_164` char(10) DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;

--
-- Table structure for table `admin_user_role`
--

DROP TABLE IF EXISTS `admin_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_user_role` (
  `role_id` char(10) NOT NULL,
  `admin_id` char(10) NOT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_role`
--

/*!40000 ALTER TABLE `admin_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_user_role` ENABLE KEYS */;

--
-- Table structure for table `after_sale`
--

DROP TABLE IF EXISTS `after_sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `after_sale` (
  `order_id` char(10) DEFAULT NULL,
  `user_id` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `content` char(10) DEFAULT NULL,
  `evidence` char(10) DEFAULT NULL,
  `handled_by` char(10) DEFAULT NULL,
  `handled_at` char(10) DEFAULT NULL,
  `result` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `after_sale`
--

/*!40000 ALTER TABLE `after_sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `after_sale` ENABLE KEYS */;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcement` (
  `announcement_id` char(10) NOT NULL,
  `scope` char(10) DEFAULT NULL,
  `target_id` char(10) DEFAULT NULL,
  `title` char(10) DEFAULT NULL,
  `context` char(10) DEFAULT NULL,
  `effective_from` char(10) DEFAULT NULL,
  `effective_to` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_by` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `id` char(10) NOT NULL,
  `operator_type` char(10) DEFAULT NULL,
  `operator_id` char(10) DEFAULT NULL,
  `action` char(10) DEFAULT NULL,
  `request_path` char(10) DEFAULT NULL,
  `changes` char(10) DEFAULT NULL,
  `ip_address` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `banner_id` char(10) NOT NULL,
  `title` char(10) DEFAULT NULL,
  `image_url` char(10) DEFAULT NULL,
  `jump_link` char(10) DEFAULT NULL,
  `position` char(10) DEFAULT NULL,
  `sort_order` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_by` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`banner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `total_price` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `dish_id` char(10) NOT NULL,
  `quantity` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `canteen_id` char(10) NOT NULL,
  `name` char(10) DEFAULT NULL,
  `campus` char(10) DEFAULT NULL,
  `location` char(10) DEFAULT NULL,
  `contact_phone` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `business_hours` char(10) DEFAULT NULL,
  `service_fee_rate` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`canteen_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canteen`
--

/*!40000 ALTER TABLE `canteen` DISABLE KEYS */;
/*!40000 ALTER TABLE `canteen` ENABLE KEYS */;

--
-- Table structure for table `canteen_announcement`
--

DROP TABLE IF EXISTS `canteen_announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `canteen_announcement` (
  `canteen_id` char(10) DEFAULT NULL,
  `title` char(10) DEFAULT NULL,
  `content` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `canteen_id` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `estimated_wait_time` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canteen_status`
--

/*!40000 ALTER TABLE `canteen_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `canteen_status` ENABLE KEYS */;

--
-- Table structure for table `canteenshops`
--

DROP TABLE IF EXISTS `canteenshops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `canteenshops` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `monthly_sales` int DEFAULT NULL,
  `wait_time` int DEFAULT NULL,
  `distance` varchar(64) DEFAULT NULL,
  `min_price` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canteenshops`
--

/*!40000 ALTER TABLE `canteenshops` DISABLE KEYS */;
INSERT INTO `canteenshops` VALUES (1,'肯德基（城院店）','/images/shops/kfc.png',4.8,719,15,'1.8km',0),(2,'兰州拉面','/images/shops/lamian.png',4.7,1750,10,'2.4km',0),(3,'益禾堂','/images/shops/yihetang.png',4.3,NULL,NULL,NULL,NULL),(4,'蜜雪冰城','/images/shops/mixue.png',4.6,NULL,NULL,NULL,NULL),(5,'沙县小吃','/images/shops/shaxian.png',4.2,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `canteenshops` ENABLE KEYS */;

--
-- Table structure for table `cart_snapshot`
--

DROP TABLE IF EXISTS `cart_snapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_snapshot` (
  `user_id` char(10) DEFAULT NULL,
  `items` char(10) DEFAULT NULL,
  `total_amount` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `coupon_template_id` char(10) NOT NULL,
  `scope_type` char(10) DEFAULT NULL,
  `scope_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `threshold_amount` char(10) DEFAULT NULL,
  `discount_amount` char(10) DEFAULT NULL,
  `discount_rate` char(10) DEFAULT NULL,
  `total_count` char(10) DEFAULT NULL,
  `claimed_count` char(10) DEFAULT NULL,
  `valid_from` char(10) DEFAULT NULL,
  `valid_to` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_by` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`coupon_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `dish_id` char(10) NOT NULL,
  `name` char(10) DEFAULT NULL,
  `description` char(10) DEFAULT NULL,
  `cover_image` char(10) DEFAULT NULL,
  `month_sales` char(10) DEFAULT NULL,
  `base_price` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `is_deleted` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;

--
-- Table structure for table `dish_categories`
--

DROP TABLE IF EXISTS `dish_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shop_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `sort_order` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_categories`
--

/*!40000 ALTER TABLE `dish_categories` DISABLE KEYS */;
INSERT INTO `dish_categories` VALUES (1,4,'人气热销',1),(2,4,'冰淇淋系列',2),(3,4,'鲜果茶',3),(4,3,'招牌奶茶',1),(5,3,'清新果茶',2),(6,5,'经典小吃',1),(7,5,'营养炖罐',2),(8,6,'冰淇淋系列',2);
/*!40000 ALTER TABLE `dish_categories` ENABLE KEYS */;

--
-- Table structure for table `dish_category`
--

DROP TABLE IF EXISTS `dish_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_category` (
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `sort_order` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_category`
--

/*!40000 ALTER TABLE `dish_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `dish_category` ENABLE KEYS */;

--
-- Table structure for table `dish_spec`
--

DROP TABLE IF EXISTS `dish_spec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_spec` (
  `dish_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `price` char(10) DEFAULT NULL,
  `stock` char(10) DEFAULT NULL,
  `is_default` char(10) DEFAULT NULL,
  `spicy_level` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `option_type` char(10) DEFAULT NULL,
  `option_name` char(10) DEFAULT NULL,
  `extra_price` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_spec_option`
--

/*!40000 ALTER TABLE `dish_spec_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `dish_spec_option` ENABLE KEYS */;

--
-- Table structure for table `dishes`
--

DROP TABLE IF EXISTS `dishes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dishes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `image` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `monthly_sales` int DEFAULT '0',
  `good_rate` int DEFAULT '100',
  `tags` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dishes`
--

/*!40000 ALTER TABLE `dishes` DISABLE KEYS */;
INSERT INTO `dishes` VALUES (1,1,'冰鲜柠檬水','新鲜柠檬，清爽解渴','/images/goods/lemonade.png',4.00,4.00,9999,100,'推荐,解渴'),(2,1,'珍珠奶茶','Q弹珍珠，香浓奶茶','/images/goods/milktea.png',7.00,7.00,5000,98,'招牌'),(3,2,'新鲜冰淇淋','奶香浓郁，入口即化','/images/goods/icecream.png',3.00,3.00,8000,99,'超值'),(4,4,'益禾烤奶','经典焦香，回味无穷','/images/goods/kaonai.png',8.00,8.00,12000,99,'招牌,必点'),(5,4,'泷珠奶茶','Q弹珍珠，大口满足','/images/goods/longzhu.png',10.00,10.00,6000,97,'推荐'),(6,6,'飘香拌面','花生酱浓郁，面条劲道','/images/goods/banmian.png',5.00,5.00,8000,95,'实惠'),(7,6,'柳叶蒸饺','皮薄馅大，现蒸现卖','/images/goods/zhengjiao.png',6.00,6.00,7500,96,'推荐'),(8,7,'茶树菇排骨汤','清热去火，营养滋补','/images/goods/soup.png',12.00,12.00,3000,98,'养生');
/*!40000 ALTER TABLE `dishes` ENABLE KEYS */;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `user_id` char(10) DEFAULT NULL,
  `target_type` char(10) DEFAULT NULL,
  `target_id` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` char(10) DEFAULT NULL,
  `target_type` char(10) DEFAULT NULL,
  `target_id` char(10) DEFAULT NULL,
  `viewed_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `menu_id` char(10) NOT NULL,
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `start_time` char(10) DEFAULT NULL,
  `end_time` char(10) DEFAULT NULL,
  `is_active` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `dish_id` char(10) NOT NULL,
  `menu_id` char(10) NOT NULL,
  `sort_order` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `merchant_account_id` char(10) NOT NULL,
  `canteen_id` char(10) DEFAULT NULL,
  `username` char(10) DEFAULT NULL,
  `password_has` char(10) DEFAULT NULL,
  `real_name` char(10) DEFAULT NULL,
  `mobile` char(10) DEFAULT NULL,
  `role` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `last_login_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`merchant_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_account`
--

/*!40000 ALTER TABLE `merchant_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_account` ENABLE KEYS */;

--
-- Table structure for table `merchant_operation_log`
--

DROP TABLE IF EXISTS `merchant_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_operation_log` (
  `merchant_account_id` char(10) DEFAULT NULL,
  `operation` char(10) DEFAULT NULL,
  `detail` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `order_id` char(10) NOT NULL,
  `user_id` char(10) DEFAULT NULL,
  `canteen_id` char(10) DEFAULT NULL,
  `review_id` char(10) DEFAULT NULL,
  `order_no` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `dining_mode` char(10) DEFAULT NULL,
  `reserve_start` char(10) DEFAULT NULL,
  `reserve_end` char(10) DEFAULT NULL,
  `total_amount` char(10) DEFAULT NULL,
  `package_fee` char(10) DEFAULT NULL,
  `discount_amount` char(10) DEFAULT NULL,
  `paid_amount` char(10) DEFAULT NULL,
  `payment_method` char(10) DEFAULT NULL,
  `pickup_code` char(10) DEFAULT NULL,
  `pickup_window` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `cancel_reason` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;

--
-- Table structure for table `order_goods`
--

DROP TABLE IF EXISTS `order_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_goods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `goods_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `count` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_goods`
--

/*!40000 ALTER TABLE `order_goods` DISABLE KEYS */;
INSERT INTO `order_goods` VALUES (1,1,101,'嫩牛五方超值单人餐','/images/goods/niuwufang.png',19.50,1),(2,2,102,'香辣鸡腿堡单人餐','/images/goods/jileitui.png',25.90,1),(3,3,4,'益禾烤奶','/images/goods/kaonai.png',8.00,1),(4,4,101,'嫩牛五方超值单人餐','/images/goods/niuwufang.png',19.50,1),(5,5,102,'香辣鸡腿堡单人餐','/images/goods/jileitui.png',25.90,1),(6,6,501,'兰州牛肉拉面','/images/goods/lamian.png',15.00,1),(7,7,503,'牛肉面+小菜套餐','/images/goods/set.png',22.00,1),(8,8,101,'嫩牛五方超值单人餐','/images/goods/niuwufang.png',19.50,1),(9,9,301,'奥尔良烤鸡腿堡套餐','/images/goods/orleans.png',32.00,1),(10,10,301,'奥尔良烤鸡腿堡套餐','/images/goods/orleans.png',32.00,1),(11,11,101,'嫩牛五方超值单人餐','/images/goods/niuwufang.png',19.50,1),(12,12,1,'Test Product','/images/goods/test.png',10.00,1),(13,13,101,'TestProduct','/test.png',10.00,1),(14,14,101,'TestProduct','/test.png',10.00,1),(15,15,101,'TestProduct','/test.png',10.00,1),(16,16,101,'嫩牛五方超值单人餐','/images/goods/niuwufang.png',19.50,1);
/*!40000 ALTER TABLE `order_goods` ENABLE KEYS */;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `dish_id` char(10) NOT NULL,
  `order_id` char(10) NOT NULL,
  `dish_name` char(10) DEFAULT NULL,
  `spec_name` char(10) DEFAULT NULL,
  `unit_price` char(10) DEFAULT NULL,
  `quantity` char(10) DEFAULT NULL,
  `extra_options` char(10) DEFAULT NULL,
  `total_price` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;

--
-- Table structure for table `order_status_log`
--

DROP TABLE IF EXISTS `order_status_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_status_log` (
  `order_id` char(10) DEFAULT NULL,
  `from_status` char(10) DEFAULT NULL,
  `to_status` char(10) DEFAULT NULL,
  `operator_type` char(10) DEFAULT NULL,
  `operator_id` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_status_log`
--

/*!40000 ALTER TABLE `order_status_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_status_log` ENABLE KEYS */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `shop_id` bigint NOT NULL,
  `shop_name` varchar(255) DEFAULT NULL,
  `shop_logo` varchar(255) DEFAULT NULL,
  `total_count` int DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `status_text` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `dining_mode` varchar(50) DEFAULT NULL,
  `table_no` varchar(50) DEFAULT NULL,
  `pickup_time` varchar(50) DEFAULT NULL,
  `remark` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,21.50,'preparing','制作中','2025-12-14 14:08:31','takeaway','','',''),(2,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,27.90,'canceled','已取消','2025-12-14 17:16:08','takeaway','','','加葱'),(3,1,3,'库迪咖啡（城院南校区店）','/images/shops/cotti.png',1,10.00,'preparing','制作中','2025-12-14 22:40:41','takeaway','','',''),(4,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,21.50,'canceled','已取消','2025-12-14 22:48:11','takeaway','','',''),(5,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,27.90,'preparing','制作中','2025-12-14 22:58:30','takeaway','','',''),(6,1,2,'兰州拉面','/images/shops/lamian.png',1,17.00,'canceled','已取消','2025-12-14 23:00:16','takeaway','','',''),(7,1,2,'兰州拉面','/images/shops/lamian.png',1,24.00,'completed','已完成','2025-12-14 23:00:40','takeaway','','',''),(8,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,21.50,'completed','已完成','2025-12-15 14:00:31','takeaway','','',''),(9,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,34.00,'completed','已完成','2025-12-15 19:09:56','takeaway','','',''),(10,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,34.00,'completed','已完成','2025-12-16 17:47:04','takeaway','','',''),(11,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,21.50,'preparing','制作中','2025-12-17 16:00:40','takeaway','','',''),(12,1,1,'肯德基（城院店）','/images/shops/kfc.png',1,10.00,'pending','待付款','2025-12-18 13:11:02','takeaway','','Immediately','Test order'),(13,5,1,'肯德基（城院店）','/images/shops/kfc.png',1,10.00,'pending','待付款','2025-12-18 13:52:57','takeaway',NULL,NULL,NULL),(14,5,1,'肯德基（城院店）','/images/shops/kfc.png',1,10.00,'pending','待付款','2025-12-18 13:53:22','takeaway',NULL,NULL,NULL),(15,5,1,'肯德基（城院店）','/images/shops/kfc.png',1,10.00,'pending','待付款','2025-12-18 13:53:28','takeaway',NULL,NULL,NULL),(16,7,1,'肯德基（城院店）','/images/shops/kfc.png',1,21.50,'preparing','制作中','2025-12-18 13:57:50','takeaway','','','');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;

--
-- Table structure for table `payment_record`
--

DROP TABLE IF EXISTS `payment_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_record` (
  `order_id` char(10) DEFAULT NULL,
  `pay_no` char(10) DEFAULT NULL,
  `channel` char(10) DEFAULT NULL,
  `amount` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `paid_at` char(10) DEFAULT NULL,
  `raw_response` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_record`
--

/*!40000 ALTER TABLE `payment_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_record` ENABLE KEYS */;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `rule` char(10) DEFAULT NULL,
  `start_time` char(10) DEFAULT NULL,
  `end_time` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;

--
-- Table structure for table `promotion_dish`
--

DROP TABLE IF EXISTS `promotion_dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion_dish` (
  `dish_id` char(10) NOT NULL,
  `discount_price` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion_dish`
--

/*!40000 ALTER TABLE `promotion_dish` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion_dish` ENABLE KEYS */;

--
-- Table structure for table `refund_record`
--

DROP TABLE IF EXISTS `refund_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund_record` (
  `order_id` char(10) DEFAULT NULL,
  `after_sale_id` char(10) DEFAULT NULL,
  `refund_no` char(10) DEFAULT NULL,
  `amount` char(10) DEFAULT NULL,
  `reason` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `processed_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund_record`
--

/*!40000 ALTER TABLE `refund_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `refund_record` ENABLE KEYS */;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `review_id` char(10) NOT NULL,
  `user_id` char(10) DEFAULT NULL,
  `order_id` char(10) DEFAULT NULL,
  `rating` char(10) DEFAULT NULL,
  `content` char(10) DEFAULT NULL,
  `images` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;

--
-- Table structure for table `review_audit_log`
--

DROP TABLE IF EXISTS `review_audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_audit_log` (
  `review_id` char(10) DEFAULT NULL,
  `admin_id` char(10) DEFAULT NULL,
  `action` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_audit_log`
--

/*!40000 ALTER TABLE `review_audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `review_audit_log` ENABLE KEYS */;

--
-- Table structure for table `settlement`
--

DROP TABLE IF EXISTS `settlement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlement` (
  `settlement_id` char(10) NOT NULL,
  `canteen_id` char(10) DEFAULT NULL,
  `period_start` char(10) DEFAULT NULL,
  `period_end` char(10) DEFAULT NULL,
  `order_count` char(10) DEFAULT NULL,
  `gross_amount` char(10) DEFAULT NULL,
  `platform_fee` char(10) DEFAULT NULL,
  `service_fee` char(10) DEFAULT NULL,
  `net_amount` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `confirmed_at` char(10) DEFAULT NULL,
  `paid_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`settlement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settlement`
--

/*!40000 ALTER TABLE `settlement` DISABLE KEYS */;
/*!40000 ALTER TABLE `settlement` ENABLE KEYS */;

--
-- Table structure for table `settlement_order`
--

DROP TABLE IF EXISTS `settlement_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlement_order` (
  `settlement_id` char(10) DEFAULT NULL,
  `order_id` char(10) DEFAULT NULL,
  `order_amount` char(10) DEFAULT NULL,
  `platform_fee` char(10) DEFAULT NULL,
  `service_fee` char(10) DEFAULT NULL,
  `ceated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settlement_order`
--

/*!40000 ALTER TABLE `settlement_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `settlement_order` ENABLE KEYS */;

--
-- Table structure for table `system_monitor`
--

DROP TABLE IF EXISTS `system_monitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_monitor` (
  `id` char(10) NOT NULL,
  `service_name` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `metrics` char(10) DEFAULT NULL,
  `checked_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `id` char(10) NOT NULL,
  `param_key` char(10) DEFAULT NULL,
  `param_value` char(10) DEFAULT NULL,
  `description` char(10) DEFAULT NULL,
  `updated_by` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` char(10) NOT NULL,
  `order_id` char(10) NOT NULL,
  `type` char(10) DEFAULT NULL,
  `amount` char(10) DEFAULT NULL,
  `payment_channel` char(10) DEFAULT NULL,
  `trade_no` char(10) DEFAULT NULL,
  `trade_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
  `openid` varchar(255) DEFAULT NULL COMMENT '微信openid',
  `unionid` varchar(255) DEFAULT NULL COMMENT '微信unionid',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像地址',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` varchar(20) DEFAULT 'active' COMMENT '用户状态',
  `last_login_at` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_address` (
  `receiver` char(10) DEFAULT NULL,
  `phone` char(10) DEFAULT NULL,
  `campus` char(10) DEFAULT NULL,
  `building` char(10) DEFAULT NULL,
  `is_default` char(10) DEFAULT NULL,
  `is_deleted` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` char(10) DEFAULT NULL,
  `balance` char(10) DEFAULT NULL,
  `points` char(10) DEFAULT NULL,
  `total_recharge` char(10) DEFAULT NULL,
  `total_spend` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` char(10) NOT NULL,
  `coupon_template_id` char(10) NOT NULL,
  `status` char(10) DEFAULT NULL,
  `user_order_id` char(10) DEFAULT NULL,
  `claimed_at` char(10) DEFAULT NULL,
  `used_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`coupon_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `title` char(10) DEFAULT NULL,
  `content` char(10) DEFAULT NULL,
  `is_read` char(10) DEFAULT NULL,
  `extra` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `gender` char(10) DEFAULT NULL,
  `campus_card_no` char(10) DEFAULT NULL,
  `campus_card_status` char(10) DEFAULT NULL,
  `birthday` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;

--
-- Dumping routines for database 'diancan'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-18 14:19:24
