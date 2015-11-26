package bikescheme;

/**
 * Created by gvsi on 25/11/2015.
 */
public class Bike {
     private String bikeId;

    public Bike(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeId() {
        return bikeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bike) {
            Bike b = (Bike) obj;
            return this.bikeId == b.getBikeId();
        }
        return false;
    }
}
