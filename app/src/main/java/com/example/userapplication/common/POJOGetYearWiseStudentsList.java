package com.example.userapplication.common;

public class POJOGetYearWiseStudentsList {

    String strid, strName, strEnrollmentno;

    public POJOGetYearWiseStudentsList(String strid, String strName, String strEnrollmentno) {
        this.strid = strid;
        this.strName = strName;
        this.strEnrollmentno = strEnrollmentno;
    }

    public String getStrid() {
        return strid;
    }

    public void setStrid(String strid) {
        this.strid = strid;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrEnrollmentno() {
        return strEnrollmentno;
    }

    public void setStrEnrollmentno(String strEnrollmentno) {
        this.strEnrollmentno = strEnrollmentno;
    }
}
