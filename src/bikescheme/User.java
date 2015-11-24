package bikescheme;

public class User {
    private String name;
    private String keyId;
    private String authCode;

    public User(String name, String keyId, String authCode) {
        this.name = name;
        this.keyId = keyId;
        this.authCode = authCode;
    }
}
