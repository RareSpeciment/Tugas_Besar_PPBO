-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2025 at 06:57 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `komorebi_pos`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity_logs`
--

CREATE TABLE `activity_logs` (
  `log_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `details` text DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cafe_tables`
--

CREATE TABLE `cafe_tables` (
  `table_id` int(11) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `cafe_tables`
--

INSERT INTO `cafe_tables` (`table_id`, `table_name`, `capacity`) VALUES
(1, 'Table 01', 2),
(2, 'Table 02', 4),
(3, 'Table 03', 4),
(4, 'VIP 01', 6),
(5, 'Table 4', 6),
(6, 'Table 5', 8),
(7, 'Table 6', 4),
(8, 'Table 7', 4);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `name`) VALUES
(1, 'Coffee'),
(5, 'Drinks'),
(3, 'Main Course'),
(2, 'Non-Coffee'),
(4, 'Snacks');

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `ingredient_id` int(11) NOT NULL,
  `ingredient_name` varchar(100) NOT NULL,
  `quantity` decimal(12,3) NOT NULL DEFAULT 0.000,
  `unit` varchar(20) NOT NULL,
  `min_stock` decimal(12,3) NOT NULL DEFAULT 0.000,
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`ingredient_id`, `ingredient_name`, `quantity`, `unit`, `min_stock`, `updated_at`) VALUES
(1, 'Arabica Coffee Beans', 5123.000, 'gram', 500.000, '2025-12-15 00:01:58'),
(2, 'Fresh Milk', 49.010, 'liter', 5.000, '2025-12-15 00:01:58'),
(3, 'Liquid Sugar', 1930.000, 'ml', 200.000, '2025-12-15 00:01:58'),
(4, 'Matcha Powder', 985.000, 'gram', 100.000, '2025-11-29 22:09:04'),
(5, 'Premium Chocolate', 960.000, 'gram', 100.000, '2025-12-02 10:27:01'),
(6, 'Jasmine Tea Leaves', 495.000, 'gram', 50.000, '2025-12-15 00:01:58'),
(7, 'Premium Rice', 10200.000, 'gram', 1000.000, '2025-12-15 00:01:58'),
(8, 'Chicken Breast', 4900.000, 'gram', 500.000, '2025-12-15 00:01:58'),
(9, 'Frozen French Fries', 5000.000, 'gram', 500.000, '2025-11-29 14:57:48');

-- --------------------------------------------------------

--
-- Table structure for table `inventory_logs`
--

CREATE TABLE `inventory_logs` (
  `log_id` int(11) NOT NULL,
  `ingredient_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `change_amount` decimal(12,3) NOT NULL,
  `note` text DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `inventory_logs`
--

