package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.models;

public class User {
    private String usersEmail;
    private String usersNIM;
    private String usersName;
    private String usersUId;
    private String usersUrl;

    public User(){}

    public User(String usersEmail, String usersNIM, String usersName, String usersUId, String usersUrl) {
        this.usersEmail = usersEmail;
        this.usersNIM = usersNIM;
        this.usersName = usersName;
        this.usersUId = usersUId;
        this.usersUrl = usersUrl;
    }

    public String getUsersEmail() {
        return usersEmail;
    }

    public void setUsersEmail(String usersEmail) {
        this.usersEmail = usersEmail;
    }

    public String getUsersNIM() {
        return usersNIM;
    }

    public void setUsersNIM(String usersNIM) {
        this.usersNIM = usersNIM;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getUsersUId() {
        return usersUId;
    }

    public void setUsersUId(String usersUId) {
        this.usersUId = usersUId;
    }

    public String getUsersUrl() {
        return usersUrl;
    }

    public void setUsersUrl(String usersUrl) {
        this.usersUrl = usersUrl;
    }
}
