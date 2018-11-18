-- MySQL Script generated by MySQL Workbench
-- Sun Nov 18 16:54:09 2018
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`table1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`table1` (
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`customer` (
  `id` INT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `encryptedPassword` VARCHAR(80) NOT NULL,
  `salt` VARCHAR(10) NOT NULL,
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  `uniqueId` VARCHAR(23) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `E-mail_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `unique_id_UNIQUE` (`uniqueId` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`movie` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `runningTimeMin` INT NOT NULL,
  `age` INT NOT NULL,
  `languageVersion` VARCHAR(45) NOT NULL,
  `releaseDate` DATE NOT NULL,
  `description` VARCHAR(600) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`genre` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`seatType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`seatType` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `price` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`reservation` (
  `customerId` INT NOT NULL,
  `movieId` INT NOT NULL,
  `hall` INT NOT NULL,
  `seatNumber` INT NOT NULL,
  `row` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `seatTypeId` INT NOT NULL,
  PRIMARY KEY (`customerId`, `movieId`, `seatTypeId`),
  INDEX `fk_Klient_has_Film_Film1_idx` (`movieId` ASC) VISIBLE,
  INDEX `fk_Klient_has_Film_Klient_idx` (`customerId` ASC) VISIBLE,
  INDEX `fk_Rezerwacja_TypMiejsca1_idx` (`seatTypeId` ASC) VISIBLE,
  CONSTRAINT `fk_Klient_has_Film_Klient`
    FOREIGN KEY (`customerId`)
    REFERENCES `mydb`.`customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Klient_has_Film_Film1`
    FOREIGN KEY (`movieId`)
    REFERENCES `mydb`.`movie` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Rezerwacja_TypMiejsca1`
    FOREIGN KEY (`seatTypeId`)
    REFERENCES `mydb`.`seatType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`gatunekHasMovie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`gatunekHasMovie` (
  `genreId` INT NOT NULL,
  `movieId` INT NOT NULL,
  PRIMARY KEY (`genreId`, `movieId`),
  INDEX `fk_Gatunek_has_Film_Film1_idx` (`movieId` ASC) VISIBLE,
  INDEX `fk_Gatunek_has_Film_Gatunek1_idx` (`genreId` ASC) VISIBLE,
  CONSTRAINT `fk_Gatunek_has_Film_Gatunek1`
    FOREIGN KEY (`genreId`)
    REFERENCES `mydb`.`genre` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gatunek_has_Film_Film1`
    FOREIGN KEY (`movieId`)
    REFERENCES `mydb`.`movie` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`repertoire`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`repertoire` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `movie_id` INT NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_repertoire_movie1_idx` (`movie_id` ASC) VISIBLE,
  CONSTRAINT `fk_repertoire_movie1`
    FOREIGN KEY (`movie_id`)
    REFERENCES `mydb`.`movie` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
