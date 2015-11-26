package bikescheme;

public class User {

    static int userNum = 1;

    private String userId;
    private String name;
    private Key key;
    private String authCode;

    public User(String name, Key key, String authCode) {
        this.name = name;
        this.key = key;
        this.authCode = authCode;
        this.userId = Integer.toString(userNum);
        userNum++;
    }

    public String getUserId() {
        return userId;
    }

    public Key getKey() {
        return key;
    }

    public String getAuthCode() {
        return authCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User b = (User) obj;
            return this.userId.equals(b.getUserId());
        }
        return false;
    }
}