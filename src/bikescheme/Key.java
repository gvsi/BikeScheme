package bikescheme;

/**
 * Created by gvsi on 24/11/2015.
 */
public class Key {
    private String keyId;
    private boolean isMasterKey;

    public Key(String keyId, boolean isMasterKey) {
        this.keyId = keyId;
        this.isMasterKey = isMasterKey;
    }

    public String getKeyId() {
        return keyId;
    }

    public boolean isMasterKey() {
        return isMasterKey;
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
