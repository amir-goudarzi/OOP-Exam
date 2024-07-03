package it.polito.emergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.*;

public class EmergencyApp {

    TreeMap<String, Professional> professionals;
    TreeMap<String, Department> departments;
    TreeMap<String, Patient> patients;
    TreeMap<String, Report> reports;
    int reportCount;

    public EmergencyApp() {
        this.professionals = new TreeMap<>();
        this.departments = new TreeMap<>();
        this.patients = new TreeMap<>();
        this.reports = new TreeMap<>();
        this.reportCount = 1000;
    }

    public enum PatientStatus {
        ADMITTED,
        DISCHARGED,
        HOSPITALIZED
    }
    
    /**
     * Add a professional working in the emergency room
     * 
     * @param id
     * @param name
     * @param surname
     * @param specialization
     * @param period
     * @param workingHours
     */
    public void addProfessional(String id, String name, String surname, String specialization, String period) {
        Professional newProfessional = new Professional(id, name, surname, specialization, period);
        professionals.put(id, newProfessional);
    }

    /**
     * Retrieves a professional utilizing the ID.
     *
     * @param id The id of the professional.
     * @return A Professional.
     * @throws EmergencyException If no professional is found.
     */    
    public Professional getProfessionalById(String id) throws EmergencyException {
        if (!professionals.containsKey(id)) {
            throw new EmergencyException();
        }
        return professionals.get(id);
    }

