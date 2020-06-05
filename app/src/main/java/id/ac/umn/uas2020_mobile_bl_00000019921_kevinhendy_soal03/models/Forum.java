package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models;

public class Forum {
    private String forumId;
    private String forumUserUId;
    private String forumTitle;
    private String forumBody;
    private int forumCount;
    private String forumPublisher;
    private String forumUrl;

    public Forum(){}

    public Forum(String forumId, String forumUserUId, String forumTitle, String forumBody, int forumCount, String forumPublisher, String forumUrl) {
        this.forumId = forumId;
        this.forumUserUId = forumUserUId;
        this.forumTitle = forumTitle;
        this.forumBody = forumBody;
        this.forumCount = forumCount;
        this.forumPublisher = forumPublisher;
        this.forumUrl = forumUrl;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getForumUserUId() {
        return forumUserUId;
    }

    public void setForumUserUId(String forumUserUId) {
        this.forumUserUId = forumUserUId;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getForumBody() {
        return forumBody;
    }

    public void setForumBody(String forumBody) {
        this.forumBody = forumBody;
    }

    public int getForumCount() {
        return forumCount;
    }

    public void setForumCount(int forumCount) {
        this.forumCount = forumCount;
    }

    public String getForumPublisher() {
        return forumPublisher;
    }

    public void setForumPublisher(String forumPublisher) {
        this.forumPublisher = forumPublisher;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl = forumUrl;
    }
}
