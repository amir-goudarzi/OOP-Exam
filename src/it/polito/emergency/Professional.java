package it.polito.emergency;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Professional {

    String name, surname, professionalID, specialization, period;

    public Professional(String ID, String name, String surname, String specialization, String period) {
        this.name = name;
        this.surname = surname;
        this.professionalID = ID;
        this.specialization = specialization;
        this.period = period;
    }

    public String getId() {
        return professionalID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPeriod() {
        return period;
    }

    public boolean isAvailable(String desiredPeriod) {
        // LocalDate startPeriod = LocalDate.parse(this.period.split(" to ")[0]);
        // LocalDate endPeriod = LocalDate.parse(this.period.split(" to ")[1]);
        // LocalDate desiredStart = LocalDate.parse(desiredPeriod.split(" to ")[0]);
        // LocalDate desiredEnd =  LocalDate.parse(desiredPeriod.split(" to ")[1]);
        String startPeriod = this.period.split(" to ")[0];
        String endPeriod = this.period.split(" to ")[1];
        String desiredStart = desiredPeriod.split(" to ")[0];
        String desiredEnd =  desiredPeriod.split(" to ")[1];

        if (desiredStart.compareTo(startPeriod) >= 0 && desiredEnd.compareTo(endPeriod) <= 0) {
            return true;
        }
        return false;

    }

    public String getWorkingHours() {
        return null;
    }
}