INSERT INTO `inventory_logs` (`log_id`, `ingredient_id`, `user_id`, `change_amount`, `note`, `created_at`) VALUES
(5, 1, 1, -18.000, 'Order Sold (Menu ID: 2)', '2025-11-29 14:23:44'),
(6, 3, 1, -5.000, 'Order Sold (Menu ID: 2)', '2025-11-29 14:23:44'),
(7, 1, 1, -18.000, 'Order Sold (Menu ID: 3)', '2025-11-29 14:23:44'),
(8, 2, 1, -0.120, 'Order Sold (Menu ID: 3)', '2025-11-29 14:23:44'),
(9, 9, 1, -150.000, 'Order Sold (Menu ID: 9)', '2025-11-29 14:23:44'),
(10, 5, 1, -20.000, 'Order Sold (Menu ID: 5)', '2025-11-29 14:24:23'),
(11, 2, 1, -0.200, 'Order Sold (Menu ID: 5)', '2025-11-29 14:24:23'),
(12, 3, 1, -10.000, 'Order Sold (Menu ID: 5)', '2025-11-29 14:24:23'),
(13, 6, 1, -5.000, 'Order Sold (Menu ID: 6)', '2025-11-29 14:24:23'),
(14, 3, 1, -20.000, 'Order Sold (Menu ID: 6)', '2025-11-29 14:24:23'),
(15, 7, 1, -200.000, 'Order Sold (Menu ID: 8)', '2025-11-29 14:24:23'),
(16, 8, 1, -100.000, 'Order Sold (Menu ID: 8)', '2025-11-29 14:24:23'),
(17, 9, 2, 150.000, 'Restock from Canceled Order (Menu ID: 9)', '2025-11-29 14:57:48'),
(18, 1, 1, -18.000, 'Order Sold (Menu ID: 1)', '2025-11-29 21:47:53'),
(19, 2, 1, -0.150, 'Order Sold (Menu ID: 1)', '2025-11-29 21:47:53'),
(20, 3, 1, -10.000, 'Order Sold (Menu ID: 1)', '2025-11-29 21:47:53'),
(21, 4, 1, -15.000, 'Order Sold (Menu ID: 4)', '2025-11-29 21:47:54'),
(22, 2, 1, -0.200, 'Order Sold (Menu ID: 4)', '2025-11-29 21:47:54'),
(23, 3, 1, -15.000, 'Order Sold (Menu ID: 4)', '2025-11-29 21:47:54'),
(24, 6, 1, -5.000, 'Order Sold (Menu ID: 6)', '2025-11-29 21:47:54'),
(25, 3, 1, -20.000, 'Order Sold (Menu ID: 6)', '2025-11-29 21:47:54'),
(26, 7, 1, -200.000, 'Order Sold (Menu ID: 7)', '2025-11-29 21:47:54'),
(27, 8, 1, -50.000, 'Order Sold (Menu ID: 7)', '2025-11-29 21:47:54'),
(28, 4, 2, 15.000, 'Restock from Canceled Order (Menu ID: 4)', '2025-11-29 21:48:19'),
(29, 2, 2, 0.200, 'Restock from Canceled Order (Menu ID: 4)', '2025-11-29 21:48:19'),
(30, 3, 2, 15.000, 'Restock from Canceled Order (Menu ID: 4)', '2025-11-29 21:48:19'),
(31, 6, 2, 5.000, 'Restock from Canceled Order (Menu ID: 6)', '2025-11-29 21:48:19'),
(32, 3, 2, 20.000, 'Restock from Canceled Order (Menu ID: 6)', '2025-11-29 21:48:19'),
(33, 7, 2, 200.000, 'Restock from Canceled Order (Menu ID: 7)', '2025-11-29 21:48:19'),
(34, 8, 2, 50.000, 'Restock from Canceled Order (Menu ID: 7)', '2025-11-29 21:48:19'),
(35, 6, 2, 5.000, 'Restock from Canceled Order (Menu ID: 6)', '2025-11-29 22:08:36'),
(36, 3, 2, 20.000, 'Restock from Canceled Order (Menu ID: 6)', '2025-11-29 22:08:36'),
(37, 7, 2, 200.000, 'Restock from Canceled Order (Menu ID: 8)', '2025-11-29 22:08:36'),
(38, 8, 2, 100.000, 'Restock from Canceled Order (Menu ID: 8)', '2025-11-29 22:08:36'),
(39, 1, 1, -18.000, 'Order Sold (Menu ID: 2)', '2025-11-29 22:08:59'),
(40, 3, 1, -5.000, 'Order Sold (Menu ID: 2)', '2025-11-29 22:08:59'),
(41, 1, 1, -18.000, 'Order Sold (Menu ID: 3)', '2025-11-29 22:08:59'),
(42, 2, 1, -0.120, 'Order Sold (Menu ID: 3)', '2025-11-29 22:08:59'),
(43, 5, 1, -20.000, 'Order Sold (Menu ID: 5)', '2025-11-29 22:08:59'),
(44, 2, 1, -0.200, 'Order Sold (Menu ID: 5)', '2025-11-29 22:08:59'),
(45, 3, 1, -10.000, 'Order Sold (Menu ID: 5)', '2025-11-29 22:08:59'),
(46, 7, 1, -200.000, 'Order Sold (Menu ID: 8)', '2025-11-29 22:08:59'),
(47, 8, 1, -100.000, 'Order Sold (Menu ID: 8)', '2025-11-29 22:08:59'),
(48, 4, 1, -15.000, 'Order Sold (Menu ID: 4)', '2025-11-29 22:09:04'),
(49, 2, 1, -0.200, 'Order Sold (Menu ID: 4)', '2025-11-29 22:09:04'),
(50, 3, 1, -15.000, 'Order Sold (Menu ID: 4)', '2025-11-29 22:09:04'),
(51, 5, 1, -20.000, 'Order Sold (Menu ID: 5)', '2025-11-29 22:09:04'),
(52, 2, 1, -0.200, 'Order Sold (Menu ID: 5)', '2025-11-29 22:09:04'),
(53, 3, 1, -10.000, 'Order Sold (Menu ID: 5)', '2025-11-29 22:09:04'),
(54, 6, 1, -5.000, 'Order Sold (Menu ID: 6)', '2025-11-29 22:09:04'),
(55, 3, 1, -20.000, 'Order Sold (Menu ID: 6)', '2025-11-29 22:09:04'),
(56, 7, 1, -200.000, 'Order Sold (Menu ID: 8)', '2025-11-29 22:09:04'),
(57, 8, 1, -100.000, 'Order Sold (Menu ID: 8)', '2025-11-29 22:09:04'),
(58, 1, 1, -18.000, 'Order Sold (Menu ID: 1)', '2025-11-29 22:09:09'),
(59, 2, 1, -0.150, 'Order Sold (Menu ID: 1)', '2025-11-29 22:09:09'),
(60, 3, 1, -10.000, 'Order Sold (Menu ID: 1)', '2025-11-29 22:09:09'),
(61, 1, 1, -18.000, 'Order Sold (Menu ID: 3)', '2025-11-29 22:09:09'),
(62, 2, 1, -0.120, 'Order Sold (Menu ID: 3)', '2025-11-29 22:09:09'),
(63, 6, 1, -5.000, 'Order Sold (Menu ID: 6)', '2025-11-29 22:09:10'),
(64, 3, 1, -20.000, 'Order Sold (Menu ID: 6)', '2025-11-29 22:09:10'),
(65, 7, 1, -200.000, 'Order Sold (Menu ID: 8)', '2025-11-29 22:09:10'),
(66, 8, 1, -100.000, 'Order Sold (Menu ID: 8)', '2025-11-29 22:09:10'),
(67, 7, 2, 200.000, 'Restock from Canceled Order (Menu ID: 8)', '2025-11-29 22:09:34'),
(68, 8, 2, 100.000, 'Restock from Canceled Order (Menu ID: 8)', '2025-11-29 22:09:34'),
(69, 1, 2, 18.000, 'Restock from Canceled Order (Menu ID: 3)', '2025-12-02 10:14:19'),
(70, 2, 2, 0.120, 'Restock from Canceled Order (Menu ID: 3)', '2025-12-02 10:14:19'),
(71, 1, 7, 5.000, 'alamak', '2025-12-02 10:23:19'),
(72, 1, 7, 90.000, 'alamak', '2025-12-02 10:24:17'),
(73, 1, 7, 100.000, 'WOI', '2025-12-02 10:24:48'),
(74, 7, 7, 400.000, 'sudah??', '2025-12-02 10:25:06'),
(75, 5, 2, 20.000, 'Restock from Canceled Order (Menu ID: 5)', '2025-12-02 10:27:01'),
(76, 2, 2, 0.200, 'Restock from Canceled Order (Menu ID: 5)', '2025-12-02 10:27:01'),
(77, 3, 2, 10.000, 'Restock from Canceled Order (Menu ID: 5)', '2025-12-02 10:27:01'),
(78, 1, 2, 18.000, 'Restock from Canceled Order (Menu ID: 1)', '2025-12-15 00:01:58'),
(79, 2, 2, 0.150, 'Restock from Canceled Order (Menu ID: 1)', '2025-12-15 00:01:58'),
(80, 3, 2, 10.000, 'Restock from Canceled Order (Menu ID: 1)', '2025-12-15 00:01:58'),
(81, 1, 2, 18.000, 'Restock from Canceled Order (Menu ID: 2)', '2025-12-15 00:01:58'),
(82, 3, 2, 5.000, 'Restock from Canceled Order (Menu ID: 2)', '2025-12-15 00:01:58'),
(83, 6, 2, 5.000, 'Restock from Canceled Order (Menu ID: 6)', '2025-12-15 00:01:58'),
(84, 3, 2, 20.000, 'Restock from Canceled Order (Menu ID: 6)', '2025-12-15 00:01:58'),
(85, 7, 2, 200.000, 'Restock from Canceled Order (Menu ID: 8)', '2025-12-15 00:01:58'),
(86, 8, 2, 100.000, 'Restock from Canceled Order (Menu ID: 8)', '2025-12-15 00:01:58');

