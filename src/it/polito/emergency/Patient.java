package it.polito.emergency;

import it.polito.emergency.EmergencyApp.*;

public class Patient {

    String fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted, departmentName;
    PatientStatus status;

    public Patient(String fiscalCode, String name, String surname, String dateOfBirth, String reason, String dateTimeAccepted) {
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.reason = reason;
        this.dateTimeAccepted = dateTimeAccepted;
        this.status = PatientStatus.ADMITTED;
        this.departmentName = "";
    }

    public void assigntoDepartment(String departmentName) {
        this.departmentName = departmentName;
    }

    public String lastFirst() {
        return this.surname + this.name;
    }

    public String getFiscalCode() {
        return this.fiscalCode;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDateTimeAccepted() {
        return this.dateTimeAccepted;
    }

    public PatientStatus getStatus() {
        return this.status;
    }
}
