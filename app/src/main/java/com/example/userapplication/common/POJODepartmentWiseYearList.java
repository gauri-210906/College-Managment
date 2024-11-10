package com.example.userapplication.common;

public class POJODepartmentWiseYearList {

    String strID, strDepartment, strYear;

    public POJODepartmentWiseYearList(String strID, String strDepartment, String strYear) {
        this.strID = strID;
        this.strDepartment = strDepartment;
        this.strYear = strYear;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getStrDepartment() {
        return strDepartment;
    }

    public void setStrDepartment(String strDepartment) {
        this.strDepartment = strDepartment;
    }

    public String getStrYear() {
        return strYear;
    }

    public void setStrYear(String strYear) {
        this.strYear = strYear;
    }
}
