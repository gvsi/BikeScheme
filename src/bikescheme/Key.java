package bikescheme;

/**
 * Created by gvsi on 24/11/2015.
 */
public class Key {
    private String keyId;

    // TODO
    // private boolean isMasterKey;


    public Key(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyId() {
        return keyId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Key) {
            Key k = (Key) obj;
            return this.keyId.equals(k.getKeyId());
        }

        return false;
    }
}
