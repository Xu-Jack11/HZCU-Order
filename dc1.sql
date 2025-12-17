/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50556
Source Host           : localhost:3306
Source Database       : dc1

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2025-12-07 16:47:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `code` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `module` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_permission
-- ----------------------------

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `role_id` char(10) NOT NULL,
  `name` char(10) DEFAULT NULL,
  `description` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_role
-- ----------------------------

-- ----------------------------
-- Table structure for admin_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_permission`;
CREATE TABLE `admin_role_permission` (
  `role_id` char(10) NOT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  CONSTRAINT `FK_admin_role_permission` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_user
-- ----------------------------

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `role_id` char(10) NOT NULL,
  `admin_id` char(10) NOT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for after_sale
-- ----------------------------
DROP TABLE IF EXISTS `after_sale`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of after_sale
-- ----------------------------

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcement
-- ----------------------------

-- ----------------------------
-- Table structure for audit_log
-- ----------------------------
DROP TABLE IF EXISTS `audit_log`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of audit_log
-- ----------------------------

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of banner
-- ----------------------------

-- ----------------------------
-- Table structure for bundle
-- ----------------------------
DROP TABLE IF EXISTS `bundle`;
CREATE TABLE `bundle` (
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `total_price` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bundle
-- ----------------------------

-- ----------------------------
-- Table structure for bundle_item
-- ----------------------------
DROP TABLE IF EXISTS `bundle_item`;
CREATE TABLE `bundle_item` (
  `dish_id` char(10) NOT NULL,
  `quantity` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bundle_item
-- ----------------------------

-- ----------------------------
-- Table structure for canteen
-- ----------------------------
DROP TABLE IF EXISTS `canteen`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of canteen
-- ----------------------------

-- ----------------------------
-- Table structure for canteen_announcement
-- ----------------------------
DROP TABLE IF EXISTS `canteen_announcement`;
CREATE TABLE `canteen_announcement` (
  `canteen_id` char(10) DEFAULT NULL,
  `title` char(10) DEFAULT NULL,
  `content` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of canteen_announcement
-- ----------------------------

-- ----------------------------
-- Table structure for canteen_status
-- ----------------------------
DROP TABLE IF EXISTS `canteen_status`;
CREATE TABLE `canteen_status` (
  `canteen_id` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `estimated_wait_time` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of canteen_status
-- ----------------------------

-- ----------------------------
-- Table structure for cart_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `cart_snapshot`;
CREATE TABLE `cart_snapshot` (
  `user_id` char(10) DEFAULT NULL,
  `items` char(10) DEFAULT NULL,
  `total_amount` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cart_snapshot
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_template
-- ----------------------------
DROP TABLE IF EXISTS `coupon_template`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of coupon_template
-- ----------------------------

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dish
-- ----------------------------

-- ----------------------------
-- Table structure for dish_category
-- ----------------------------
DROP TABLE IF EXISTS `dish_category`;
CREATE TABLE `dish_category` (
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `sort_order` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dish_category
-- ----------------------------

-- ----------------------------
-- Table structure for dish_spec
-- ----------------------------
DROP TABLE IF EXISTS `dish_spec`;
CREATE TABLE `dish_spec` (
  `dish_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `price` char(10) DEFAULT NULL,
  `stock` char(10) DEFAULT NULL,
  `is_default` char(10) DEFAULT NULL,
  `spicy_level` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dish_spec
-- ----------------------------

