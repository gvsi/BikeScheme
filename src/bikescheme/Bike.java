package bikescheme;

/**
 * Created by gvsi on 25/11/2015.
 */
public class Bike {
    static int bikeNum = 1;

    private int bikeId;

    public Bike() {
        this.bikeId = bikeNum;
        bikeNum++;
    }

    public int getBikeId() {
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
