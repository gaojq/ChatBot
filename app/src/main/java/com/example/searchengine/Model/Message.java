package com.example.searchengine.Model;

public class Message {

    private String message;
    private boolean isSelf;
    private Enum mode;
    private long executionTime;

    public long getExecutionTime() { return executionTime; }

    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }

    public Enum getMode() { return mode; }

    public void setMode(Enum mode) { this.mode = mode; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
