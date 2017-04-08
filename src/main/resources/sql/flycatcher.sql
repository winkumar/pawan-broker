-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: flycatcher_pawnbroker
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `account_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_number` varchar(100) DEFAULT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `father_name` varchar(100) DEFAULT NULL,
  `present_address` varchar(250) DEFAULT NULL,
  `current_address` varchar(250) DEFAULT NULL,
  `area` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `pin_code` varchar(50) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `account_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_number` (`account_number`),
  UNIQUE KEY `account_number_2` (`account_number`),
  KEY `created_by` (`created_by`),
  KEY `modified_by` (`modified_by`),
  KEY `account_type` (`account_type`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `account_ibfk_2` FOREIGN KEY (`modified_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `account_ibfk_3` FOREIGN KEY (`account_type`) REFERENCES `account_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'ACC1','string','string','string','test','string','string','test','string','string','2017-03-31','2017-04-08',1,1,2),(2,'CA2','string','string','string','test','string','string','test','string','string','2017-04-08','2017-04-08',1,1,1),(3,'AA3','string','string','string','present address','string','string','test','string','string','2017-04-08','2017-04-08',1,1,3);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_type`
--

DROP TABLE IF EXISTS `account_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_type` varchar(100) NOT NULL,
  `account_type_desc` varchar(100) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `acc_start_from` varchar(50) DEFAULT NULL,
  `account_type_api` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_type`
--

LOCK TABLES `account_type` WRITE;
/*!40000 ALTER TABLE `account_type` DISABLE KEYS */;
INSERT INTO `account_type` VALUES (1,'Capital','Capital account',NULL,'CA','capital'),(2,'Gold Account','Gold Account',NULL,'GA','gold'),(3,'Gain Account','Gain Account',NULL,'AA','gain'),(4,'Expense Account','Expense Account',NULL,'EA','expense'),(5,'Personal Account','Personal Account',NULL,'PA','personal'),(6,'Bank Account','Bank Account',NULL,'BA','bank');
/*!40000 ALTER TABLE `account_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `day_book`
--

DROP TABLE IF EXISTS `day_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `day_book` (
  `day_book_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `transaction_amount` decimal(10,2) NOT NULL,
  `transaction_type` enum('DEBIT','CREDIT') DEFAULT NULL,
  `transaction_desc` varchar(200) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `transaction_date` date DEFAULT NULL,
  PRIMARY KEY (`day_book_id`),
  KEY `created_by` (`created_by`),
  KEY `modified_by` (`modified_by`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `day_book_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `day_book_ibfk_2` FOREIGN KEY (`modified_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `day_book_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `day_book`
--

LOCK TABLES `day_book` WRITE;
/*!40000 ALTER TABLE `day_book` DISABLE KEYS */;
INSERT INTO `day_book` VALUES (1,1,10.00,'CREDIT','string','2017-03-31','2017-03-31',1,1,NULL),(2,1,10.00,'CREDIT','string','2017-03-31','2017-03-31',1,1,'2017-03-31'),(3,1,1000.00,'CREDIT','string','2017-04-08','2017-04-08',1,1,'2017-04-08'),(4,1,1000.00,'CREDIT','string','2017-04-08','2017-04-08',1,1,'2017-04-07'),(5,1,500.00,'DEBIT','string','2017-04-08','2017-04-08',1,1,'2017-04-07'),(6,1,500.00,'DEBIT','string','2017-04-08','2017-04-08',1,1,'2017-03-31'),(7,1,1010.00,'DEBIT','string','2017-04-08','2017-04-08',1,1,'2017-04-07'),(8,1,1010.00,'DEBIT','string','2017-04-08','2017-04-08',1,1,'2017-04-07'),(9,2,1010.00,'DEBIT','string','2017-04-08','2017-04-08',1,1,'2017-04-07'),(10,3,1010.00,'CREDIT','string','2017-04-08','2017-04-08',1,1,'2017-04-07'),(11,2,200.00,'CREDIT','test','2017-04-08','2017-04-08',1,1,'2017-04-07');
/*!40000 ALTER TABLE `day_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `role_desc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_ADMIN','Admin'),(2,'ROLE_USER','User');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `last_login` date DEFAULT NULL,
  `last_password_update` date DEFAULT NULL,
  `is_enable` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'kumar','$2a$04$FVw/SmaoqZ9yKLau2M2kz.beiD47pgFTW4DD0i4rhPXJOIObkNMAm','kumar','D','2017-04-08',NULL,1);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-08 20:44:10
