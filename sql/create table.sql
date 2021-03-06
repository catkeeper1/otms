CREATE TABLE `USER` (
  `USER_NAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(200) DEFAULT NULL,
  `USER_DESCRIPTION` varchar(200) DEFAULT NULL,
  `IS_LOCKED` char(1) DEFAULT NULL,
  `LAST_MODIFIED_TIMESTAMP` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`USER_NAME`)
) ;






CREATE TABLE `MENU` (
  `CODE` varchar(40) NOT NULL,
  `PARENT_CODE` varchar(40) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `FUNCTION_POINT` varchar(40) DEFAULT NULL,
  `MODULE` varchar(200) DEFAULT NULL,
  `LAST_MODIFIED_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ;




CREATE TABLE `ROLE` (
  `ROLE_CODE` varchar(100) NOT NULL,
  `PARENT_ROLE_CODE` varchar(100) DEFAULT NULL,  
  `ROLE_DESCRIPTION` varchar(200) DEFAULT NULL,
  `LAST_MODIFIED_TIMESTAMP` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ROLE_CODE`)
) ;



CREATE TABLE `USER_ROLE` (
  `USER_NAME` varchar(100) NOT NULL,
  `ROLE_CODE` varchar(100) NOT NULL,
  PRIMARY KEY (`USER_NAME`,`ROLE_CODE`)
) ;