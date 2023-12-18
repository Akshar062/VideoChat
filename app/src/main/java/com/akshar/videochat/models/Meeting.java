package com.akshar.videochat.models;

public class Meeting {
    private String meetingName;
    private String createdAt;

    public Meeting() {
    }

    public Meeting(String meetingName, String createdAt) {
        this.meetingName = meetingName;
        this.createdAt = createdAt;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}