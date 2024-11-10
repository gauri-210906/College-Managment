package com.example.userapplication.common;

public class POJOGetAllDepartmentDetails {

    String strID, strDepartment, strDeptimage;

    public POJOGetAllDepartmentDetails(String strID, String strDepartment, String strDeptimage) {
        this.strID = strID;
        this.strDepartment = strDepartment;
        this.strDeptimage = strDeptimage;
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

    public String getStrDeptimage() {
        return strDeptimage;
    }

    public void setStrDeptimage(String strDeptimage) {
        this.strDeptimage = strDeptimage;
    }
}
