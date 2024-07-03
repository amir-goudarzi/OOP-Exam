package it.polito.emergency;

public class Report {

    String reportID, fiscalCode, professionalID, date, description;

    public Report(String professionalId, String fiscalCode, String date, String description) {
        this.fiscalCode = fiscalCode;
        this.professionalID = professionalId;
        this.date = date;
        this.description = description;
        this.reportID = fiscalCode + professionalId + date;
    }

    public String getId() {
        return reportID;
    }

    public String getProfessionalId() {
        return professionalID;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getDate() {
        return date;
    }


    public String getDescription() {
        return description;
    }
}
