-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Firmalar`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Firmalar` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Firmalar` (
  `firma_id` VARCHAR(8) NOT NULL,
  `firma_adi` VARCHAR(45) CHARACTER SET 'utf8' NULL,
  `kontor_id` INT NULL,
  `Kontorler_kontor_id` INT NOT NULL,
  PRIMARY KEY (`firma_id`, `Kontorler_kontor_id`))
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE UNIQUE INDEX `firma_id_UNIQUE` ON `Firmalar` (`firma_id` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Kontorler`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Kontorler` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Kontorler` (
  `kontor_id` INT NOT NULL,
  `firma_id` VARCHAR(8) NULL,
  `toplam_kontor` INT NULL,
  `kalan_kontor` INT NULL,
  `Firmalar_firma_id` VARCHAR(8) NOT NULL,
  `Firmalar_Kontorler_kontor_id` INT NOT NULL,
  PRIMARY KEY (`kontor_id`, `Firmalar_firma_id`, `Firmalar_Kontorler_kontor_id`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Sorgulamalar`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Sorgulamalar` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Sorgulamalar` (
  `sorgu_id` INT NOT NULL,
  `firma_id` VARCHAR(8) NULL,
  `bolge_id` INT NULL,
  `il_id` INT NULL,
  `magaza_id` INT NULL,
  `musteri_id` INT NULL,
  `sorgu_durumu` VARCHAR(20) NULL,
  `sorgu_zaman` DATETIME NULL,
  PRIMARY KEY (`sorgu_id`))
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE UNIQUE INDEX `firma_id_UNIQUE` ON `Sorgulamalar` (`firma_id` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Bolgeler`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Bolgeler` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Bolgeler` (
  `bolge_id` INT NOT NULL,
  `bolge_adi` VARCHAR(45) NULL,
  `firma_id` VARCHAR(8) NULL,
  PRIMARY KEY (`bolge_id`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Iller`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Iller` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Iller` (
  `il_id` INT NOT NULL,
  `il_adi` VARCHAR(45) NULL,
  `bolge_id` INT NULL,
  `Bolgeler_bolge_id` INT NOT NULL,
  PRIMARY KEY (`il_id`, `Bolgeler_bolge_id`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Magazalar`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Magazalar` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Magazalar` (
  `magaza_id` INT NOT NULL,
  `magaza_adi` VARCHAR(30) NULL,
  `firma_id` VARCHAR(8) NULL,
  `il_id` INT NULL,
  `supervisor_id` INT NULL,
  `Iller_il_id` INT NOT NULL,
  `Iller_Bolgeler_bolge_id` INT NOT NULL,
  PRIMARY KEY (`magaza_id`, `Iller_il_id`, `Iller_Bolgeler_bolge_id`))
ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