-- ----------------------------
-- Table structure for dish_spec_option
-- ----------------------------
DROP TABLE IF EXISTS `dish_spec_option`;
CREATE TABLE `dish_spec_option` (
  `option_type` char(10) DEFAULT NULL,
  `option_name` char(10) DEFAULT NULL,
  `extra_price` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dish_spec_option
-- ----------------------------

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `user_id` char(10) DEFAULT NULL,
  `target_type` char(10) DEFAULT NULL,
  `target_id` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of favorite
-- ----------------------------

-- ----------------------------
-- Table structure for footprint
-- ----------------------------
DROP TABLE IF EXISTS `footprint`;
CREATE TABLE `footprint` (
  `user_id` char(10) DEFAULT NULL,
  `target_type` char(10) DEFAULT NULL,
  `target_id` char(10) DEFAULT NULL,
  `viewed_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of footprint
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `menu_id` char(10) NOT NULL,
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `start_time` char(10) DEFAULT NULL,
  `end_time` char(10) DEFAULT NULL,
  `is_active` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------

-- ----------------------------
-- Table structure for menu_dish
-- ----------------------------
DROP TABLE IF EXISTS `menu_dish`;
CREATE TABLE `menu_dish` (
  `dish_id` char(10) NOT NULL,
  `menu_id` char(10) NOT NULL,
  `sort_order` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_dish
-- ----------------------------

-- ----------------------------
-- Table structure for merchant_account
-- ----------------------------
DROP TABLE IF EXISTS `merchant_account`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of merchant_account
-- ----------------------------

-- ----------------------------
-- Table structure for merchant_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `merchant_operation_log`;
CREATE TABLE `merchant_operation_log` (
  `merchant_account_id` char(10) DEFAULT NULL,
  `operation` char(10) DEFAULT NULL,
  `detail` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of merchant_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_item
-- ----------------------------

-- ----------------------------
-- Table structure for order_status_log
-- ----------------------------
DROP TABLE IF EXISTS `order_status_log`;
CREATE TABLE `order_status_log` (
  `order_id` char(10) DEFAULT NULL,
  `from_status` char(10) DEFAULT NULL,
  `to_status` char(10) DEFAULT NULL,
  `operator_type` char(10) DEFAULT NULL,
  `operator_id` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_status_log
-- ----------------------------

-- ----------------------------
-- Table structure for payment_record
-- ----------------------------
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record` (
  `order_id` char(10) DEFAULT NULL,
  `pay_no` char(10) DEFAULT NULL,
  `channel` char(10) DEFAULT NULL,
  `amount` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `paid_at` char(10) DEFAULT NULL,
  `raw_response` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment_record
-- ----------------------------

-- ----------------------------
-- Table structure for promotion
-- ----------------------------
DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion` (
  `canteen_id` char(10) DEFAULT NULL,
  `name` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `rule` char(10) DEFAULT NULL,
  `start_time` char(10) DEFAULT NULL,
  `end_time` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of promotion
-- ----------------------------

-- ----------------------------
-- Table structure for promotion_dish
-- ----------------------------
DROP TABLE IF EXISTS `promotion_dish`;
CREATE TABLE `promotion_dish` (
  `dish_id` char(10) NOT NULL,
  `discount_price` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of promotion_dish
-- ----------------------------

-- ----------------------------
-- Table structure for refund_record
-- ----------------------------
DROP TABLE IF EXISTS `refund_record`;
CREATE TABLE `refund_record` (
  `order_id` char(10) DEFAULT NULL,
  `after_sale_id` char(10) DEFAULT NULL,
  `refund_no` char(10) DEFAULT NULL,
  `amount` char(10) DEFAULT NULL,
  `reason` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `processed_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of refund_record
-- ----------------------------

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of review
-- ----------------------------

-- ----------------------------
-- Table structure for review_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `review_audit_log`;
CREATE TABLE `review_audit_log` (
  `review_id` char(10) DEFAULT NULL,
  `admin_id` char(10) DEFAULT NULL,
  `action` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of review_audit_log
-- ----------------------------

-- ----------------------------
-- Table structure for settlement
-- ----------------------------
DROP TABLE IF EXISTS `settlement`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of settlement
-- ----------------------------

-- ----------------------------
-- Table structure for settlement_order
-- ----------------------------
DROP TABLE IF EXISTS `settlement_order`;
CREATE TABLE `settlement_order` (
  `settlement_id` char(10) DEFAULT NULL,
  `order_id` char(10) DEFAULT NULL,
  `order_amount` char(10) DEFAULT NULL,
  `platform_fee` char(10) DEFAULT NULL,
  `service_fee` char(10) DEFAULT NULL,
  `ceated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of settlement_order
-- ----------------------------

-- ----------------------------
-- Table structure for system_monitor
-- ----------------------------
DROP TABLE IF EXISTS `system_monitor`;
CREATE TABLE `system_monitor` (
  `id` char(10) NOT NULL,
  `service_name` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `metrics` char(10) DEFAULT NULL,
  `checked_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for system_param
-- ----------------------------
DROP TABLE IF EXISTS `system_param`;
CREATE TABLE `system_param` (
  `id` char(10) NOT NULL,
  `param_key` char(10) DEFAULT NULL,
  `param_value` char(10) DEFAULT NULL,
  `description` char(10) DEFAULT NULL,
  `updated_by` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_param
-- ----------------------------

-- ----------------------------
-- Table structure for transaction_record
-- ----------------------------
DROP TABLE IF EXISTS `transaction_record`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of transaction_record
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` char(10) NOT NULL,
  `openid` char(10) DEFAULT NULL,
  `unionid` char(10) DEFAULT NULL,
  `nickname` char(10) DEFAULT NULL,
  `avatar_url` char(10) DEFAULT NULL,
  `mobile` char(10) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `last_login_at` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `receiver` char(10) DEFAULT NULL,
  `phone` char(10) DEFAULT NULL,
  `campus` char(10) DEFAULT NULL,
  `building` char(10) DEFAULT NULL,
  `is_default` char(10) DEFAULT NULL,
  `is_deleted` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_address
-- ----------------------------

-- ----------------------------
-- Table structure for user_asset
-- ----------------------------
DROP TABLE IF EXISTS `user_asset`;
CREATE TABLE `user_asset` (
  `user_id` char(10) DEFAULT NULL,
  `balance` char(10) DEFAULT NULL,
  `points` char(10) DEFAULT NULL,
  `total_recharge` char(10) DEFAULT NULL,
  `total_spend` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_asset
-- ----------------------------

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `user_id` char(10) NOT NULL,
  `coupon_template_id` char(10) NOT NULL,
  `status` char(10) DEFAULT NULL,
  `user_order_id` char(10) DEFAULT NULL,
  `claimed_at` char(10) DEFAULT NULL,
  `used_at` char(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`coupon_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for user_notification
-- ----------------------------
DROP TABLE IF EXISTS `user_notification`;
CREATE TABLE `user_notification` (
  `user_id` char(10) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `title` char(10) DEFAULT NULL,
  `content` char(10) DEFAULT NULL,
  `is_read` char(10) DEFAULT NULL,
  `extra` char(10) DEFAULT NULL,
  `created_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_notification
-- ----------------------------

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `user_id` char(10) DEFAULT NULL,
  `gender` char(10) DEFAULT NULL,
  `campus_card_no` char(10) DEFAULT NULL,
  `campus_card_status` char(10) DEFAULT NULL,
  `birthday` char(10) DEFAULT NULL,
  `remark` char(10) DEFAULT NULL,
  `updated_at` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_profile
-- ----------------------------

-- ----------------------------
-- Table structure for ’order’
-- ----------------------------
DROP TABLE IF EXISTS `’order’`;
CREATE TABLE `’order’` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ’order’
-- ----------------------------
