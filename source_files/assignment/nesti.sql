-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2021 at 11:30 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nesti`
--
CREATE DATABASE IF NOT EXISTS `nesti` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `nesti`;

-- --------------------------------------------------------

--
-- Table structure for table `registered_user`
--

DROP TABLE IF EXISTS `registered_user`;
CREATE TABLE `registered_user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `first_name` varchar(200) DEFAULT NULL,
  `last_name` varchar(200) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `password_hash` varchar(200) NOT NULL,
  `registration_date` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `registered_user`
--

INSERT INTO `registered_user` (`user_id`, `username`, `email`, `first_name`, `last_name`, `city`, `password_hash`, `registration_date`) VALUES
(428, 'a', 'a@a.a', '', '', '', '$2a$06$g6iuU7Qs8LZEpt/kk88IL.xOC3LNg.rBvKc627bhqmEJrwHssvFH.', '06/01/2021'),
(429, 'judy', 'dsq@d.dd', '', '', '', '$2a$06$eMlyyDuOjB81QIQzaeA9P.lTH2rheYqCi9pRiUV7Q1AqldtR5wTG6', '06/01/2021'),
(430, 'bob', 'bobby@bob.bob', 'Bobby', 'Brown', '', '$2a$06$a.hUqiLyRfhrYSBnF7//Te0zYatp4jFw7EK3XW5Ks5thSUZpliTWu', '06/01/2021'),
(431, 'jack', 'jacky@d.dd', '', '', '', '$2a$06$t8AJZAZj5EtGizAl.EzeTOaYFMwWkeDKr2sHlZkVgkCpq5TPR97Y6', '06/01/2021'),
(432, 'stacy', 's@s.s', '', '', '', '$2a$06$2EVJ/wb6FZQ./RwjIy0OdubjGI5uQhsWU27Bv0Xl2fADNaD6oXuDy', '06/01/2021'),
(433, 'erik', 'hoopsnale@gmail.com', '', '', '', '$2a$06$ECtvx9IhBfqS4kfx6Lji2eo4wpAYtOMPHn7yKqPHeaouQa3sIkyUy', '06/01/2021');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `registered_user`
--
ALTER TABLE `registered_user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `registered_user`
--
ALTER TABLE `registered_user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=434;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
