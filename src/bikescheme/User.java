package bikescheme;

public class User {

    static int userNum = 0;

    private int userId;
    private String name;
    private String keyId;
    private String authCode;

    public User(String name, String keyId, String authCode) {
        this.name = name;
        this.keyId = keyId;
        this.authCode = authCode;
        this.userId = userNum;
        userNum++;
    }

    public int getUserId() {
        return userId;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getAuthCode() {
        return authCode;
    }
}