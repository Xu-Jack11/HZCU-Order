-- Exported 2025-12-16T02:31:35.820Z
-- Database: diancan
-- Tables with data: 5/51
SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for dishes
-- ----------------------------
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `image` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `monthly_sales` int(11) DEFAULT '0',
  `good_rate` int(11) DEFAULT '100',
  `tags` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of dishes
-- ----------------------------
INSERT INTO `dishes` (`id`, `category_id`, `name`, `description`, `image`, `price`, `original_price`, `monthly_sales`, `good_rate`, `tags`)
VALUES
(1, 1, '冰鲜柠檬水', '新鲜柠檬，清爽解渴', '/images/goods/lemonade.png', '4.00', '4.00', 9999, 100, '推荐,解渴'),
(2, 1, '珍珠奶茶', 'Q弹珍珠，香浓奶茶', '/images/goods/milktea.png', '7.00', '7.00', 5000, 98, '招牌'),
(3, 2, '新鲜冰淇淋', '奶香浓郁，入口即化', '/images/goods/icecream.png', '3.00', '3.00', 8000, 99, '超值'),
(4, 4, '益禾烤奶', '经典焦香，回味无穷', '/images/goods/kaonai.png', '8.00', '8.00', 12000, 99, '招牌,必点'),
(5, 4, '泷珠奶茶', 'Q弹珍珠，大口满足', '/images/goods/longzhu.png', '10.00', '10.00', 6000, 97, '推荐'),
(6, 6, '飘香拌面', '花生酱浓郁，面条劲道', '/images/goods/banmian.png', '5.00', '5.00', 8000, 95, '实惠'),
(7, 6, '柳叶蒸饺', '皮薄馅大，现蒸现卖', '/images/goods/zhengjiao.png', '6.00', '6.00', 7500, 96, '推荐'),
(8, 7, '茶树菇排骨汤', '清热去火，营养滋补', '/images/goods/soup.png', '12.00', '12.00', 3000, 98, '养生');


-- ----------------------------
-- Table structure for dish_categories
-- ----------------------------
DROP TABLE IF EXISTS `dish_categories`;
CREATE TABLE `dish_categories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `sort_order` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of dish_categories
-- ----------------------------
INSERT INTO `dish_categories` (`id`, `shop_id`, `name`, `sort_order`)
VALUES
(1, 4, '人气热销', 1),
(2, 4, '冰淇淋系列', 2),
(3, 4, '鲜果茶', 3),
(4, 3, '招牌奶茶', 1),
(5, 3, '清新果茶', 2),
(6, 5, '经典小吃', 1),
(7, 5, '营养炖罐', 2),
(8, 6, '冰淇淋系列', 2);


-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL,
  `shop_name` varchar(255) DEFAULT NULL,
  `shop_logo` varchar(255) DEFAULT NULL,
  `total_count` int(11) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `status_text` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `dining_mode` varchar(50) DEFAULT NULL,
  `table_no` varchar(50) DEFAULT NULL,
  `pickup_time` varchar(50) DEFAULT NULL,
  `remark` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` (`id`, `shop_id`, `shop_name`, `shop_logo`, `total_count`, `total_price`, `status`, `status_text`, `create_time`, `dining_mode`, `table_no`, `pickup_time`, `remark`)
VALUES
(1, 1, '肯德基（城院店）', '/images/shops/kfc.png', 1, '21.50', 'preparing', '制作中', '2025-12-14 14:08:31', 'takeaway', '', '', ''),
(2, 1, '肯德基（城院店）', '/images/shops/kfc.png', 1, '27.90', 'canceled', '已取消', '2025-12-14 17:16:08', 'takeaway', '', '', '加葱'),
(3, 3, '库迪咖啡（城院南校区店）', '/images/shops/cotti.png', 1, '10.00', 'preparing', '制作中', '2025-12-14 22:40:41', 'takeaway', '', '', ''),
(4, 1, '肯德基（城院店）', '/images/shops/kfc.png', 1, '21.50', 'canceled', '已取消', '2025-12-14 22:48:11', 'takeaway', '', '', ''),
(5, 1, '肯德基（城院店）', '/images/shops/kfc.png', 1, '27.90', 'preparing', '制作中', '2025-12-14 22:58:30', 'takeaway', '', '', ''),
(6, 2, '兰州拉面', '/images/shops/lamian.png', 1, '17.00', 'canceled', '已取消', '2025-12-14 23:00:16', 'takeaway', '', '', ''),
(7, 2, '兰州拉面', '/images/shops/lamian.png', 1, '24.00', 'completed', '已完成', '2025-12-14 23:00:40', 'takeaway', '', '', ''),
(8, 1, '肯德基（城院店）', '/images/shops/kfc.png', 1, '21.50', 'completed', '已完成', '2025-12-15 14:00:31', 'takeaway', '', '', ''),
(9, 1, '肯德基（城院店）', '/images/shops/kfc.png', 1, '34.00', 'completed', '已完成', '2025-12-15 19:09:56', 'takeaway', '', '', '');


-- ----------------------------
-- Table structure for order_goods
-- ----------------------------
DROP TABLE IF EXISTS `order_goods`;
CREATE TABLE `order_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of order_goods
-- ----------------------------
INSERT INTO `order_goods` (`id`, `order_id`, `goods_id`, `name`, `image`, `price`, `count`)
VALUES
(1, 1, 101, '嫩牛五方超值单人餐', '/images/goods/niuwufang.png', '19.50', 1),
(2, 2, 102, '香辣鸡腿堡单人餐', '/images/goods/jileitui.png', '25.90', 1),
(3, 3, 4, '益禾烤奶', '/images/goods/kaonai.png', '8.00', 1),
(4, 4, 101, '嫩牛五方超值单人餐', '/images/goods/niuwufang.png', '19.50', 1),
(5, 5, 102, '香辣鸡腿堡单人餐', '/images/goods/jileitui.png', '25.90', 1),
(6, 6, 501, '兰州牛肉拉面', '/images/goods/lamian.png', '15.00', 1),
(7, 7, 503, '牛肉面+小菜套餐', '/images/goods/set.png', '22.00', 1),
(8, 8, 101, '嫩牛五方超值单人餐', '/images/goods/niuwufang.png', '19.50', 1),
(9, 9, 301, '奥尔良烤鸡腿堡套餐', '/images/goods/orleans.png', '32.00', 1);


-- ----------------------------
-- Table structure for shops
-- ----------------------------
DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `monthly_sales` int(11) DEFAULT NULL,
  `wait_time` int(11) DEFAULT NULL,
  `distance` varchar(64) DEFAULT NULL,
  `min_price` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of shops
-- ----------------------------
INSERT INTO `shops` (`id`, `name`, `logo`, `rating`, `monthly_sales`, `wait_time`, `distance`, `min_price`)
VALUES
(1, '肯德基（城院店）', '/images/shops/kfc.png', 4.8, 719, 15, '1.8km', 0),
(2, '兰州拉面', '/images/shops/lamian.png', 4.7, 1750, 10, '2.4km', 0),
(3, '益禾堂', '/images/shops/yihetang.png', 4.3, NULL, NULL, NULL, NULL),
(4, '蜜雪冰城', '/images/shops/mixue.png', 4.6, NULL, NULL, NULL, NULL),
(5, '沙县小吃', '/images/shops/shaxian.png', 4.2, NULL, NULL, NULL, NULL);


SET FOREIGN_KEY_CHECKS=1;
