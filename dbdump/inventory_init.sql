DROP DATABASE IF EXISTS `inventory`;
CREATE DATABASE  IF NOT EXISTS `inventory` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `inventory`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: inventory
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit`
--

DROP TABLE IF EXISTS `audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `statement` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit`
--

LOCK TABLES `audit` WRITE;
/*!40000 ALTER TABLE `audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Admin',NULL,NULL,NULL,NULL),(2,'User',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email_id` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `date_of_join` date DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_id_UNIQUE` (`email_id`),
  KEY `fk_user_user_role_id` (`role_id`),
  CONSTRAINT `fk_user_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Admin','Admin','admin@norc.com','Male',1,'2020-04-11','admin','$2a$10$QdAMkO5/.sfyk7WURV5emeIwtF7OlPNXQDLJXgV4Qno0WfBp4K6rS',1,'admin','2020-04-11 19:22:26.000000','admin','2020-04-11 19:22:26.000000');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `CUSTOMER_ID` bigint NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DISTRICT` varchar(50) NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `CONTACT_NO` varchar(50) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `CUSTOMER_ID` bigint NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DISTRICT` varchar(50) NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `CONTACT_NO` varchar(50) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `district_city_map`
--

DROP TABLE IF EXISTS `district_city_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `district_city_map` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `DISTRICT` varchar(50) NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `district_city_map`
--

LOCK TABLES `district_city_map` WRITE;
/*!40000 ALTER TABLE `district_city_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `district_city_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `STORE_ID` bigint NOT NULL AUTO_INCREMENT,
  `STORE_NAME` varchar(100) NOT NULL,
  `DURATION_OF_SLOT` int NOT NULL,
  `DELIVERIES_IN_SLOT` int NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`STORE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_breaks`
--

DROP TABLE IF EXISTS `store_breaks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_breaks` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `STORE_ID` bigint NOT NULL,
  `BREAK_FROM` varchar(50) NOT NULL,
  `BREAK_TO` varchar(50) NOT NULL,
  `BREAK_NAME` varchar(50) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `STORE_ID` (`STORE_ID`),
  CONSTRAINT `store_breaks_ibfk_1` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`STORE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_breaks`
--

LOCK TABLES `store_breaks` WRITE;
/*!40000 ALTER TABLE `store_breaks` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_breaks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_holidays`
--

DROP TABLE IF EXISTS `store_holidays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_holidays` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `STORE_ID` bigint NOT NULL,
  `HOLIDAY_NAME` varchar(50) NOT NULL,
  `HOLIDAY_ON` date NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `STORE_ID` (`STORE_ID`),
  CONSTRAINT `store_holidays_ibfk_1` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`STORE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_holidays`
--

LOCK TABLES `store_holidays` WRITE;
/*!40000 ALTER TABLE `store_holidays` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_holidays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_timeslots`
--

DROP TABLE IF EXISTS `store_timeslots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_timeslots` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `STORE_ID` bigint NOT NULL,
  `CUSTOMER_ID` bigint NOT NULL,
  `SLOT_DATE` date NOT NULL,
  `SLOT_FROM` varchar(50) NOT NULL,
  `SLOT_TO` varchar(50) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `STORE_ID` (`STORE_ID`),
  KEY `CUSTOMER_ID` (`CUSTOMER_ID`),
  CONSTRAINT `store_timeslots_ibfk_1` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`STORE_ID`),
  CONSTRAINT `store_timeslots_ibfk_2` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_timeslots`
--

LOCK TABLES `store_timeslots` WRITE;
/*!40000 ALTER TABLE `store_timeslots` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_timeslots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_timing`
--

DROP TABLE IF EXISTS `store_timing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_timing` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `STORE_ID` bigint NOT NULL,
  `DAY` varchar(50) NOT NULL,
  `STORE_STARTS_AT` varchar(50) NOT NULL,
  `STORE_CLOSES_AT` varchar(50) NOT NULL,
  `WEEK_OFF` char(1) NOT NULL DEFAULT 'N',
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `STORE_ID` (`STORE_ID`),
  CONSTRAINT `store_timing_ibfk_1` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`STORE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_timing`
--

LOCK TABLES `store_timing` WRITE;
/*!40000 ALTER TABLE `store_timing` DISABLE KEYS */;
/*!40000 ALTER TABLE `store_timing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `ORDER_NO` bigint NOT NULL AUTO_INCREMENT,
  `STORE_ID` bigint NOT NULL,
  `CUSTOMER_ID` bigint NOT NULL,
  `DELIVERY_DATE` date NOT NULL,
  `SLOT_FROM` varchar(50) NOT NULL,
  `SLOT_TO` varchar(50) NOT NULL,
  `AMT_PAYABLE` varchar(50) NOT NULL,
  `STATUS` varchar(50) NOT NULL DEFAULT 'pending',
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ORDER_NO`),
  KEY `STORE_ID` (`STORE_ID`),
  KEY `CUSTOMER_ID` (`CUSTOMER_ID`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`STORE_ID`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ORDER_NO` bigint NOT NULL,
  `ITEM_NAME` varchar(100) NOT NULL,
  `QUANTITY` varchar(50) NOT NULL,
  `PRICE` varchar(50) NOT NULL,
  `AMOUNT` varchar(50) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime(6) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime(6) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`ID`),
  KEY `ORDER_NO` (`ORDER_NO`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`ORDER_NO`) REFERENCES `orders` (`ORDER_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-05 15:10:10
