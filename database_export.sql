CREATE DATABASE  IF NOT EXISTS `jmt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `jmt`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: jmt
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
                       `id` int NOT NULL AUTO_INCREMENT,
                       `user_id` int NOT NULL,
                       `category` varchar(45) NOT NULL,
                       `job_date` date NOT NULL,
                       `description` text NOT NULL,
                       `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       `image` blob,
                       `completion_status` tinyint(1) DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       KEY `fk_user_id` (`user_id`),
                       CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `reactiveUserDetails` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,1,'Category Name','2023-10-18','Job description goes here','2023-10-18 18:22:42',NULL,0);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(45) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'USER'),(2,'CONTRACTOR'),(3,'EDITOR'),(4,'ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `expired` tinyint(1) NOT NULL,
                         `revoked` tinyint(1) NOT NULL,
                         `token` varchar(255) NOT NULL,
                         `token_type` enum('BEARER') NOT NULL,
                         `user_id` int DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `token` (`token`),
                         KEY `user_id` (`user_id`),
                         CONSTRAINT `token_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `reactiveUserDetails` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reactiveUserDetails`
--

DROP TABLE IF EXISTS `reactiveUserDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reactiveUserDetails` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `email` varchar(45) NOT NULL,
                        `full_name` varchar(45) NOT NULL,
                        `password` varchar(64) NOT NULL,
                        `enabled` tinyint DEFAULT NULL,
                        `profile_picture` blob,
                        `joined_date` date DEFAULT NULL,
                        `last_active_date` timestamp NULL DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reactiveUserDetails`
--

LOCK TABLES `reactiveUserDetails` WRITE;
/*!40000 ALTER TABLE `reactiveUserDetails` DISABLE KEYS */;
INSERT INTO `reactiveUserDetails` VALUES (1,'user1@hotmail.com','User One','$2a$10$u/Tz.2hm4ah.6UckAxma6OhfciqkiLoYvkXMGcm0mQ8u9R7ak5tXO',1,NULL,NULL,NULL),(2,'user2@gmail.com','User Two','$2a$10$Tlg.il6JfPga2bF4nrP3LOSJ0fjgKwZnzRv8Fp95r3OmxHqxjDBHy',1,NULL,NULL,NULL),(3,'admin@JMT.com','Admin User','$2a$10$PJQaXpPpzA9pBxaR/bQPXenHa5vYoyFbQU4iykZhQwwHGjSFouFi6',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `reactiveUserDetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
                             `user_id` int NOT NULL,
                             `role_id` int NOT NULL,
                             KEY `user_fk_idx` (`user_id`),
                             KEY `role_fk_idx` (`role_id`),
                             CONSTRAINT `role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
                             CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `reactiveUserDetails` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(2,2),(3,4);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-23 13:34:46
