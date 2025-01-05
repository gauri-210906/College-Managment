package com.example.userapplication.common;

public class POJOGetAllNotification {
    String id,year,title;

    public POJOGetAllNotification(String id, String year, String title) {
        this.id = id;
        this.year = year;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