-- --------------------------------------------------------

--
-- Table structure for table `menu_items`
--

CREATE TABLE `menu_items` (
  `menu_id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `category_id` int(11) NOT NULL,
  `available` tinyint(1) DEFAULT 1,
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `menu_items`
--

INSERT INTO `menu_items` (`menu_id`, `name`, `description`, `price`, `category_id`, `available`, `image_path`) VALUES
(1, 'Komorebi Latte', 'Signature coffee with creamy milk', 25000.00, 1, 1, 'images/Latte.png'),
(2, 'Americano', 'Pure black coffee', 18000.00, 1, 1, 'images/Americano.png'),
(3, 'Cappuccino', 'Espresso with frothy milk', 22000.00, 1, 1, 'images/Cappucino.png'),
(4, 'Matcha Latte', 'Pure Japanese Matcha with milk', 28000.00, 2, 1, 'images/Matcha Latte.png'),
(5, 'Chocolate Delight', 'Rich chocolate drink', 26000.00, 2, 1, 'images/Chocolate Delight.png'),
(6, 'Lemon Tea', 'Refreshing tea with lemon', 15000.00, 5, 1, 'images/Lemon Tea.png'),
(7, 'Nasi Goreng Special', 'Fried rice with chicken and egg', 35000.00, 3, 1, 'images/Nasi Goreng Special.png'),
(8, 'Chicken Katsu Curry', 'Japanese curry with rice', 40000.00, 3, 1, 'images/Chicken Katsu Curry.png'),
(9, 'French Fries', 'Crispy shoestring fries', 15000.00, 4, 1, 'images/French Rice.png');
-- --------------------------------------------------------

--
-- Table structure for table `menu_recipes`
--

CREATE TABLE `menu_recipes` (
  `recipe_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `ingredient_id` int(11) NOT NULL,
  `quantity_required` decimal(12,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `menu_recipes`
--

INSERT INTO `menu_recipes` (`recipe_id`, `menu_id`, `ingredient_id`, `quantity_required`) VALUES
(67, 1, 1, 18.000),
(68, 1, 2, 0.150),
(69, 1, 3, 10.000),
(70, 2, 1, 18.000),
(71, 2, 3, 5.000),
(72, 3, 1, 18.000),
(73, 3, 2, 0.120),
(74, 4, 4, 15.000),
(75, 4, 2, 0.200),
(76, 4, 3, 15.000),
(77, 5, 5, 20.000),
(78, 5, 2, 0.200),
(79, 5, 3, 10.000),
(80, 6, 6, 5.000),
(81, 6, 3, 20.000),
(82, 7, 7, 200.000),
(83, 7, 8, 50.000),
(84, 8, 7, 200.000),
(85, 8, 8, 100.000),
(86, 9, 9, 150.000);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `table_id` int(11) NOT NULL,
  `cashier_id` int(11) DEFAULT NULL,
  `status` enum('NEW','PAID','DONE','FULLY_SERVED','CANCELED') DEFAULT 'NEW',
  `total` decimal(12,2) DEFAULT 0.00,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price_each` decimal(10,2) NOT NULL,
  `status` enum('NEW','PREPARING','DONE','SERVED','CANCELED') DEFAULT 'NEW'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Triggers `order_items`
--
DELIMITER $$
CREATE TRIGGER `auto_update_order_status` AFTER UPDATE ON `order_items` FOR EACH ROW BEGIN
    DECLARE total_active INT;
    DECLARE done_active INT;
    DECLARE served_active INT;
    
    -- 1. Hitung HANYA item yang TIDAK CANCELED (Item Aktif)
    SELECT COUNT(*) INTO total_active 
    FROM order_items WHERE order_id = NEW.order_id AND status != 'CANCELED';
    
    -- 2. Hitung item Aktif yang sudah selesai masak (DONE/SERVED)
    SELECT COUNT(*) INTO done_active 
    FROM order_items WHERE order_id = NEW.order_id 
    AND status != 'CANCELED' AND (status = 'DONE' OR status = 'SERVED');
    
    -- 3. Hitung item Aktif yang sudah diantar (SERVED)
    SELECT COUNT(*) INTO served_active 
    FROM order_items WHERE order_id = NEW.order_id 
    AND status != 'CANCELED' AND status = 'SERVED';

    -- LOGIKA DONE: Jika semua item aktif sudah selesai
    -- (Pastikan ada item aktif minimal 1, supaya order kosong tidak dianggap Done)
    IF (total_active > 0 AND total_active = done_active) THEN
        UPDATE orders SET status = 'DONE' 
        WHERE order_id = NEW.order_id AND status != 'FULLY_SERVED';
    END IF;

    -- LOGIKA FULLY_SERVED: Jika semua item aktif sudah diantar
    IF (total_active > 0 AND total_active = served_active) THEN
        UPDATE orders SET status = 'FULLY_SERVED' 
        WHERE order_id = NEW.order_id;
    END IF;
    
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `payment_method` enum('CASH','CARD','QRIS','TRANSFER') NOT NULL,
  `payment_time` datetime NOT NULL DEFAULT current_timestamp(),
  `cashier_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `receipts`
--

CREATE TABLE `receipts` (
  `receipt_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `receipt_code` varchar(50) NOT NULL,
  `receipt_content` text NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `role_name`) VALUES
(1, 'Administrator'),
(4, 'Barista'),
(3, 'Cashier'),
(6, 'Chef'),
(2, 'Manager'),
(7, 'StockManager'),
(5, 'Waiter');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `role_id` int(11) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  `deactivated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `fullname`, `role_id`, `status`, `deactivated_at`, `created_at`) VALUES
(1, 'admin', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Admin Komorebi', 1, 'ACTIVE', NULL, '2025-11-26 01:00:18'),
(2, 'kasir01', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Siti Cashier', 3, 'ACTIVE', NULL, '2025-11-26 01:00:18'),
(3, 'barista01', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Budi Barista', 4, 'ACTIVE', NULL, '2025-11-26 01:00:18'),
(6, 'cheff', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'ChArnold', 6, 'ACTIVE', NULL, '2025-11-27 19:16:38'),
(7, 'stockmanager', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Stocker', 7, 'ACTIVE', NULL, '2025-11-27 19:17:06'),
(8, 'waiter', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'wait', 5, 'ACTIVE', NULL, '2025-11-27 19:17:29');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `cafe_tables`
--
ALTER TABLE `cafe_tables`
  ADD PRIMARY KEY (`table_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`ingredient_id`),
  ADD UNIQUE KEY `ingredient_name` (`ingredient_name`);

--
-- Indexes for table `inventory_logs`
--
ALTER TABLE `inventory_logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `ingredient_id` (`ingredient_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `menu_items`
--
ALTER TABLE `menu_items`
  ADD PRIMARY KEY (`menu_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `menu_recipes`
--
ALTER TABLE `menu_recipes`
  ADD PRIMARY KEY (`recipe_id`),
  ADD KEY `menu_id` (`menu_id`),
  ADD KEY `ingredient_id` (`ingredient_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `table_id` (`table_id`),
  ADD KEY `cashier_id` (`cashier_id`),
  ADD KEY `idx_order_date` (`created_at`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `cashier_id` (`cashier_id`),
  ADD KEY `idx_payment_date` (`payment_time`);

--
-- Indexes for table `receipts`
--
ALTER TABLE `receipts`
  ADD PRIMARY KEY (`receipt_id`),
  ADD UNIQUE KEY `receipt_code` (`receipt_code`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `role_name` (`role_name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity_logs`
--
ALTER TABLE `activity_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cafe_tables`
--
ALTER TABLE `cafe_tables`
  MODIFY `table_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `inventory_logs`
--
ALTER TABLE `inventory_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;

--
-- AUTO_INCREMENT for table `menu_items`
--
ALTER TABLE `menu_items`
  MODIFY `menu_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `menu_recipes`
--
ALTER TABLE `menu_recipes`
  MODIFY `recipe_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `receipts`
--
ALTER TABLE `receipts`
  MODIFY `receipt_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD CONSTRAINT `activity_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `inventory_logs`
--
ALTER TABLE `inventory_logs`
  ADD CONSTRAINT `inventory_logs_ibfk_1` FOREIGN KEY (`ingredient_id`) REFERENCES `inventory` (`ingredient_id`),
  ADD CONSTRAINT `inventory_logs_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `menu_items`
--
ALTER TABLE `menu_items`
  ADD CONSTRAINT `menu_items_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `menu_recipes`
--
ALTER TABLE `menu_recipes`
  ADD CONSTRAINT `menu_recipes_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menu_items` (`menu_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `menu_recipes_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `inventory` (`ingredient_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`table_id`) REFERENCES `cafe_tables` (`table_id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu_items` (`menu_id`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `receipts`
--
ALTER TABLE `receipts`
  ADD CONSTRAINT `receipts_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
