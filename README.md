# managementPatientSystem
My degree project


SQL Script to remove all the data(temperature, glucose, blood pressure) from all patients in database hospital table patients:
UPDATE patients SET `temperatureAmount` = 0, `temperatureAvg` = 0, `temperatureMin` = NULL, `temperatureMax` = NULL, `bloodPressureAmount` = 0,
`bloodPressureAvg` = '0,0', `bloodPressureMin` = NULL, `bloodPressureMax` = NULL, `glucoseAmount` = 0, `glucoseAvg` = 0, `glucoseMin` = NULL,
 `glucoseMax` = NULL;
 
