package com.example.searchengine.Model;

public class Chat {

    private Message msgFromUser;
    private Message msgFromChatbot;


    public Message getMsgFromUser() {
        return msgFromUser;
    }

    public void setMsgFromUser(Message msgFromUser) {
        this.msgFromUser = msgFromUser;
    }

    public Message getMsgFromChatbot() {
        return msgFromChatbot;
    }

    public void setMsgFromChatbot(Message msgFromChatbot) {
        this.msgFromChatbot = msgFromChatbot;
    }
}
