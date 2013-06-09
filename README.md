DataAnalysis
============

A Java-based example for building data analysis tools

Introduction
This is a project-skeleton for configuration and integration of components related to data analysis.   It utilizes the Spring framework, R server,  JDBC/ORM data access,  and RESTful web services.  The code is functional, but in essence it is a starting point to build upon.    Support for the Hadoop framework will follow.

Details
The code is developed with NetBeans IDE and tested with Tomcat server, R Rserve package, and MySql DB.  For test data, R Iris dataset is used.   I could be loaded to the DB with one of the supplyed unit tests.   Before loading create this table

CREATE  TABLE `iris`.`DATA_SET` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `SEPAL_LENGTH` DOUBLE NULL ,
  `SEPAL_WIDTH` DOUBLE NULL ,
  `PETAL_LENGTH` DOUBLE NULL ,
  `PETAL_WIDTH` DOUBLE NULL ,
  `SPECIES` VARCHAR(45) NULL ,
  PRIMARY KEY (`ID`) );

Also edit the properties and log configuration files accordingly.

License
MIT License


