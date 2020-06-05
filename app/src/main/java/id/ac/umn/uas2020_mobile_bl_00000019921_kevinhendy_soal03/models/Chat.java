package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models;

public class Chat {
    private String chatsForumId;
    private String chatsMessage;
    private String chatsSenderName;
    private String chatsSenderUId;

    public Chat(){}

    public Chat(String chatsForumId, String chatsMessage, String chatsSenderName, String chatsSenderUId) {
        this.chatsForumId = chatsForumId;
        this.chatsMessage = chatsMessage;
        this.chatsSenderName = chatsSenderName;
        this.chatsSenderUId = chatsSenderUId;
    }

    public String getChatsForumId() {
        return chatsForumId;
    }

    public void setChatsForumId(String chatsForumId) {
        this.chatsForumId = chatsForumId;
    }

    public String getChatsMessage() {
        return chatsMessage;
    }

    public void setChatsMessage(String chatsForumMessage) {
        this.chatsMessage = chatsForumMessage;
    }

    public String getChatsSenderName() {
        return chatsSenderName;
    }

    public void setChatsSenderName(String chatsSenderName) {
        this.chatsSenderName = chatsSenderName;
    }

    public String getChatsSenderUId() {
        return chatsSenderUId;
    }

    public void setChatsSenderUId(String chatsSenderUId) {
        this.chatsSenderUId = chatsSenderUId;
    }
}
