package com.example.userapplication.common;

public class POJOGetAllStaffList {

    String strid, strName, strBranch;

    public POJOGetAllStaffList(String strid, String strName, String strBranch) {
        this.strid = strid;
        this.strName = strName;
        this.strBranch = strBranch;
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

    public String getStrBranch() {
        return strBranch;
    }

    public void setStrBranch(String strBranch) {
        this.strBranch = strBranch;
    }
}
