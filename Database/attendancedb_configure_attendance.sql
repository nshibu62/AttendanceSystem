-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: attendancedb
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `configure_attendance`
--

DROP TABLE IF EXISTS `configure_attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configure_attendance` (
  `config_atten_id` int NOT NULL AUTO_INCREMENT,
  `password_id` int NOT NULL,
  `question_id_1` int DEFAULT NULL,
  `question_id_2` int DEFAULT NULL,
  `question_id_3` int DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`config_atten_id`),
  KEY `question_id_1` (`question_id_1`),
  KEY `question_id_2` (`question_id_2`),
  KEY `question_id_3` (`question_id_3`),
  KEY `password_id` (`password_id`),
  CONSTRAINT `configure_attendance_ibfk_1` FOREIGN KEY (`question_id_1`) REFERENCES `question` (`question_id`),
  CONSTRAINT `configure_attendance_ibfk_2` FOREIGN KEY (`question_id_2`) REFERENCES `question` (`question_id`),
  CONSTRAINT `configure_attendance_ibfk_4` FOREIGN KEY (`password_id`) REFERENCES `passwords` (`password_ID`),
  CONSTRAINT `configure_attendance_obfk_3` FOREIGN KEY (`question_id_3`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configure_attendance`
--

LOCK TABLES `configure_attendance` WRITE;
/*!40000 ALTER TABLE `configure_attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `configure_attendance` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-28 14:08:39
