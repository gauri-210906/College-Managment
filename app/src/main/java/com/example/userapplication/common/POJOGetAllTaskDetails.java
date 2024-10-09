package com.example.userapplication.common;

public class POJOGetAllTaskDetails {
    // plain old java object
    // reusability
    // get or set multiple data

    String id, taskImage, subject, taskDescription, deadline;

    public POJOGetAllTaskDetails(String sid, String ssubject, String staskdescription, String sdeadline) {
        this.id = sid;
        this.subject = ssubject;
        this.taskDescription = staskdescription;
        this.deadline = sdeadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