    /**
     * Retrieves the list of professional IDs by their specialization.
     *
     * @param specialization The specialization to search for among the professionals.
     * @return A list of professional IDs who match the given specialization.
     * @throws EmergencyException If no professionals are found with the specified specialization.
     */    
    public List<String> getProfessionals(String specialization) throws EmergencyException {
        return professionals.values().stream().filter(pro -> pro.specialization.equals(specialization)).map(pro -> pro.getId())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the list of professional IDs who are specialized and available during a given period.
     *
     * @param specialization The specialization to search for among the professionals.
     * @param period The period during which the professional should be available, formatted as "YYYY-MM-DD to YYYY-MM-DD".
     * @return A list of professional IDs who match the given specialization and are available during the period.
     * @throws EmergencyException If no professionals are found with the specified specialization and period.
     */    
    public List<String> getProfessionalsInService(String specialization, String period) throws EmergencyException {
        List<String> selectedPros = professionals.values().stream().filter(pro -> pro.specialization.equals(specialization) && pro.isAvailable(period))
                    .map(pro -> pro.getId()).collect(Collectors.toList());
        if (selectedPros.isEmpty()) {
            throw new EmergencyException();
        }
        return selectedPros;
    }

    /**
     * Adds a new department to the emergency system if it does not already exist.
     *
     * @param name The name of the department.
     * @param maxPatients The maximum number of patients that the department can handle.
     * @throws EmergencyException If the department already exists.
     */
    public void addDepartment(String name, int maxPatients) {
        Department newDepartment = new Department(name, maxPatients);
        departments.put(name, newDepartment);

    }

    /**
     * Retrieves a list of all department names in the emergency system.
     *
     * @return A list containing the names of all registered departments.
     * @throws EmergencyException If no departments are found.
     */
    public List<String> getDepartments() throws EmergencyException {
        if (departments.isEmpty()) {
            throw new EmergencyException();
        }
        return departments.keySet().stream().collect(Collectors.toList());
    }

    /**
     * Reads professional data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a professional's ID, name, surname, specialization, period of availability, and working hours.
     * The expected format of each line is: matricola, nome, cognome, specializzazione, period, orari_lavoro
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of professionals successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */
    public int readFromFileProfessionals(Reader reader) throws IOException {
        int i = 0;
        int count = 0;
        try {
            if (reader == null) {
                throw new IOException();
            }
            BufferedReader mainReader = new BufferedReader(reader);
            List<String> lines = mainReader.lines().collect(Collectors.toList());
            for (String line: lines) {
                if (i == 0) {
                    i = 1;
                    continue;
                }
                String id = line.split(",")[0];
                String name = line.split(",")[1];
                String surname = line.split(",")[2];
                String specialization = line.split(",")[3];
                String period = line.split(",")[4];
                this.addProfessional(id, name, surname, specialization, period);
                count++;
            }
        } catch (IOException e) {
            throw new IOException();
        }

        return count;
    }

    /**
     * Reads department data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a department's name and the maximum number of patients it can accommodate.
     * The expected format of each line is: nome_reparto, num_max
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of departments successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */    
    public int readFromFileDepartments(Reader reader) throws IOException {
        int i = 0;
        int count = 0;
        try {
            if (reader == null) {
                throw new IOException();
            }
            BufferedReader mainReader = new BufferedReader(reader);
            List<String> lines = mainReader.lines().collect(Collectors.toList());
            for (String line: lines) {
                if (i == 0) {
                    i = 1;
                    continue;
                }
                String name = line.split(",")[0];
                int maxPatients = Integer.parseInt(line.split(",")[1]);
                this.addDepartment(name, maxPatients);
                count++;
            }
        } catch (IOException e) {
            throw new IOException();
        }

        return count;
    }

    /**
     * Registers a new patient in the emergency system if they do not exist.
     * 
     * @param fiscalCode The fiscal code of the patient, used as a unique identifier.
     * @param name The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The birth date of the patient.
     * @param reason The reason for the patient's visit.
     * @param dateTimeAccepted The date and time the patient was accepted into the emergency system.
     */
    public Patient addPatient(String fiscalCode, String name, String surname, String dateOfBirth, String reason, String dateTimeAccepted) {
        if (patients.containsKey(fiscalCode)) {
            return patients.get(fiscalCode);
        }
        Patient newPatient = new Patient(fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted);
        patients.put(fiscalCode, newPatient);
        return newPatient;
    }

    /**
     * Retrieves a patient or patients based on a fiscal code or surname.
     *
     * @param identifier Either the fiscal code or the surname of the patient(s).
     * @return A single patient if a fiscal code is provided, or a list of patients if a surname is provided.
     *         Returns an empty collection if no match is found.
     */    
    public List<Patient> getPatient(String identifier) throws EmergencyException {
        List<Patient> byLastName = patients.values().stream().filter(p -> p.getSurname().equals(identifier))
                                    .collect(Collectors.toList());
        List<Patient> byCode = patients.values().stream().filter(p -> p.getFiscalCode().equals(identifier))
                                .collect(Collectors.toList());
        if (byLastName.size() > byCode.size()) {
            return byLastName;
        } 
        return byCode;
    }

    /**
     * Retrieves the fiscal codes of patients accepted on a specific date, 
     * sorted by acceptance time in descending order.
     *
     * @param date The date of acceptance to filter the patients by, expected in the format "yyyy-MM-dd".
     * @return A list of patient fiscal codes who were accepted on the given date, sorted from the most recent.
     *         Returns an empty list if no patients were accepted on that date.
     */
    public List<String> getPatientsByDate(String date) {
        return patients.values().stream().filter(p -> p.getDateTimeAccepted().equals(date))
        .sorted((p, q) -> p.lastFirst().compareTo(q.lastFirst())).map(p -> p.getFiscalCode()).collect(Collectors.toList());
    }

    /**
     * Assigns a patient to a professional based on the required specialization and checks availability during the request period.
     *
     * @param fiscalCode The fiscal code of the patient.
     * @param specialization The required specialization of the professional.
     * @return The ID of the assigned professional.
     * @throws EmergencyException If the patient does not exist, if no professionals with the required specialization are found, or if none are available during the period of the request.
     */
    public String assignPatientToProfessional(String fiscalCode, String specialization) throws EmergencyException {
        if (!patients.containsKey(fiscalCode)) {
            throw new EmergencyException();
        }
        Patient myPatient = patients.get(fiscalCode);
        List<Professional> selectedProfessionals = professionals.values().stream()
        .filter(p -> p.specialization.equals(specialization) && p.containsDate(myPatient.getDateTimeAccepted()))
        .collect(Collectors.toList());
        if (selectedProfessionals.isEmpty()) {
            throw new EmergencyException();
        }
        if (selectedProfessionals.size() == 1) {
            return selectedProfessionals.get(0).getId();
        }
        Collections.sort(selectedProfessionals, (p, q) -> p.getId().compareTo(q.getId()));                                            
        return selectedProfessionals.get(0).getId();
    }

    public Report saveReport(String professionalId, String fiscalCode, String date, String description) throws EmergencyException {
        if (!professionals.containsKey(professionalId)) {
            throw new EmergencyException();
        }
        Report newReport = new Report(professionalId, fiscalCode, date, description, reportCount);
        reports.put(newReport.getId(), newReport);
        reportCount++;
        return newReport;
    }

    /**
     * Either discharges a patient or hospitalizes them depending on the availability of space in the requested department.
     * 
     * @param fiscalCode The fiscal code of the patient to be discharged or hospitalized.
     * @param departmentName The name of the department to which the patient might be admitted.
     * @throws EmergencyException If the patient does not exist or if the department does not exist.
     */
    public void dischargeOrHospitalize(String fiscalCode, String departmentName) throws EmergencyException {
        //TODO: to be implemented
    }

    /**
     * Checks if a patient is currently hospitalized in any department.
     *
     * @param fiscalCode The fiscal code of the patient to verify.
     * @return 0 if the patient is currently hospitalized, -1 if not hospitalized or discharged.
     * @throws EmergencyException If no patient is found with the given fiscal code.
     */
    public int verifyPatient(String fiscalCode) throws EmergencyException{
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of patients currently being managed in the emergency room.
     *
     * @return The total number of patients in the system.
     */    
    public int getNumberOfPatients() {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of patients admitted on a specified date.
     *
     * @param dateString The date of interest provided as a String (format "yyyy-MM-dd").
     * @return The count of patients admitted on that date.
     */
    public int getNumberOfPatientsByDate(String date) {
        //TODO: to be implemented
        return -1;
    }

    public int getNumberOfPatientsHospitalizedByDepartment(String departmentName) throws EmergencyException {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of patients who have been discharged from the emergency system.
     *
     * @return The count of discharged patients.
     */
    public int getNumberOfPatientsDischarged() {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of discharged patients who were treated by professionals of a specific specialization.
     *
     * @param specialization The specialization of the professionals to filter by.
     * @return The count of discharged patients treated by professionals of the given specialization.
     */
    public int getNumberOfPatientsAssignedToProfessionalDischarged(String specialization) {
        //TODO: to be implemented
        return -1;
    }
}
