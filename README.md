# managementPatientSystem
My degree project


SQL Script to remove all the data(temperature, glucose, blood pressure) from all patients in database hospital table patients:
UPDATE patients SET `temperatureAmount` = 0, `temperatureAvg` = 0, `temperatureMin` = NULL, `temperatureMax` = NULL, `bloodPressureAmount` = 0,
`bloodPressureAvg` = '0,0', `bloodPressureMin` = NULL, `bloodPressureMax` = NULL, `glucoseAmount` = 0, `glucoseAvg` = 0, `glucoseMin` = NULL,
 `glucoseMax` = NULL;
 


SQL script to creating patients table:

CREATE TABLE `hospital`.`patients` ( `id` INT(9) NOT NULL , `email` VARCHAR(45) NOT NULL , `password` VARCHAR(20) NOT NULL , `name` VARCHAR(40) NOT NULL , `phone` VARCHAR(16) NOT NULL , `doctor` INT(9) NULL DEFAULT NULL , `diseaes` TEXT NULL DEFAULT NULL , `tests` VARCHAR(100) NULL DEFAULT NULL , `temperatureMin` VARCHAR(50) NULL DEFAULT NULL , `temperatureMax` VARCHAR(50) NULL DEFAULT NULL , `temperatureAvg` VARCHAR(50) NULL DEFAULT '0' , `temperatureAmount` VARCHAR(50) NULL DEFAULT '0' , `criticalMinTemperature` VARCHAR(50) NULL DEFAULT '35.6' , `criticalMaxTemperature` VARCHAR(50) NULL DEFAULT '38.8' , `bloodPressureMin` VARCHAR(50) NULL DEFAULT NULL , `bloodPressureMax` VARCHAR(50) NULL DEFAULT NULL , `bloodPressureAmount` VARCHAR(50) NOT NULL DEFAULT '0' , `bloodPressureAvg` VARCHAR(50) NOT NULL DEFAULT '0,0' , `criticalBloodPressure` VARCHAR(50) NOT NULL DEFAULT '150,100' , `glucoseMin` VARCHAR(50) NULL DEFAULT NULL , `glucoseMax` VARCHAR(50) NULL DEFAULT NULL , `glucoseAmount` VARCHAR(50) NOT NULL DEFAULT '0' , `glucoseAvg` VARCHAR(50) NOT NULL DEFAULT '0' , `criticalMinGlucose` VARCHAR(50) NOT NULL DEFAULT '70' , `criticalMaxGlucose` VARCHAR(50) NOT NULL DEFAULT '120' ) ENGINE = InnoDB;



SQL script for creating doctors table:

CREATE TABLE `hospital`.`doctors` ( `email` VARCHAR(100) NOT NULL , `name` VARCHAR(100) NOT NULL , `password` VARCHAR(20) NOT NULL , `warnings` VARCHAR(500) NULL DEFAULT '' ) ENGINE = InnoDB;


